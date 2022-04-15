package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.math.controller.PIDController;

public class Limelight extends SubsystemBase {
    private static NetworkTableInstance inst;
    private static NetworkTable table;
    private PIDController pidcontrol;
    private final float kp = 0.05f;
    private final float ki = 0.01f;
    private final float kd = 0.02f;

    public static double limelightAngleDeg = 30;
    public static double limelightHeightInch = 29;
    public static double goalHeightInch = 104;

    public Limelight(){
        pidcontrol = new PIDController(1, ki, kd);
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("limelight"); //initiate limelight table
    }

    public double getAdjustment(){
        float tx = table.getEntry("tx").getNumber(0).floatValue() * kp; //get target x position
        double steering_adjust = pidcontrol.calculate(tx); //calculate PID
        return steering_adjust;
    }
    
    public static double distanceToHoop() {
        turnOnLED();
        double targetOffsetAngleVert = table.getEntry("ty").getDouble(0.0);
        double angletoGoalDeg = limelightAngleDeg + targetOffsetAngleVert;
        double angletoGoalRad = angletoGoalDeg * (Math.PI / 180);
        turnOffLED();
        return (goalHeightInch - limelightHeightInch) / Math.tan(angletoGoalRad) * 0.0254; //returns in meters
  }
    public static void turnOnLED(){
        table.getEntry("ledMode").setDouble(3);
    }
    public static void turnOffLED(){
        table.getEntry("ledMode").setDouble(1);
    }
    @Override
    public void periodic() {
        
    }
  
    @Override
    public void simulationPeriodic() {
  
    }
    
}
