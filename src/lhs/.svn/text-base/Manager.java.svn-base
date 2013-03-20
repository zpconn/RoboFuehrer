package lhs;

import robocode.*;

// This abstract class defines the common interface to which all managers must conform. A manager
// is responsible for arbitrating a specific aspect of a robot's functionality, such as movement
// or enemy surveillance.
public abstract class Manager
{	
	public Manager(AdvancedRobot ourBot)
	{
		this.ourBot = ourBot;
	}
	
	// Called once per turn to perform necessary updates.
	public abstract void update();
	
	// Provides initialization on the second and subsequent rounds of battle.
	public abstract void restart();
	
	public AdvancedRobot ourBot;
}