package lhs;

import robocode.AdvancedRobot;

public class Firepower
{
	public static double compute(AdvancedRobot ourBot, EnemyBot target)
	{	
		double power;
		
		if (ourBot.getEnergy() <= 0.1)
			return 0;
		else if (ourBot.getEnergy() < 1)
			return Constants.MIN_FIRE_POWER;
		
		// Don't use more fire power than is necessary to kill off the enemy
		power = Math.min(target.getEnergy() / Constants.HIT_DAMAGE,
				             Constants.MAX_FIRE_POWER);
		
		// We don't want to kill our robot.
		power = Math.min(ourBot.getEnergy() / Constants.HIT_DAMAGE, power);
		
		// A target approaching us more directly is easier to hit
		double angleOfApproach = Utils.calcShortestAngleBetween(
				target.getMovementInfo().heading,
				Utils.calcHeading(target.getMovementInfo().pos, Utils.robotPos(ourBot)));
		
		if (target.getDistance(ourBot) > Constants.LONG_DISTANCE)
		{
			double speedRatio = 1 - (target.getMovementInfo().linearVel.mag() / Constants.MAX_SPEED);
			double distanceRatio = 1 - (target.getDistance(ourBot) / Battlefield.getDiagonal());
			double angleRatio = 1 - (angleOfApproach / Math.PI);
			
			power *= Math.max(speedRatio, Math.max(distanceRatio, angleRatio));
		}
		
		power = Utils.clampNumToRange(power,Constants.MIN_FIRE_POWER, Constants.MAX_FIRE_POWER);
		return power;
	}
}