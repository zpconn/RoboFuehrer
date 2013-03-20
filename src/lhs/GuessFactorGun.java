package lhs;

import robocode.AdvancedRobot;

public class GuessFactorGun extends Gun
{

	public GuessFactorGun(AdvancedRobot ourBot, EnemyBot target) 
	{
		super(ourBot);
		
		this.target = target;
	}
	
	public String name() 
	{
		return "GFT";
	}
	
	public double computeFiringAngle(BattleSituation situation, double firePower)
	{	
		return Utils.calcHeading(situation.ourBotMoveInfo.pos, target.getMovementInfo().pos) +  this.binsManager.getPredictedAngleChange(target);	
	}
	
	public void update()
	{
		Wave newWave = new Wave(getCurrBattleSituation(), ourBot.getTime(), Firepower.compute(ourBot, target));
		waveManager.addWave(newWave);
		waveManager.propagateWaves(ourBot.getTime(), new WaveUpdater_CheckCollision(this));
		binsManager.update();
	}
	
	public void waveUpdater(Wave wave)
	{				
		if (wave.intersects(target.getMovementInfo().pos, ERROR_TOLERANCE))
		{	
			waveManager.killWave(wave);
			
			Vec2 ourBotPos = wave.initialSituation.ourBotMoveInfo.pos;
			Vec2 targetPos = target.getMovementInfo().pos;
			Vec2 oldTargetPos = wave.initialSituation.currTargetMoveInfo.pos;
			double changeInAngle = Utils.getAngleChange(ourBotPos, oldTargetPos, targetPos);
			this.binsManager.smoothBins(changeInAngle, wave.initialSituation);
		}
	}
	
	class WaveUpdater_CheckCollision implements WaveUpdater
	{
		public WaveUpdater_CheckCollision(GuessFactorGun gun)
		{
			this.gun = gun;
		}
		
		public void update(Wave wave)
		{
			if (gun == null)
				return;
			
			gun.waveUpdater(wave);
		}
		
		private GuessFactorGun gun = null;
	}
	
	private BattleSituation getCurrBattleSituation()
	{
		return new BattleSituation(new MovementInfo(ourBot), target.getMovementInfo().clone());
	}
	
	private EnemyBot target = null;
	private WaveManager waveManager = new WaveManager();
	private BinsManager binsManager = new BinsManager(ourBot);
	private double ERROR_TOLERANCE = 5;
}
