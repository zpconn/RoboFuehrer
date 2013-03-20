package lhs;

import robocode.*;
import java.awt.geom.*;

public class Battlefield
{	
	public static void initialize(AdvancedRobot ar)
	{
		ourBot = ar;
		
		width = ourBot.getBattleFieldWidth();
		height = ourBot.getBattleFieldHeight();
		center = new Vec2(width / 2, height / 2);
		diagonal = Math.sqrt(width*width + height*height);
	}
	
	public static double getWidth() { return width; }
	public static double getHeight() { return height; }
	public static Vec2 getCenter() { return center; }
	public static double getDiagonal() { return diagonal; }
	
	public static boolean containsPoint(Vec2 point)
	{
		Rectangle2D.Double fieldRect = new Rectangle2D.Double(
				Constants.WALL_AVOID_DISTANCE, Constants.WALL_AVOID_DISTANCE,
				width - 2 * Constants.WALL_AVOID_DISTANCE,
				height - 2 * Constants.WALL_AVOID_DISTANCE);
		
		return fieldRect.contains(point.x, point.y);
	}
	
	public static double calcWallRisk(Vec2 point)
	{
		return Math.min(
				Math.min(point.x, width - point.x),
				Math.min(point.y, height - point.y));
	}
	
	public static double calcCenterRisk(Vec2 point)
	{
		return 1 / (Utils.calcDistance(point, center));
	}
	
	private static double width = 0;
	private static double height = 0;
	private static Vec2 center = new Vec2();
	private static double diagonal = 0;
	private static AdvancedRobot ourBot = null;
}