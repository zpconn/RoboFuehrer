package lhs;

import robocode.*;

// This class is used to store statistics about a specific enemy robot.
class EnemyBot
{	
	public EnemyBot(ScannedRobotEvent evt, AdvancedRobot ourBot)
	{
		update(evt, ourBot);
		
		gunStatsManager = new GunStatsManager(ourBot, this);
		gunStatsManager.addGun(new LinearGun(ourBot));
		gunStatsManager.addGun(new CircularGun(ourBot));
		gunStatsManager.addGun(new GuessFactorGun(ourBot, this));
	}
	
	public void update()
	{
		gunStatsManager.update();
	}
	
	public void update(ScannedRobotEvent evt, AdvancedRobot ourBot)
	{
		name = evt.getName();
		
		Vec2 relPosition = Vec2.fromPolar(evt.getDistance(), 
				evt.getBearingRadians() + ourBot.getHeadingRadians());
		movementInfo.pos = Utils.robotPos(ourBot).add(relPosition);
		
		movementInfo.prevVel = movementInfo.linearVel;
		movementInfo.linearVel = Vec2.fromPolar(evt.getVelocity(), evt.getHeadingRadians());
		
		movementInfo.angularVel = 
			Utils.clampAngle(evt.getHeadingRadians() - movementInfo.heading) 
				/ (ourBot.getTime() - lastTimeUpdated);
		
		if (evt.getVelocity() != 0)
		{		
			movementInfo.orbitDirection = Utils.sign(evt.getVelocity() * Math.sin(evt.getHeadingRadians() - getAbsBearing(ourBot)));
		}
		
		movementInfo.speed = evt.getVelocity();
		bearing = evt.getBearingRadians();
		
		double dE = energy - evt.getEnergy();
		
		if (dE > 0 && dE <= Constants.MAX_FIRE_POWER)
			isFiring = true;
		else
			isFiring = false;
		
		energy = evt.getEnergy();
		lastFirePower = dE;
		movementInfo.heading = evt.getHeadingRadians();
		lastTimeUpdated = ourBot.getTime();
	}
	
	public double getDistance(AdvancedRobot ourBot)
	{
		return Utils.robotPos(ourBot).sub(movementInfo.pos).mag();
	}
	
	public double getAbsBearing(AdvancedRobot ourBot)
	{
		Vec2 disp = movementInfo.pos.sub(Utils.robotPos(ourBot));
		return disp.angle();
	}
	
	public double getRelBearing(AdvancedRobot ourBot)
	{
		return Utils.clampAngle(getAbsBearing(ourBot) - ourBot.getHeadingRadians());
	}
	
	public boolean equals(Object obj)
	{
		boolean equal = false;
		
		if (obj instanceof EnemyBot)
		{
			if (((EnemyBot)obj).name.equals(name))
				equal = true;
		}
		
		return equal;
	}
	
	// Reinitializes the statistics for the second and subsequent rounds of battle.
	public void reinitialize()
	{
		energy = 100;
		movementInfo = new MovementInfo();
		isFiring = false;
		lastTimeUpdated = 0;
		isAlive = true;
	}
	
	public void kill() { isAlive = false; }
	
	public boolean doesMusashiTrickWork = true;
	
	public String getName() { return name; }
	public long getLastTimeUpdated() { return lastTimeUpdated; }
	public double getEnergy() { return energy; }
	public double getLastFirePower() { return lastFirePower; }
	public boolean getIsFiring() { return isFiring; }
	public MovementInfo getMovementInfo() { return movementInfo; }
	public boolean getIsAlive() { return isAlive; }
	public GunStatsManager getGunStatsManager() { return gunStatsManager; }
	public double getBearing() { return bearing; }
	
	private String name = null;
	private long lastTimeUpdated = 0;
	private double energy = 0;
	private double lastFirePower = 1.5;
	private boolean isFiring = false;
	private boolean isAlive = true;
	private MovementInfo movementInfo = new MovementInfo();
	private GunStatsManager gunStatsManager = null;
	private double bearing = 0;
}