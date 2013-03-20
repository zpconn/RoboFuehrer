package lhs;

import robocode.*;

class MovementStrategyManager extends Manager
{
	public MovementStrategyManager(AdvancedRobot ourBot)
	{
		super(ourBot);
	}
	
	public void update()
	{
		if (currentStrategy == null)
			return;
		
		checkForStrategyUpdate();
		currentStrategy.move();
	}
	
	public void restart()
	{
		
	}
	
	public void setMovementStrategy(MovementStrategy strategy)
	{
		currentStrategy = strategy;
	}
	
	public void onScannedRobot(ScannedRobotEvent evt)
	{
		if (currentStrategy != null)
			currentStrategy.onScannedRobot(evt);
	}
	
	public void onHitWall(HitWallEvent evt) 
	{
		if (currentStrategy != null)
			currentStrategy.onHitWall(evt);
	}
	
	public void onHitByBullet(HitByBulletEvent evt) 
	{
		if (currentStrategy != null)
			currentStrategy.onHitByBullet(evt);
	}
	
	private void checkForStrategyUpdate()
	{
		if (currentStrategy == null)
			return;
		
		MovementStrategy newStrategy = currentStrategy.getNextStrategy();
		
		if (newStrategy != null)
			currentStrategy = newStrategy;
	}
	
	private MovementStrategy currentStrategy = null;
}