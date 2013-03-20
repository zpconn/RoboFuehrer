package lhs;

import robocode.*;
import java.util.*;

// This movement strategy picks a set of candidates moves (distributed along a circle about the robot)
// and evaluates the risk of each move. It moves the robot to the destination of lowest risk.
class MinimumRiskMovement extends MovementStrategy
{
	public MinimumRiskMovement(AdvancedRobot ourBot, EnemyManager enemyManager, EnemyBulletManager enemyBulletManager,
			                   MovementManager moveManager)
	{
		super(ourBot, enemyManager, enemyBulletManager, moveManager);
	}
	
	public void move()
	{
		Vec2 dest = findBestSamplePoint();
		moveManager.moveTo(dest);
	}
	
	public MovementStrategy getNextStrategy()
	{
		MovementStrategy nextStrategy = null;
		EnemyBot target = enemyManager.getCurrTarget();
		
		if (target != null && ourBot.getOthers() <= 1)
		{			
			if (target.doesMusashiTrickWork)
				nextStrategy = new MusashiTrick(ourBot, enemyManager, enemyBulletManager, moveManager);
			else 
				nextStrategy = new RandomOrbitalMovement(ourBot, enemyManager, enemyBulletManager, moveManager);
		}
		
		return nextStrategy;
	}
	
	private Vec2 findBestSamplePoint()
	{
		Vec2 bestSamplePoint = Utils.robotPos(ourBot);
		double lowestRisk = calcRisk(bestSamplePoint);
		
		for (int i = 0; i < NUM_SAMPLE_POINTS; ++i)
		{
			Vec2 samplePoint = computeNthSamplePoint(i);
			
			if (!Battlefield.containsPoint(samplePoint))
				continue;
			
			double risk = calcRisk(samplePoint);
			
			if (risk < lowestRisk)
			{
				bestSamplePoint = samplePoint;
				lowestRisk = risk;
			}
		}
		
		return bestSamplePoint;
	}
	
	private Vec2 computeNthSamplePoint(int N)
	{
		double angle = N * SAMPLE_ANGULAR_OFFSET;
		
		return Utils.robotPos(ourBot).add(Vec2.fromPolar(SAMPLE_CIRCLE_RADIUS, angle));
	}
	
	private double calcRisk(Vec2 position)
	{
		return WALL_RISK_WEIGHT * Battlefield.calcWallRisk(position) +
		       CENTER_RISK_WEIGHT * Battlefield.calcCenterRisk(position) +
		       ENEMY_RISK_WEIGHT * enemyManager.calcRisk(position) +
		       BULLET_RISK_WEIGHT * enemyBulletManager.calcRisk(position);
	}
	
	private final int NUM_SAMPLE_POINTS = 250;
	private final double SAMPLE_CIRCLE_RADIUS = 150;
	private final double SAMPLE_ANGULAR_OFFSET = (2*Math.PI) / NUM_SAMPLE_POINTS;
	
	private final double WALL_RISK_WEIGHT = 0;
	private final double CENTER_RISK_WEIGHT = 0;
	private final double ENEMY_RISK_WEIGHT = 1;
	private final double BULLET_RISK_WEIGHT = 2.5;
}