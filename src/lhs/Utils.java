package lhs;

import robocode.*;

class Utils
{
	public static double calcHeading(Vec2 start, Vec2 end)
	{
		Vec2 disp = end.sub(start);
		return disp.angle();
	}
	
	public static double calcDistance(Vec2 start, Vec2 end)
	{
		Vec2 disp = end.sub(start);
		return disp.mag();
	}
	
	public static int sign(double num)
	{
		return num >= 0 ? 1 : -1;
	}
	
	public static Vec2 robotPos(AdvancedRobot bot)
	{
		return new Vec2(bot.getX(), bot.getY());
	}
	
	// Given an angle in radians that may not be in the range [0, 2Pi), this returns
	// a coterminal angle within said range.
	public static double clampAngle(double angle)
	{
		double clampedAngle = angle;
		
		if (angle >= 2*Math.PI)
			clampedAngle = angle - 2*Math.PI;
		else if (angle < 0)
			clampedAngle = 2*Math.PI + angle;
		
		return clampedAngle % (2*Math.PI);
	}
	
	public static double clampNumToRange(double num, double min, double max)
	{		
		return Math.max(min, Math.min(max, num));
	}
	
	// Given two angles in radians, this returns the shortest angle from the first to the second.
	// The sign of the result indicates whether the angle goes clockwise or counter-clockwise.
	public static double calcShortestAngleBetween(double angle1, double angle2)
	{
		double shortestAngle = (angle2 - angle1) % (2*Math.PI);
		
		if (shortestAngle < -Math.PI) 
			shortestAngle = shortestAngle + 2*Math.PI;
		else if (shortestAngle > Math.PI) 
			shortestAngle = shortestAngle - 2*Math.PI;
		
		return shortestAngle;
	}
	
	public static double bulletSpeed(double bulletPower)
	{
		return 20 - 3 * bulletPower;
	}
	
	public static double getAngleChange(Vec2 myPos, Vec2 a, Vec2 b)
	{
		Vec2 disp_a = a.sub(myPos);
		Vec2 disp_b = b.sub(myPos);
		return disp_a.a_angle() - disp_b.a_angle();
	}
}