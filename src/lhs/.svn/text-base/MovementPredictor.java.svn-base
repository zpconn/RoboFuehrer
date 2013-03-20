package lhs;

class MovementPredictor
{
	public long resolveImpactTime(long currTime, double firePower, int numIterations,
			MovementInfo ourBotMoveInfo, MovementInfo targetMoveInfo)
	{
		long newEstimate = currTime;
		Vec2 targetPos = targetMoveInfo.pos;
		Vec2 ourBotPos = ourBotMoveInfo.pos;
		
		for (int i = 0; i < numIterations; ++i)
		{
			newEstimate = currTime + (long)targetPos.sub(ourBotPos).mag() 
						/ (long)Utils.bulletSpeed(firePower);
			targetPos = predictPos(newEstimate - currTime, targetMoveInfo);
		}
		
		return (long)newEstimate;
	}
	
	public Vec2 predictPos(long deltaTime, MovementInfo moveInfo)
	{
		return moveInfo.pos;
	}
}

// This MovementPredictor assumes the target robot moves along a straight line with constant velocity.
class LinearMovementPredictor extends MovementPredictor
{
	public Vec2 predictPos(long deltaTime, MovementInfo moveInfo)
	{
		return moveInfo.pos.add(moveInfo.linearVel.mul(deltaTime));
	}
}

// This MovementPredictor assumes the target robot moves along a circular arc with constant velocity.
class CircularMovementPredictor extends MovementPredictor
{
	public Vec2 predictPos(long deltaTime, MovementInfo moveInfo)
	{
		double radius = moveInfo.linearVel.mag() / moveInfo.angularVel;
		double totalHeadingChange = moveInfo.angularVel * deltaTime;
		
		Vec2 circularVel = new Vec2(Math.cos(moveInfo.heading) * radius - Math.cos(moveInfo.heading + totalHeadingChange) * radius,
				Math.sin(moveInfo.heading + totalHeadingChange) * radius - Math.sin(moveInfo.heading) * radius);
		
		return moveInfo.pos.add(circularVel);
	}
}