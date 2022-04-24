package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.math.controller.PIDController;

public class Limelight extends SubsystemBase {
    private NetworkTableInstance inst;
    private NetworkTable table;
    private PIDController pidcontrol;
    private final float kp = 0.02f;
    private final float ki = 0.01f;
    private final float kd = 0f;

    public static final double LIMELIGHT_ANGLE_DEG = 35;
    public static final double LIMELIGHT_HEIGHT_INCH = 22;
    public static final double GOAL_HEIGHT_INCH = 104;

    private double launchSpeed = 0;
    private double shooterRPM = 0;

    private final double HOOP_HEIGHT = 2.6416; // in meters
    private final double HOOP_RADIUS = 0.6096; // in meters
    private final double WHEEL_RADIUS = 0.0619125; // in meters
    private final double BALL_MASS = 0.26932047; // in kilograms
    private final double WHEEL_MASS = 0.144582568 * 2; // in kilograms
    private final double GRAVITY = 9.80665; // in meters per second squared
    private final double ANGLE = 63; // degrees
    private final double SHOOTER_HEIGHT = 0.65; // in meters
    private final double DELTA = 0.6;
    private final double speedFactor = 0.99; //direct multiplicative to shooter RPM
    private float prev_tx = 0f;

    public Limelight() {
        pidcontrol = new PIDController(1, ki, kd);
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("limelight"); // initiate limelight table
    }

    public double getAdjustment() {
        float tx = table.getEntry("tx").getNumber(0).floatValue() * kp; // get target x position
        if(tx > 1){
            tx =1;
        }
        if(tx < -1){
            tx = -1;
        }
        if(tx < 0.01 && tx > -0.01){
            tx = prev_tx;
        }
        else{
            prev_tx = tx;
        }
        double steering_adjust = pidcontrol.calculate(tx); // calculate PID
        
        return steering_adjust;
    }

    public double getDistance() {
        double targetOffsetAngleVert = table.getEntry("ty").getDouble(0.0);
        double angletoGoalDeg = LIMELIGHT_ANGLE_DEG + targetOffsetAngleVert;
        double angletoGoalRad = angletoGoalDeg * (Math.PI / 180);
        double distance = (GOAL_HEIGHT_INCH - LIMELIGHT_HEIGHT_INCH) / Math.tan(angletoGoalRad) * 0.0254;
        System.out.println(distance);
        return distance;
    }

    public void setLaunchSpeed() {
        double x = getDistance() + HOOP_RADIUS - DELTA;
        double y = HOOP_HEIGHT - SHOOTER_HEIGHT;
        double a = Math.toRadians(ANGLE);
        double g = GRAVITY;
        double velocity = Math.sqrt((-(g / 2) * Math.pow(x, 2)) / ((y - x * Math.tan(a)) * Math.pow(Math.cos(a), 2)));
        
        launchSpeed = velocity;
      }
    
      public void setShooterRPM() {
        double wheelTanSpeed = 2 * launchSpeed * ((WHEEL_MASS + ((7 / 5) * BALL_MASS)) / WHEEL_MASS);
        double angularVelocity = wheelTanSpeed / WHEEL_RADIUS;
        
        shooterRPM = (angularVelocity * 60) / (2 * Math.PI) * speedFactor;
      }
    
      public double getShooterRPM() {
          return shooterRPM;
      }

    public void turnOnLED() {
        table.getEntry("ledMode").setDouble(3);
    }

    public void turnOffLED() {
        table.getEntry("ledMode").setDouble(1);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void simulationPeriodic() {

    }

}
