package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.math.controller.PIDController;

public class Limelight extends SubsystemBase {
    private NetworkTableInstance inst;
    private NetworkTable table;
    private PIDController pidcontrol;
    private final float kp = 0.05f;
    private final float ki = 0.01f;
    private final float kd = 0.02f;

    public static final double LIMELIGHT_ANGLE_DEG = 34;
    public static final double LIMELIGHT_HEIGHT_INCH = 24;
    public static final double GOAL_HEIGHT_INCH = 96;

    public Limelight() {
        pidcontrol = new PIDController(1, ki, kd);
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("limelight"); // initiate limelight table
    }

    public double getAdjustment() {
        float tx = table.getEntry("tx").getNumber(0).floatValue() * kp; // get target x position
        double steering_adjust = pidcontrol.calculate(tx); // calculate PID
        
        return steering_adjust;
    }

    public double getDistance() {
        double targetOffsetAngleVert = table.getEntry("ty").getDouble(0.0);
        double angletoGoalDeg = LIMELIGHT_ANGLE_DEG + targetOffsetAngleVert;
        double angletoGoalRad = angletoGoalDeg * (Math.PI / 180);
        
        return (GOAL_HEIGHT_INCH - LIMELIGHT_HEIGHT_INCH) / Math.tan(angletoGoalRad) * 0.0254;
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
