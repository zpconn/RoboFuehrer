package lhs;

import robocode.*;

class MovementStrategy
{
	public MovementStrategy(AdvancedRobot ourBot, EnemyManager enemyManager, EnemyBulletManager enemyBulletManager,
			                MovementManager moveManager)
	{
		this.ourBot = ourBot;
		this.enemyManager = enemyManager;
		this.enemyBulletManager = enemyBulletManager;
		this.moveManager = moveManager;
	}
	
	public void onScannedRobot(ScannedRobotEvent evt) { }
	public void onHitWall(HitWallEvent evt) { }
	public void onHitByBullet(HitByBulletEvent evt) { }
	public void move() { }
	
	// A value of null means that the robot should stick with its current strategy
	public MovementStrategy getNextStrategy() { return null; }
	
	protected AdvancedRobot ourBot = null;
	protected EnemyManager enemyManager = null;
	protected EnemyBulletManager enemyBulletManager = null;
	protected MovementManager moveManager = null;
}