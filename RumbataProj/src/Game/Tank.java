package Game;

public class Tank extends Character {

	boolean moveForward;
	boolean moveBackward;
	boolean rotateLeft;
	boolean rotateRight;

	private double rotation = 0.0;
	private int reloadTime;
	private int currentReload;
	private int health;
	private double previousX;
	private double previousY;

	public Tank(int x, int y, int maxSpeed, double rotation, int reload50MicroSec, int health) {
		super(x, y, maxSpeed);
		moveForward = false;
		moveBackward = false;
		rotateLeft = false;
		rotateRight = false;
		this.rotation= rotation;
		this.reloadTime = reload50MicroSec;
		this.health = health;

	}

	public double getRotation() {
		return rotation;
	}
	public void tickChange(){ //changes rotation, speed, moves tank, progresses reload

		if (this.moveForward) setSpeed(getSpeed()+2);
		else if (this.moveBackward) setSpeed(getSpeed()-2);
		else{
			if(getSpeed()>0) setSpeed(getSpeed()-1);
			if(getSpeed()<0) setSpeed(getSpeed()+1);
		}

		if (rotateLeft) rotation -= 0.12 -getSpeed()/200d;
		if(rotateRight) rotation += 0.12 -getSpeed()/200d;

		if (getSpeed() > getMaxSpeed()) setSpeed(getMaxSpeed());
		if (getSpeed() < -getMaxSpeed()) setSpeed(-getMaxSpeed());
		previousX = getX();
		previousY = getY();
		setX(getX()+Math.cos(rotation)*getSpeed());
		setY(getY()+Math.sin(rotation)*getSpeed());

		if(currentReload>0) currentReload--;
		else currentReload=0;
	}
	public boolean shoot(){ //shoots
		if(currentReload!=0) return false;
		else{
			currentReload = reloadTime;
			return true;
		}
	}
	public void stop(){ //stops the tank
		this.moveForward=false;
		this.moveBackward=false;
		setSpeed(0);
	}
	public void returnPrevious(){
		setX(previousX);
		setY(previousY);
	}
	public void removeHealth(int damage){
		health=health-damage;
	}
	public int getHealth(){
		return health;
	}
}
