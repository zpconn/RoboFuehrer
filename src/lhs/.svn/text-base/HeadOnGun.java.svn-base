package lhs;

import robocode.*;

// This gun just fires directly at the enemy at his current position without trying to predict
// his motion.
class HeadOnGun extends Gun
{
	public HeadOnGun(AdvancedRobot ourBot)
	{
		super(ourBot);
	}
	
	public String name()
	{
		return "HEADON";
	}
	
	public double computeFiringAngle(BattleSituation situation, double firePower)
	{		
		return Utils.calcHeading(situation.ourBotMoveInfo.pos, situation.currTargetMoveInfo.pos);
	}
}
