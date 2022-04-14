package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.math.controller.PIDController;

public class LimeLight extends SubsystemBase{
    private NetworkTableInstance inst;
    private NetworkTable table;
    private PIDController pidcontrol;
    private final float kp = 0.05f;
    private final float ki = 0.01f;
    private final float kd = 0.02f;

    public LimeLight(){
        pidcontrol = new PIDController(1, ki, kd);
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("limelight");
    }
    public double getAdjustment(){
        float tx = table.getEntry("tx").getNumber(0).floatValue() * kp;
        double steering_adjust = pidcontrol.calculate(tx);
        return steering_adjust;
    }
    
    @Override
    public void periodic() {
        
    }
  
    @Override
    public void simulationPeriodic() {
  
    }
    
}
