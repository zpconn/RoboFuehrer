package lhs;

import robocode.*;
import java.util.*;

// Stores performance statistics about a Gun.
class GunProfile
{
	public GunProfile(Gun gun)
	{
		this.gun = gun;
	}
	
	public Gun gun = null;
	public double rating = 0;
}

// This manager keeps a list of Guns and collects performance statistics on each to determine which
// is most effective against each individual enemy. For collecting statistics it uses the wave system.
class GunStatsManager extends Manager
{
	public GunStatsManager(AdvancedRobot ourBot, EnemyBot target)
	{
		super(ourBot);
		this.target = target;
	}
	
	public void addGun(Gun gun)
	{
		gunProfiles.add(new GunProfile(gun));
	}
	
	public void update()
	{
		firePower = Firepower.compute(ourBot, target);
		
		for (GunProfile gunProfile : gunProfiles)
			gunProfile.gun.update();
		
		Wave newWave = new Wave(getCurrBattleSituation(), ourBot.getTime(), firePower);
		waveManager.addWave(newWave);
		
		waveManager.propagateWaves(ourBot.getTime(), new WaveUpdater_CheckCollision(this));
		
		chooseBestGun();
	}
	
	public void onScannedRobot(ScannedRobotEvent evt)
	{
		for (GunProfile gunProfile : gunProfiles)
			gunProfile.gun.onScannedRobot(evt);
	}
	
	public void restart()
	{
		
	}
	
	public Gun getBestGun()
	{
		if (bestGunProfile == null)
			return null;
		
		return bestGunProfile.gun;
	}
	
	private BattleSituation getCurrBattleSituation()
	{
		return new BattleSituation(new MovementInfo(ourBot), target.getMovementInfo().clone());
	}
	
	private void chooseBestGun()
	{
		if (gunProfiles.size() == 0)
			return;
		
		for (GunProfile gunProfile : gunProfiles)
		{
			if (bestGunProfile == null)
			{
				bestGunProfile = gunProfile;
				continue;
			}
			
			if (gunProfile.rating > bestGunProfile.rating)
				bestGunProfile = gunProfile;
		}
	}
	
	public void waveUpdater(Wave wave)
	{
		if (wave.intersects(target.getMovementInfo().pos, ERROR_TOLERANCE))
		{			
			waveManager.killWave(wave);
			
			double idealAngle = Utils.calcHeading(wave.initialSituation.ourBotMoveInfo.pos, target.getMovementInfo().pos);
			
			for (GunProfile gunProfile : gunProfiles)
			{
				double error = Math.abs(Utils.calcShortestAngleBetween(idealAngle, gunProfile.gun.computeFiringAngle(wave.initialSituation, firePower)));
				
				gunProfile.rating += 2 * Math.PI - error;
			}
		}
	}
	
	class WaveUpdater_CheckCollision implements WaveUpdater
	{
		public WaveUpdater_CheckCollision(GunStatsManager gunManager)
		{
			this.gunManager = gunManager;
		}
		
		public void update(Wave wave)
		{
			if (gunManager == null)
				return;
			
			gunManager.waveUpdater(wave);
		}
		
		private GunStatsManager gunManager = null;
	}
	
	private EnemyBot target = null;
	private Vector<GunProfile> gunProfiles = new Vector<GunProfile>();
	private WaveManager waveManager = new WaveManager();
	private GunProfile bestGunProfile = null;
	private double firePower = 2;
	
	private final double ERROR_TOLERANCE = 5;
}