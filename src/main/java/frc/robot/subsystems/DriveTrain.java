// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Constants;


public class DriveTrain extends SubsystemBase {

  private static CANSparkMax frontLeft;
  private static CANSparkMax frontRight;
  private static CANSparkMax rearLeft;
  private static CANSparkMax rearRight;
  //private static ADIS16470_IMU gyro;

  // private final RelativeEncoder frontLeftEncoder;
  // private final RelativeEncoder frontRightEncoder;
  // private final RelativeEncoder rearLeftEncoder;
  // private final RelativeEncoder rearRightEncoder;
  // private final PIDController driveEncoders;
  // private double yMeasurement;

  /*
  private double xMeasurement;
  private double zMeasurement;

  private double kp = 0.0015;
  private double ki = 0.001;
  private double kd = 0;
  */

  private MecanumDrive m_dDrive;

  private boolean settingDistance = true;
  /** Creates a new ExampleSubsystem. */
  public DriveTrain() {
    frontLeft = new CANSparkMax(Constants.frontLeftMotorPort, MotorType.kBrushless);
    frontRight = new CANSparkMax(Constants.frontRightMotorPort, MotorType.kBrushless);
    rearLeft = new CANSparkMax(Constants.rearLeftMotorPort, MotorType.kBrushless);
    rearRight = new CANSparkMax(Constants.rearRighttMotorPort, MotorType.kBrushless);
    //gyro = new ADIS16470_IMU();
    frontRight.setInverted(false);
    rearRight.setInverted(false);
    frontLeft.setInverted(true);
    rearLeft.setInverted(true);
    
  
    

    // frontLeftEncoder = frontLeft.getEncoder();
    // frontRightEncoder = frontRight.getEncoder();
    // rearLeftEncoder = rearLeft.getEncoder();
    // rearRightEncoder = rearRight.getEncoder();
    // driveEncoders = new PIDController(kp, ki, kd);  

    m_dDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_dDrive.setSafetyEnabled(false); // MAKE SURE TO DISABLE THIS BEFORE TESTING BOT TODO



  }


  public void drive(double ySpeed, double xSpeed, double zRotation) {
    //System.out.println("y: " + ySpeed + " x: " + xSpeed + " rot: " + zRotation);
    m_dDrive.driveCartesian(ySpeed, xSpeed, zRotation, 0);
    //aimBall.schedule();
  }

  public void setShooterDistanceFinished() {
    settingDistance = false;
  }
  
  public void setShooterDistanceReset() {
    settingDistance = true;
  }

  public boolean isSettingShooterDistance() {
    return settingDistance;
  }

  public void setMecanumDrive(double translationAngle, double translationPower, double turnPower)
{
    // calculate motor power
    double ADPower = translationPower * Math.sqrt(2) * 0.5 * (Math.sin(translationAngle) + Math.cos(translationAngle));
    double BCPower = translationPower * Math.sqrt(2) * 0.5 * (Math.sin(translationAngle) - Math.cos(translationAngle));

    // System.out.println( "front left: " + frontLeft.isFollower() + " front right " + frontRight.isFollower() + " rear left " + rearLeft.isFollower() + " rear right " + rearRight.isFollower());
    // check if turning power will interfere with normal translation
    // check ADPower to see if trying to apply turnPower would put motor power over 1.0 or under -1.0
    double turningScale = Math.max(Math.abs(ADPower + turnPower), Math.abs(ADPower - turnPower)); 
    // check BCPower to see if trying to apply turnPower would put motor power over 1.0 or under -1.0
    turningScale = Math.max(turningScale, Math.max(Math.abs(BCPower + turnPower), Math.abs(BCPower - turnPower)));

    // adjust turn power scale correctly
    if (Math.abs(turningScale) < 1.0)
    {
        turningScale = 1.0;
    }

    // set the motors, and divide them by turningScale to make sure none of them go over the top, which would alter the translation angle
//     frontLeft.set((ADPower - turningScale) / turningScale);
//     rearLeft.set((BCPower - turningScale) / turningScale);
//     frontRight.set((BCPower + turningScale) / turningScale);
//     rearRight.set((ADPower + turningScale) / turningScale);
System.out.println((ADPower - turningScale) / turningScale);
System.out.println((BCPower - turningScale) / turningScale);
System.out.println((BCPower + turningScale) / turningScale);
System.out.println((ADPower + turningScale) / turningScale);
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
