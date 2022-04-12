package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

public class Gyro extends SubsystemBase{
    private static ADIS16470_IMU gyro;

    public Gyro(){
        gyro = new ADIS16470_IMU();
        gyro.setYawAxis(ADIS16470_IMU.IMUAxis.kY);
    }
    public static double getGyroAngle(){
        return gyro.getAngle();
      }
    public static void setGyroAxis(ADIS16470_IMU.IMUAxis axis){
        gyro.setYawAxis(axis);
    }
    public static void resetGyroAngle() {
        gyro.reset();
    }
    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
  
    @Override
    public void simulationPeriodic() {
      // This method will be called once per scheduler run during simulation
    }
}

