package lhs;

import robocode.*;
import java.awt.Color;

public class RoboFuehrer extends AdvancedRobot
{	
	public void run()
	{
		initialize();
		
		while (true) 
		{				
			radarManager.update();
			movementManager.update();
			enemyManager.update();
			enemyBulletManager.update();
			movementStrategyManager.update();
			
			if (enemyManager.getCurrTarget() != null)
			{
				Gun gun = enemyManager.getCurrTarget().getGunStatsManager().getBestGun();
				BattleSituation situation = new BattleSituation(new MovementInfo(this),
						                              enemyManager.getCurrTarget().getMovementInfo());
				gun.fireAtTarget(situation, Firepower.compute(this, enemyManager.getCurrTarget()));
			}
		
			execute();
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent evt)
	{
		radarManager.onScannedRobot(evt);
		enemyManager.update(evt);
		movementStrategyManager.onScannedRobot(evt);
	}
	
	public void onRobotDeath(RobotDeathEvent evt)
	{
		enemyManager.onRobotDeath(evt);
	}
	
	public void onHitWall(HitWallEvent evt)
	{
		movementStrategyManager.onHitWall(evt);
	}
	
	public void onHitByBullet(HitByBulletEvent evt)
	{
		movementStrategyManager.onHitByBullet(evt);
	}
	
	private void initialize()
	{
		setColors(Color.darkGray, Color.white, Color.red);
		
		if (getRoundNum() == 0)
			initializeBattle();
		else
			restart();
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
	}
	
	// Initialization done only once per battle
	private void initializeBattle()
	{
		movementManager = new MovementManager(this);
		enemyManager = new EnemyManager(this);
		radarManager = new RadarManager(this, enemyManager);
		enemyBulletManager = new EnemyBulletManager(this, enemyManager);
		movementStrategyManager = new MovementStrategyManager(this);
		movementStrategyManager.setMovementStrategy(
				new MinimumRiskMovement(this, enemyManager, enemyBulletManager, movementManager));
		Battlefield.initialize(this);
	}
	
	// Initialization done in second and subsequent rounds
	private void restart()
	{
		radarManager.restart();
		movementManager.restart();
		enemyManager.restart();
		enemyBulletManager.restart();
		movementStrategyManager.setMovementStrategy(
				new MinimumRiskMovement(this, enemyManager, enemyBulletManager, movementManager));
	}
	
	static MovementManager movementManager = null;
	static EnemyManager enemyManager = null;
	static EnemyBulletManager enemyBulletManager = null;
	static MovementStrategyManager movementStrategyManager = null;
	static RadarManager radarManager = null;
}
