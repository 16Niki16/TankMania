package Game;

public class Character {
	private double x;
	private double y;
	private int maxSpeed;
	private int speed=0;

	public Character(double x, double y, int maxSpeed) {
		this.x = x;
		this.y = y;
		this.maxSpeed = maxSpeed;
	}

	public double getX() {return x;}
	public double getY() {return y;}
	public int getMaxSpeed() {return maxSpeed;}
	public int getSpeed() {return speed;}

	public void setX(double x) {this.x = x;}
	public void setY(double y) {this.y = y;}
	public void setMaxSpeed(int maxSpeed) {this.maxSpeed = maxSpeed;}
	public void setSpeed(int speed) {this.speed = speed;}
}
