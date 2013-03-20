package lhs;

import java.util.*;
import robocode.*;

class EnemyBullet
{
	public EnemyBullet(Vec2 startPos, Vec2 vel, long lifetime, long currTime, double firePower)
	{
		this.currPos = startPos;
		this.vel = vel;
		this.lifetime = lifetime;
		this.lastTimeUpdated = currTime;
		this.firePower = firePower;
	}
	
	public void update(long currTime)
	{
		long deltaTime = currTime - lastTimeUpdated;
		lastTimeUpdated = currTime;
		
		age += deltaTime;
		
		if (age >= lifetime)
		{
			alive = false;
			return;
		}
		
		currPos = currPos.add(vel.mul(deltaTime));
	}
	
	public boolean isAlive() { return alive; }
	public Vec2 getCurrPos() { return currPos; }
	public double getFirePower() { return firePower; }
	
	private Vec2 currPos = new Vec2();
	private Vec2 vel = new Vec2();
	private long age = 0;
	private long lifetime = 0;
	private long lastTimeUpdated = 0;
	private boolean alive = true;
	private double firePower = 0;
}

class EnemyBulletManager extends Manager
{
	public EnemyBulletManager(AdvancedRobot ourBot, EnemyManager enemyManager)
	{
		super(ourBot);
		this.enemyManager = enemyManager;
	}
	
	// Plots the trajectory of the bullet by using one of our guns on ourself.
	private void addBullet(EnemyBot shooter, double firePower, Gun gun)
	{
		double bulletSpeed = Utils.bulletSpeed(firePower);
		long lifetime = (long)Utils.calcDistance(Utils.robotPos(ourBot), shooter.getMovementInfo().pos)
        				/ (long)bulletSpeed;
		
		// Set up the battle situation so that our robot is the target.
		BattleSituation situation = new BattleSituation(shooter.getMovementInfo(), new MovementInfo(ourBot));
		double firingAngle = gun.computeFiringAngle(situation, firePower);
		
		Vec2 vel = Vec2.fromPolar(bulletSpeed, firingAngle);
		
		EnemyBullet bullet = new EnemyBullet(shooter.getMovementInfo().pos, vel, lifetime, ourBot.getTime(), firePower);
		
		bullets.add(bullet);
	}
	
	public void update()
	{	
		for (EnemyBot enemy : enemyManager.getEnemyList())
		{	
			if (!enemy.getIsFiring())
				continue;
			
			addBullet(enemy, enemy.getLastFirePower(), new HeadOnGun(ourBot));
			
			if (ourBot.getOthers() < 3)
				addBullet(enemy, enemy.getLastFirePower(), new LinearGun(ourBot));
		}
		
		for (EnemyBullet bullet : bullets)
		{
			if (bullet.isAlive())
				bullet.update(ourBot.getTime());
			else
				deadBullets.add(bullet);
		}
		
		for (EnemyBullet deadBullet : deadBullets)
		{
			bullets.remove(deadBullet);
		}
		
		deadBullets.clear();
	}
	
	public void restart()
	{
		deadBullets.clear();
		bullets.clear();
	}
	
	public double calcRisk(Vec2 point)
	{
		double totalRisk = 0;
		
		Vec2 ourBotPos = Utils.robotPos(ourBot);
		
		for (EnemyBullet bullet : bullets)
		{
			double distance = Utils.calcDistance(point, bullet.getCurrPos());
			
			double angleToDest = Utils.calcHeading(ourBotPos, point);
			double angleToBullet = Utils.calcHeading(ourBotPos, bullet.getCurrPos());
			
			double directnessOfApproach = Math.abs(Math.cos(angleToDest - angleToBullet));
			
			totalRisk += (1 + directnessOfApproach) / (distance * distance);
		}
		
		return totalRisk;
	}
	
	private Vector<EnemyBullet> bullets = new Vector<EnemyBullet>();
	private Vector<EnemyBullet> deadBullets = new Vector<EnemyBullet>();
	private EnemyManager enemyManager = null;
}