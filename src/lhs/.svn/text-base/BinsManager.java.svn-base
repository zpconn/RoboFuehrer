package lhs;

import robocode.AdvancedRobot;

class BinsManager extends Manager
{
	public BinsManager(AdvancedRobot ourBot)
	{
		super(ourBot);
	}
	
	public void update()
	{
		this.timeSinceVelocityWasZero++;
	}
	public void restart(){}
	
	public void smoothBins(double changeInAngle, BattleSituation situation)
	{	
		int bin = angleToBin(changeInAngle);
		int o = this.getOrbitSegment(situation.currTargetMoveInfo);
		int v = this.getVelocitySegment(situation.currTargetMoveInfo);
		int d = this.getDistanceSegment(situation.currTargetMoveInfo.pos.sub(situation.ourBotMoveInfo.pos).mag());
		int p = this.getPerpendicularitySegment(situation.ourBotMoveInfo.pos, 
												situation.currTargetMoveInfo.pos, 
												situation.currTargetMoveInfo.heading);
		int e = this.getNumOfEnemiesSegment(ourBot.getOthers());
		
		for(int gf = 0; gf < NUM_OF_GFS; gf++)
		{
			int actualGuessFactor = gf > NUM_OF_GFS/2 ? fixBin(-gf) : fixBin(gf);
			int dist = Math.abs(actualGuessFactor - bin);
			bins[gf][o][v][d][p][e] += Math.pow(.5, dist);
			bins[gf][o][v][d][p][e] *= .98;
		}
	}
	
	public double getPredictedAngleChange(EnemyBot target)
	{
		int highestBin = highestBin(target);
		return highestBin * Math.PI / NUM_OF_GFS;
	}
	
	private int highestBin(EnemyBot target)
	{
		int o = this.getOrbitSegment(target.getMovementInfo());
		int v = this.getVelocitySegment(target.getMovementInfo());
		int d = this.getDistanceSegment(target.getDistance(ourBot));
		int p = this.getPerpendicularitySegment(Utils.robotPos(ourBot), 
										target.getMovementInfo().pos, 
										target.getMovementInfo().heading);
		int e = this.getNumOfEnemiesSegment(ourBot.getOthers());
		System.out.println(d);
		int highest = 0;
		for(int gf = 0; gf < NUM_OF_GFS; gf++)
		{
			if(bins[gf][o][v][d][p][e] > bins[highest][o][v][d][p][e])
				highest = gf;
		}
		return fixBin(highest > NUM_OF_GFS/2 ? fixBin(-highest) : fixBin(highest));
	}
	
	private int fixBin(int bin)
	{
		int limit = NUM_OF_GFS/2;
		int s = Math.abs(bin) % NUM_OF_GFS;
		int d = Math.abs(limit - s);
		return (int)Math.signum(bin)*(limit - d);
	}
	
	private int angleToBin(double changeInAngle)
	{
		return fixBin((int)Math.round((NUM_OF_GFS * changeInAngle/Math.PI)));
	}
	
	private int getOrbitSegment(MovementInfo movementInfo)
	{
		return movementInfo.orbitDirection + 1;
	}
	
	private int getVelocitySegment(MovementInfo movementInfo)
	{
		this.timeSinceVelocityWasZero = movementInfo.speed == 8 ? 0 : this.timeSinceVelocityWasZero;
		return this.timeSinceVelocityWasZero >= MAX_HISTORY ? MAX_HISTORY-1 : this.timeSinceVelocityWasZero;
	}
	
	private int getDistanceSegment(double distanceFromBot)
	{
		return (int)Math.round(((NUM_OF_DIST_BINS-1) * distanceFromBot/Battlefield.getDiagonal()));
	}
	
	private int getPerpendicularitySegment(Vec2 ourPos, Vec2 enemyPos, double enemyHeading)
	{
		Vec2 disp = enemyPos.sub(ourPos).getNormalized();
		Vec2 heading = Vec2.fromPolar(1, enemyHeading);
		return (int)Math.round(disp.dot(heading)) + 1;	
	}
	
	private int getNumOfEnemiesSegment(int numOfEnemies)
	{
		int n = numOfEnemies - 1;
		if(n < 0 || n >= NUM_OF_ENEMIES_BINS)
			return NUM_OF_ENEMIES_BINS - 1;
		return n;
	}
	
	private static int NUM_OF_GFS = 58;
	private static int NUM_OF_DIST_BINS = 5;
	private static int MAX_HISTORY = 3;
	private static int NUM_OF_PERP_BINS = 3;
	private static int NUM_OF_ENEMIES_BINS = 2;
	private int timeSinceVelocityWasZero = 0;
	private double[][][][][][] bins = new double[NUM_OF_GFS][3][MAX_HISTORY][NUM_OF_DIST_BINS][NUM_OF_PERP_BINS][NUM_OF_ENEMIES_BINS];
}
