package lhs;

import robocode.*;

// The Gun class encapsulates a targeting algorithm and provides methods for calculating the 
// firing angle given the state of the battle. Specific targeting algorithms are implemented in classes
// derived from Gun.
class Gun
{
	public Gun(AdvancedRobot ourBot)
	{
		this.ourBot = ourBot;
	}
	
	public void update() { }
	public void onScannedRobot(robocode.ScannedRobotEvent evt) { }
	public double computeFiringAngle(BattleSituation situation, double firePower) { return 0; }
	public String name() { return ""; }
	
	public void fireAtTarget(BattleSituation situation, double firePower)
	{
		fireAtAngle(computeFiringAngle(situation, firePower), firePower);
	}
	
	public void fireAtAngle(double angle, double firePower)
	{
		setTurnGunTo(angle);
		ourBot.setFire(firePower);
	}
	
	// Rotates the gun to the specified absolute bearing in the direction that minimizes
	// the rotation time.
	public void setTurnGunTo(double angle)
	{
		double relAngle = Utils.clampAngle(angle - ourBot.getGunHeadingRadians());
		
		if (relAngle <= Math.PI)
			ourBot.setTurnGunRightRadians(relAngle);
		else
			ourBot.setTurnGunLeftRadians(2*Math.PI - relAngle);
	}
	
	protected AdvancedRobot ourBot = null;
}
