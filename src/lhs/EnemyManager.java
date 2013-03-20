package lhs;

import java.util.*;
import robocode.*;

// This manager handles the surveillance of enemy robots. It keeps a list of enemies and updates
// statistics on each.
public class EnemyManager extends Manager
{	
	public EnemyManager(AdvancedRobot ourBot)
	{
		super(ourBot);
	}
	
	public void update()
	{
		findBestTarget();
		
		for (EnemyBot enemyBot : enemies.values())
			enemyBot.update();
	}
	
	// Reinitializes statistics about all robots in the list in the second and subsequent rounds.
	public void restart()
	{
		for (EnemyBot enemy : enemies.values())
			enemy.reinitialize();
		
		findBestTarget();
	}
	
	// Searches the internal hash map to find the enemy robot with the given name.
	public EnemyBot get(String name)
	{
		return enemies.get(name);
	}
	
	public Collection<EnemyBot> getEnemyList()
	{
		return enemies.values();
	}
	
	// Updates the enemy robot to which a ScannedRobotEvent corresponds.
	public void update(ScannedRobotEvent evt)
	{
		if (enemies.containsKey(evt.getName()))
		{
			EnemyBot enemy = get(evt.getName());
			enemy.update(evt, ourBot);
		}
		else
		{
			enemies.put(evt.getName(), new EnemyBot(evt, ourBot));
		}
		
		for (EnemyBot enemy : enemies.values())
		{
			enemy.getGunStatsManager().onScannedRobot(evt);
		}
	}
	
	public void onRobotDeath(RobotDeathEvent evt)
	{
		EnemyBot enemy = get(evt.getName());
		enemy.kill();
	}
	
	public double calcRisk(Vec2 point)
	{
		double totalRisk = 0;
		
		Vec2 ourBotPos = Utils.robotPos(ourBot);
		
		for (EnemyBot enemy : enemies.values())
		{
			double distance = Utils.calcDistance(point, enemy.getMovementInfo().pos);
			
			double energyRatio = Math.min((2 * ourBot.getEnergy()) / enemy.getEnergy(), 2.5);
			
			double angleToDest = Utils.calcHeading(ourBotPos, point);
			double angleToEnemy = Utils.calcHeading(ourBotPos, enemy.getMovementInfo().pos);
			
			double directnessOfApproach = Math.abs(Math.cos(angleToDest - angleToEnemy));
			
			totalRisk += energyRatio * (1 + directnessOfApproach) / (distance * distance);
		}
		
		return totalRisk;
	}
	
	public EnemyBot getCurrTarget() { return currTarget; }
	
	private void findBestTarget()
	{
		EnemyBot bestTarget = null;
		double bestRating = 0;
		
		for (EnemyBot enemy : enemies.values())
		{
			if (!enemy.getIsAlive())
				continue;
			
			double rating = rateEnemyBot(enemy);
			
			if (rating > bestRating)
			{
				bestRating = rating;
				bestTarget = enemy;
			}
		}
		
		currTarget = bestTarget;
	}
	
	private double rateEnemyBot(EnemyBot enemy)
	{
		double angleOfApproach = Utils.calcShortestAngleBetween(
				enemy.getMovementInfo().heading,
				Utils.calcHeading(enemy.getMovementInfo().pos, Utils.robotPos(ourBot)));
		
		double speedRatio = 1 - (enemy.getMovementInfo().linearVel.mag() / Constants.MAX_SPEED);
		double distanceRatio = 1 - (enemy.getDistance(ourBot) / Battlefield.getDiagonal());
		double angleRatio = 1 - (angleOfApproach / Math.PI);
		
		return speedRatio + distanceRatio + angleRatio;
	}
	
	private HashMap<String, EnemyBot> enemies = new HashMap<String, EnemyBot>(); // The keys are the names of the robots
	private EnemyBot currTarget = null;
}
