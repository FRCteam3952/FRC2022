// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class DriveTrain extends SubsystemBase {

  private static CANSparkMax frontLeft;
  private static CANSparkMax frontRight;
  private static CANSparkMax rearLeft;
  private static CANSparkMax rearRight;
  private static ADIS16470_IMU gyro;

  private final RelativeEncoder frontLeftEncoder;
  private final RelativeEncoder frontRightEncoder;
  private final RelativeEncoder rearLeftEncoder;
  private final RelativeEncoder rearRightEncoder;

  private NetworkTableInstance inst;
  private NetworkTable table;
  private NetworkTableEntry ball;
  private NetworkTableEntry seeBall;

  private MecanumDrive m_dDrive;

  private boolean settingDistance = true;
  /** Creates a new ExampleSubsystem. */
  public DriveTrain() {
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("Vision");
    ball = table.getEntry("PID");
    seeBall = table.getEntry("seeBall");

    frontLeft = new CANSparkMax(Constants.frontLeftMotorPort, MotorType.kBrushless);
    frontRight = new CANSparkMax(Constants.frontRightMotorPort, MotorType.kBrushless);
    rearLeft = new CANSparkMax(Constants.rearLeftMotorPort, MotorType.kBrushless);
    rearRight = new CANSparkMax(Constants.rearRighttMotorPort, MotorType.kBrushless);
    frontLeftEncoder = frontLeft.getEncoder();
    frontRightEncoder = frontRight.getEncoder();
    rearLeftEncoder = rearLeft.getEncoder();
    rearRightEncoder = rearRight.getEncoder();
    gyro = new ADIS16470_IMU();
    gyro.setYawAxis(ADIS16470_IMU.IMUAxis.kY);
    frontRight.setInverted(false);
    rearRight.setInverted(false);
    frontLeft.setInverted(true);
    rearLeft.setInverted(true);

    m_dDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_dDrive.setSafetyEnabled(false); 

  }


  public void drive(double ySpeed, double xSpeed, double zRotation) {
    m_dDrive.driveCartesian(ySpeed, xSpeed, zRotation, -gyro.getAngle());
    if(RobotContainer.primaryJoystick.button8Pressed()){
      gyro.reset();
    }
  }

  public void drive(double ySpeed, double xSpeed, double zRotation, double angle) {
    m_dDrive.driveCartesian(ySpeed, xSpeed, zRotation, angle);
    if(RobotContainer.primaryJoystick.button8Pressed()){
      gyro.reset();
    }
  }

  

  public double[] getAdjustment() {
    double[] adjustXY = {0,0};
    if(seeBall.getBoolean(false)){
      double adjustment = ball.getNumber(0).doubleValue();
      double minPower = 0.25;
      if(adjustment < minPower && adjustment > 0){
        adjustment = minPower;
      }
      else if(adjustment > -minPower && adjustment < 0){
        adjustment = -minPower;
      }
      double angle = gyro.getAngle();
      adjustXY[0] = adjustment * Math.cos(angle);
      adjustXY[1] = adjustment * Math.sin(angle);
      
    };
    return adjustXY;
  }

  public double getPosition() {
    double[] encoderPositions = {frontLeftEncoder.getPosition(), frontRightEncoder.getPosition(), rearLeftEncoder.getPosition(), rearRightEncoder.getPosition()};
    double sum = 0;
    for (double i : encoderPositions)
      sum += i;
    return sum/4;
  }

  public void setPosition(double position) {
    frontLeftEncoder.setPosition(position);
    frontRightEncoder.setPosition(position);
    rearLeftEncoder.setPosition(position);
    rearRightEncoder.setPosition(position);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void stopMotors() {
    m_dDrive.stopMotor();
  }
  
}
