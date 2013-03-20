package lhs;
import java.util.*;

class Wave
{
	public Wave(BattleSituation currSituation, long time, double firePower)
	{
		initialSituation = currSituation;
		timeCreated = time;
		lastTimeUpdated = time;
		this.firePower = firePower;
	}
	
	public void update(long time)
	{
		radius += Utils.bulletSpeed(firePower) * (time - lastTimeUpdated);
		lastTimeUpdated = time;
		lifetime = time - timeCreated;
	}
	
	public boolean intersects(Vec2 point, double errorTolerance)
	{
		Vec2 disp = point.sub(initialSituation.ourBotMoveInfo.pos);
		
		if (Math.abs(radius - disp.mag()) < errorTolerance)
			return true;
		
		return false;
	}
	
	public BattleSituation initialSituation = null;
	public double radius = 0;
	public long timeCreated = 0;
	public long lastTimeUpdated = 0;
	public long lifetime = 0;
	public double firePower = 2;
}

interface WaveUpdater
{
	public void update(Wave wave);
}

class WaveManager
{
	public void addWave(Wave newWave)
	{
		waves.add(newWave);
	}
	
	public void killWave(Wave wave)
	{
		deadWaves.add(wave);
	}
	
	public void propagateWaves(long deltaTime, WaveUpdater updater)
	{
		for (Wave deadWave : deadWaves)
		{
			waves.remove(deadWave);
		}
		
		deadWaves.clear();
		
		for (Wave wave : waves)
		{
			wave.update(deltaTime);
			updater.update(wave);
		}
	}
	
	private Vector<Wave> waves = new Vector<Wave>();
	private Vector<Wave> deadWaves = new Vector<Wave>();
}