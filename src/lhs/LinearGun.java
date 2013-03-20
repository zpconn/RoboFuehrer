package lhs;

import robocode.*;

// This gun tries to predict where the enemy is moving by assuming he moves along a straight line
// with constant velocity.
class LinearGun extends Gun
{
	public LinearGun(AdvancedRobot ourBot)
	{
		super(ourBot);
	}
	
	public String name()
	{
		return "LINEAR";
	}
	
	public double computeFiringAngle(BattleSituation situation, double firePower)
	{
		MovementPredictor predictor = new LinearMovementPredictor();
		
		long impactTime = predictor.resolveImpactTime(ourBot.getTime(), firePower, 40,
				situation.ourBotMoveInfo, situation.currTargetMoveInfo);
		Vec2 predictedPos = predictor.predictPos(impactTime - ourBot.getTime(), 
				situation.currTargetMoveInfo);
		
		return Utils.calcHeading(situation.ourBotMoveInfo.pos, predictedPos);
	}
}

// This gun tries to predict where the enemy is moving by assuming he moves along a circular arc
// with constant velocity.
class CircularGun extends Gun
{
	public CircularGun(AdvancedRobot ourBot)
	{
		super(ourBot);
	}
	
	public String name()
	{
		return "CIRCULAR";
	}
	
	public double computeFiringAngle(BattleSituation situation, double firePower)
	{
		MovementPredictor predictor = new CircularMovementPredictor();
		
		long impactTime = predictor.resolveImpactTime(ourBot.getTime(), firePower, 40,
				situation.ourBotMoveInfo, situation.currTargetMoveInfo);
		Vec2 predictedPos = predictor.predictPos(impactTime - ourBot.getTime(), 
				situation.currTargetMoveInfo);
		
		return Utils.calcHeading(situation.ourBotMoveInfo.pos, predictedPos);
	}
}

