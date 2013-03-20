package lhs;

import robocode.*;

// This class stores information regarding the movement of a robot, such as its position, velocity,
// heading, etc.
class MovementInfo
{	
	public MovementInfo() { }
	
	public MovementInfo(Vec2 pos, Vec2 linearVel, double angularVel, double heading, Vec2 prevVel, double speed)
	{
		this.pos = pos;
		this.linearVel = linearVel;
		this.angularVel = angularVel;
		this.heading = heading;
		this.prevVel = prevVel;
		this.speed = speed;
	}
	
	public MovementInfo(AdvancedRobot bot)
	{
		pos = Utils.robotPos(bot);
		linearVel = Vec2.fromPolar(bot.getVelocity(), bot.getHeadingRadians());
		heading = bot.getHeadingRadians();
	}
	
	public MovementInfo clone()
	{
		MovementInfo result =  new MovementInfo(pos, linearVel, angularVel, heading, prevVel, speed);
		result.orbitDirection = this.orbitDirection;
		return result;
	}
	
	public Vec2 pos = new Vec2();
	public Vec2 linearVel = new Vec2();
	public double angularVel = 0;
	public double heading = 0;
	public Vec2 prevVel = new Vec2();
	public int orbitDirection = 1;
	public double speed = 1;
}