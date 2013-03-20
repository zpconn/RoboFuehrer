package lhs;

import robocode.*;

// This manager oversees basic movement of an AdvancedRobot. It provides methods to accomplish
// simple movement tasks, such as moving from one point to another.
public class MovementManager extends Manager
{	
	public MovementManager(AdvancedRobot ourBot)
	{
		super(ourBot);
	}
	
	public void restart()
	{
		direction = Constants.FORWARD;
	}
	
	public void update() { }
	
	public Vec2 orbitAbout(Vec2 point, double orbitRadius, int orbitDirection, 
			               double angularVelocity)
	{		
		Vec2 orbitVec = getPos().sub(point);
		
		orbitVec = orbitVec.rotate(orbitDirection * angularVelocity);
		orbitVec = orbitVec.changeMag(orbitRadius);
		
		Vec2 targetPoint = point.add(orbitVec);
		
		moveTo(targetPoint);
		return targetPoint;
	}
	
	public void orbitAboutWithWallSmoothing(Vec2 point, double orbitRadius, int orbitDirection,
			                                double angularVelocity)
	{
		Vec2 orbitVec = getPos().sub(point);
		
		orbitVec = orbitVec.rotate(orbitDirection * angularVelocity);
		orbitVec = orbitVec.changeMag(orbitRadius);
		
		Vec2 targetPoint = wallSmooth(point.add(orbitVec), orbitDirection);
		
		moveTo(targetPoint);
	}
	
	public Vec2 wallSmooth(Vec2 dest, int orbitDirection)
	{
		Vec2 target = dest;
		
		while (!Battlefield.containsPoint(target))
		{
			Vec2 disp = target.sub(getPos());
			disp = disp.rotate(Math.PI / 50 * orbitDirection);
			target = getPos().add(disp);
		}
		
		return target;
	}
	
	// Tells the robot to move to the given destination, specified in absolute coordinates.
	// The robot will rotate in the most efficient manner, that is in the direction that minimizes
	// the rotation time.
	public void moveTo(Vec2 dest)
	{		
		setSpeed(Constants.MAX_SPEED);
		turnTo(Utils.calcHeading(getPos(), dest));
		setMove(Utils.calcDistance(getPos(), dest));
	}
	
	// Changes the heading of the robot to that specified. If necessary, the direction of movement,
	// forward or reverse, may be modified to minimize the rotation time.
	public void turnTo(double heading)
	{
		double turn = shortestTurn(Utils.clampAngle(heading));
		
		if (Math.abs(turn) > Math.PI/2)
		{
			changeDirection();
			turn += (turn > Math.PI/2 ? -Math.PI : Math.PI);
		}
			
		setTurn(turn);
	}
	
	// Given a desired heading, this returns the shortest turn to rotate the robot to 
	// the provided heading without reversing the direction of movement.
	public double shortestTurn(double heading)
	{
		return Utils.calcShortestAngleBetween(getRealHeading(), heading);
	}
	
	// Changes the movement direction from forward to reverse or vice versa.
	public void changeDirection()
	{
		if (direction * ourBot.getVelocity() >= 0.0)
		{
			direction *= -1;
			setMove(getMove());
		}
	}

	// Calculates the heading at which the robot is moving, taking into account the
	// sign of its velocity.
	public double getRealHeading()
	{
		if (ourBot.getVelocity() < 0.0)
		{
			direction = Constants.REVERSE;
			return (ourBot.getHeadingRadians() + Math.PI) % (2*Math.PI);
		}
		
		direction = Constants.FORWARD;
		return ourBot.getHeadingRadians();
	}
	
	// Tells the robot to turn a certain number of radians.
	public void setTurn(double angle)
	{
		ourBot.setTurnRightRadians(angle);
	}
	
	// Gets the amount of turn remaining in radians.
	public double getTurn()
	{
		return ourBot.getTurnRemainingRadians();
	}
	
	//	 Tells the robot to move a certain distance in the set direction.
	public void setMove(double distance)
	{
		ourBot.setAhead(distance * direction);
	}
	
	// Returns the amout of movement remaining.
	public double getMove()
	{
		return Math.abs(ourBot.getDistanceRemaining());
	}
	
	// Sets the maximum speed of the robot
	public void setSpeed(double speed)
	{
		if (speed == Constants.MAX_SPEED && ourBot.getTime() % 2 == 0)
			speed = Constants.ALMOST_MAX_SPEED;
		
		ourBot.setMaxVelocity(speed);
	}
	
	// Returns the absolute value of the speed of this robot.
	public double getSpeed()
	{
		return Math.abs(ourBot.getVelocity());
	}
	
	// Returns the position of the robot as a vector.
	public Vec2 getPos()
	{
		return new Vec2(ourBot.getX(), ourBot.getY());
	}
	
	private int direction = Constants.FORWARD;
}
