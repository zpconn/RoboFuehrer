package lhs;

import robocode.*;

class RandomOrbitalMovement extends MovementStrategy
{
	public RandomOrbitalMovement(AdvancedRobot ourBot, EnemyManager enemyManager, EnemyBulletManager enemyBulletManager,
            MovementManager moveManager)
	{
		super(ourBot, enemyManager, enemyBulletManager, moveManager);
	}
	
	public void move()
	{
		EnemyBot target = enemyManager.getCurrTarget();
		
		if (target == null)
			return;
		
		double impactTime = target.getDistance(ourBot) / Utils.bulletSpeed(lastBulletPower);
		
		if ((Math.random() * impactTime * 0.45) < 1)
			orbitDirection *= -1;
		
		double orbitRadius = 450;
		
		moveManager.orbitAboutWithWallSmoothing(target.getMovementInfo().pos, 
				orbitRadius, orbitDirection, angularVelocity);
	}
	
	public void onHitByBullet(HitByBulletEvent evt)
	{
		lastBulletPower = evt.getPower();
	}
	
	private double lastBulletPower = 1;
	private int orbitDirection = 1;
	private double angularVelocity = Math.PI / 8;
}