import lejos.nxt.*;
import java.util.ArrayList;

/**
 * Interface for all of the motorized peripherals of the robot. Contains methods to read and
 * maniuplate the left and right motor of the robot.
 * 
 */
public class Movement {
	
	// NXT motors and their corresponding ports
	
	private NXTRegulatedMotor left_motor;
	private MotorPort left_motor_port;
	
	private NXTRegulatedMotor right_motor;
	private MotorPort right_motor_port;
	
	// The status of the left and right motors. Forward = 1, backward = -1
	private int LEFT_ROTATION = 1;
	private int RIGHT_ROTATION = 1;

    // Constants to allow for calibration of distance and heading calculations.
    private double DIST_MULTIPLIER = 60e-8;
    private double HEADING_MULTIPLIER = 800e-8;
    // The distance traveled and current heading
    private double dist_traveled;
    private double heading;

    // Last time setSpeed was called
    private long lastSetSpeed = 0;
	
	// starting motor speed
	private static final int START_SPEED = 0;
	
	// max motor speed
	private static final int MAX_SPEED = 128;
	
	// forward rotation
	private static final int POSITIVE_ROTATION = 1;
	
	// backward rotation
	private static final int NEGATIVE_ROTATION = -1;
	
	/**
	 * Creates a new Movement class with the given left and right ports.
	 * @param leftport the port to use for the left motor
	 * @param rightport the port to use for the right motor
	 */
	public Movement(MotorPort leftport, MotorPort rightport){

		left_motor = new NXTRegulatedMotor(leftport);
		right_motor = new NXTRegulatedMotor(rightport);
		
		setSpeed(START_SPEED, START_SPEED);
        lastSetSpeed = System.currentTimeMillis();
        dist_traveled = 0;
        heading = 0;
		
		setLeftMotorPort(leftport);
		setRightMotorPort(rightport);
	}

    public void updateDistHeading() {
        // Update approximate distance travelled and heading, based on previous motor speeds.
        long currentSetSpeed = System.currentTimeMillis();
        long elapsed = currentSetSpeed - lastSetSpeed;
        int lastLeftSpeed = left_motor.getSpeed() * LEFT_ROTATION;
        int lastRightSpeed = right_motor.getSpeed() * RIGHT_ROTATION;
        dist_traveled += (lastLeftSpeed + lastRightSpeed) * elapsed * DIST_MULTIPLIER;
        heading += (lastLeftSpeed - lastRightSpeed) * elapsed * HEADING_MULTIPLIER;
    }
	
	/**
	 * Sets the speed of the left and right motor. Negative speed values indicate backwards motion
	 * @param left_speed speed of the left motor
	 * @param right_speed speed of the right motor
	 */
	public void setSpeed(int left_speed, int right_speed){

        updateDistHeading();
		
		left_motor.setSpeed(left_speed);
		right_motor.setSpeed(right_speed);
		
		if (left_speed >= START_SPEED) {
			LEFT_ROTATION = POSITIVE_ROTATION;
			left_motor.forward();
		} else {
			LEFT_ROTATION = NEGATIVE_ROTATION;
			left_motor.backward();
		}
		
		if (right_speed >= START_SPEED) {
			RIGHT_ROTATION = POSITIVE_ROTATION;
			right_motor.forward();
		} else {
			RIGHT_ROTATION = NEGATIVE_ROTATION;
			right_motor.backward();
		}
	}

    public void setMaxSpeed(){
        setSpeed(MAX_SPEED, MAX_SPEED);
    }	
	
    public void halt(){
        setSpeed(START_SPEED, START_SPEED);
    }	
	
	
    /**
     * @return The approximate distance traveled.
     */
    public double getDistTraveled() {
        return dist_traveled;
    }

    /**
     * @return The approximate heading relative to initial heading.
     */
    public double getHeading() {
        return heading;
    }
	
	/**
	 * 
	 * @return the speed of the left motor
	 */
	public int getLeftSpeed(){
		return (int)(LEFT_ROTATION * left_motor.getSpeed());
	}
	
	/**
	 * 
	 * @return the speed of the right motor
	 */
	public int getRightSpeed(){
		return (int)(RIGHT_ROTATION * right_motor.getSpeed());
	}
	
	/**
	 * 
	 * @return the port of the left motor
	 */
	public MotorPort getLeftMotorPort(){
		return left_motor_port;
	}
	
	/**
	 * 
	 * @return the port of the right motor
	 */
	public MotorPort getRightMotorPort(){
		return right_motor_port;
	}
	
	/**
	 * Sets the port of the left motor
	 * @param lport the new port for the left motor
	 */
	public void setLeftMotorPort(MotorPort lport){
		left_motor_port = lport;
	}
	
	/**
	 * Sets the port of the right motor
	 * @param rport the new port for the right motor
	 */
	public void setRightMotorPort(MotorPort rport){
		right_motor_port = rport;
	}
	
	
}
