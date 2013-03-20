package lhs;

import robocode.*;

class MusashiTrick extends MovementStrategy
{
	public MusashiTrick(AdvancedRobot ourBot, EnemyManager enemyManager, EnemyBulletManager enemyBulletManager,
            MovementManager moveManager)
	{
		super(ourBot, enemyManager, enemyBulletManager, moveManager);
		lastOrbitInversionTime = ourBot.getTime();
	}
	
	public void move()
	{
		if (enemyManager.getCurrTarget() == null)
			return;
		
		setOrbitRadius();
		
		Vec2 dest = moveManager.orbitAbout(enemyManager.getCurrTarget().getMovementInfo().pos,
				orbitRadius, orbitDirection, angularVelocity);
		
		
		if (!Battlefield.containsPoint(dest))
		{
			orbitDirection *= -1;
			lastOrbitInversionTime = ourBot.getTime();
		}
	}
	
	public void onHitByBullet(HitByBulletEvent evt)
	{
		if (enemyManager.getCurrTarget() == null)
			return;
		
		long timeSinceLastOrbitInversion = ourBot.getTime() - lastOrbitInversionTime;
		double enemyDistance = enemyManager.getCurrTarget().getDistance(ourBot);
		
		if (enemyDistance > 200 && timeSinceLastOrbitInversion > enemyDistance / evt.getVelocity())
			headOnHits++;
	}
	
	public MovementStrategy getNextStrategy()
	{
		if (headOnHits >= MAX_HEAD_ON_HITS)
		{
			enemyManager.getCurrTarget().doesMusashiTrickWork = false;
			
			return new RandomOrbitalMovement(ourBot, enemyManager, enemyBulletManager, moveManager);
		}
		
		return null;
	}
	
	private void setOrbitRadius()
	{
		Vec2 targetPos = enemyManager.getCurrTarget().getMovementInfo().pos;
		
		double minXDistance = Math.min(targetPos.x, Battlefield.getWidth() - targetPos.x);
		double minYDistance = Math.min(targetPos.y, Battlefield.getHeight() - targetPos.y);
		double minDistance = Math.min(minXDistance, minYDistance);
		
		orbitRadius = Math.max(300, enemyManager.getCurrTarget().getDistance(ourBot));
		
		if (minDistance < Constants.WALL_AVOID_DISTANCE)
			orbitRadius -= minDistance;
	}
	
	private int orbitDirection = 1;
	private double angularVelocity = Math.PI / 8;
	private double orbitRadius = 350;
	private long lastOrbitInversionTime = 0;
	private int headOnHits = 0;
	
	private final int MAX_HEAD_ON_HITS = 1;
}