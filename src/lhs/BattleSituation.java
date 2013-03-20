package lhs;

// A BattleSituation is a snapshot of the arena at a particular moment in time. It includes movement
// information for our robot at that moment in time as well as all collected information about
// the current target at that same moment in time.
class BattleSituation
{	
	public BattleSituation() { }
	
	public BattleSituation(MovementInfo ourBotMoveInfo, MovementInfo currTargetMoveInfo)
	{
		this.ourBotMoveInfo = ourBotMoveInfo;
		this.currTargetMoveInfo = currTargetMoveInfo;
	}
	
	public MovementInfo ourBotMoveInfo = new MovementInfo();
	public MovementInfo currTargetMoveInfo = new MovementInfo();
}