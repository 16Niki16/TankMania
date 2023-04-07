package Game;

public class Shot extends Character {
	private double rotation;
	private double previousX;
	private double previousY;
	public Shot(double x, double y, int s,double rotation) {
		super(x,y,s);
		setSpeed(getMaxSpeed());
		this.rotation = rotation;
	}
	public double getPreviousX() {return previousX;}
	public double getPreviousY() {return previousY;}
	
	public void tickChange(){ //moves the bullet
		previousX = getX();
		previousY = getY();
		
		setX(getX() + Math.cos(rotation)*getSpeed());
		setY(getY() + Math.sin(rotation)*getSpeed());
	}
}