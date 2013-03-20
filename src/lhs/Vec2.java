package lhs;

// This class represents a two-dimensional vector. It is consistent with Robocode's convention
// to represent angles in compass orientation.
class Vec2
{
	public double x = 0;
	public double y = 0;
	
	public Vec2()
	{
		this(0, 0);
	}
	
	public Vec2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vec2 add(Vec2 rhs)
	{
		return new Vec2(x + rhs.x, y + rhs.y);
	}
	
	public Vec2 sub(Vec2 rhs)
	{
		return new Vec2(x - rhs.x, y - rhs.y);
	}
	
	public Vec2 mul(double scalar)
	{
		return new Vec2(x*scalar, y*scalar);
	}
	
	public Vec2 div(double scalar)
	{
		return new Vec2(x/scalar, y/scalar);
	}
	
	public double mag()
	{
		return Math.sqrt(x*x + y*y);
	}
	
	public double dot(Vec2 rhs)
	{
		return x*rhs.x + y*rhs.y;
	}
	
	// Calculates the angle of this vector in compass orientation.
	public double angle()
	{
		return Math.atan2(x, y);
	}
	
	public double a_angle()
	{
		double theta = Math.abs(Math.atan(y/x));
		if(x < 0 && y > 0)
			theta = Math.PI - theta;
		else if(x < 0 && y < 0)
			theta = Math.PI + theta;
		else if(x > 0 && y < 0)
			theta = 2*Math.PI - theta;
		return theta;
	}
	
	public double getAngleBetween(Vec2 other)
	{
		return Math.acos(this.dot(other)/(this.mag()*other.mag()));
	}
	
	public Vec2 getNormalized()
	{
		return this.div(this.mag());
	}
	
	public Vec2 rotate(double angle)
	{
		return new Vec2(x * Math.cos(angle) - y*Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
	}
	
	public Vec2 changeMag(double newMag)
	{
		return this.mul(newMag / mag());
	}
	
	// Constructs the vector from its polar representation. The angle is assumed to be
	// in compass orientation.
	public static Vec2 fromPolar(double distance, double angle)
	{
		return new Vec2(distance * Math.sin(angle), distance * Math.cos(angle));
	}
	
	public boolean equals(Object other)
	{
		Vec2 otherVec = (Vec2)other;
		return (otherVec.x == x) && (otherVec.y == y);
	}
}