package lhs;

import robocode.*;

// This manager controls the radar to obtain scans in the most efficient manner. In melee
// the radar does minimal sweeps just enough to get all the enemies and then it reverses
// so that it avoids wasting time sweeping large areas of the battlefield devoid of enemies.
// In one-on-one the radar just points right at the enemy all the time.
public class RadarManager extends Manager
{
	public RadarManager(AdvancedRobot ourBot, EnemyManager enemyManager)
	{
		super(ourBot);
		this.enemyManager = enemyManager;
	}
	
	public void update()
	{
        // The EnemyManager keeps enemy data over rounds, except it resets all the values to 0.
		// At the beginning of another round, then, the EnemyManager will have all enemy bots
		// recorded as sitting at the origin (0,0). This causes some subtle problems with the 
		// scanning algorithms below. Specifically, if there is only one enemy robot, then
		// the radar will point at the origin at the beginning of a new round and never scan the
		// target to get updated information. Thus, we must force the radar to go through a complete
		// revolution at the beginning of every round to ensure that every enemy robot gets a new
		// scan and updated information.
		
		timeSinceLastScan++;
		
        // First let's make sure every enemy has actually been scanned.
		
		if (ourBot.getOthers() > enemyManager.getEnemyList().size())
		{				
			turnRadar(Double.POSITIVE_INFINITY);
			return;
		}
		
		if (searchForEnemy)
		{
			turnRadar(Double.POSITIVE_INFINITY);
			return;
		}
		
		double radarOffset = 0;
		
		if (ourBot.getOthers() > 1)
		{			
			radarOffset = Double.POSITIVE_INFINITY;
		}
		else if (ourBot.getOthers() == 1)
		{			
			if (numEnemiesLastFrame > 1)
			{					
				numEnemiesLastFrame = 1;
				searchForEnemy = true;
				return;
			}
			
			if (timeSinceLastScan < 3 && enemyManager.getCurrTarget() != null)
			{
				double angleDelta = robocode.util.Utils.normalRelativeAngle(
						ourBot.getRadarHeadingRadians() -
						enemyManager.getCurrTarget().getAbsBearing(ourBot));
				radarOffset = angleDelta + Utils.sign(angleDelta) * 0.02;
			}
		}
		
		numEnemiesLastFrame = ourBot.getOthers();
		
		turnRadar(radarOffset);
	}
	
	public void onScannedRobot(ScannedRobotEvent evt)
	{
		timeSinceLastScan = 0;
		
		searchForEnemy = false;
	}
	
	public void restart()
	{
		numEnemiesLastFrame = ourBot.getOthers();
		
		if (ourBot.getOthers() == 1)
			searchForEnemy = true;
	}
	
	private void turnRadar(double signedAngle)
	{
		if (signedAngle >= 0)
			ourBot.setTurnRadarLeftRadians(signedAngle);
		else 
			ourBot.setTurnRadarRightRadians(-signedAngle);
	}
	
	
	private EnemyManager enemyManager = null;
	
	private int timeSinceLastScan = 10;
	
	private int numEnemiesLastFrame = 1;
	private boolean searchForEnemy = true;
}