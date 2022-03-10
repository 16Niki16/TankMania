package Game;

public class Shot extends Character {
	private double rotation;
	public Shot(double x, double y, int s,double rotation) {
		super(x,y,s);
		setSpeed(getMaxSpeed());
		this.rotation = rotation;
	}
	public void tickChange(){ //moves the bullet
		setX(getX() + Math.cos(rotation)*getSpeed());
		setY(getY()+Math.sin(rotation)*getSpeed());
	}
}