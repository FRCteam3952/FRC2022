package frc.robot.subsystems;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

public class Gyro {
    private static ADIS16470_IMU gyro;

    public Gyro(){
        gyro = new ADIS16470_IMU();
        gyro.setYawAxis(ADIS16470_IMU.IMUAxis.kY);
    }
    public double getGyroAngle(){
        return gyro.getAngle();
      }
    public void setGyroAxis(ADIS16470_IMU.IMUAxis axis){
        gyro.setYawAxis(axis);
    }
    public void resetGyroAngle() {
        gyro.reset();
    }
}
