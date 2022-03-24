// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.security.spec.EncodedKeySpec;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAlternateEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import frc.robot.Constants;


public class DriveTrain extends SubsystemBase {

  private final CANSparkMax frontLeft;
  private final CANSparkMax frontRight;
  private final CANSparkMax rearLeft;
  private final CANSparkMax rearRight;

  private final RelativeEncoder frontLeftEncoder;
  private final RelativeEncoder frontRightEncoder;
  private final RelativeEncoder rearLeftEncoder;
  private final RelativeEncoder rearRightEncoder;
  private final PIDController driveEncoders;
  private double yMeasurement;
  private double xMeasurement;
  private double zMeasurement;
  private double setpoint;

  private double kp = 0.0015;
  private double ki = 0.001;
  private double kd = 0;

  private MecanumDrive m_dDrive;

  private boolean settingDistance = true;
  /** Creates a new ExampleSubsystem. */
  public DriveTrain() {
    frontLeft = new CANSparkMax(Constants.frontLeftMotorPort, MotorType.kBrushless);
    frontRight = new CANSparkMax(Constants.frontRightMotorPort, MotorType.kBrushless);
    rearLeft = new CANSparkMax(Constants.rearLeftMotorPort, MotorType.kBrushless);
    rearRight = new CANSparkMax(Constants.rearRighttMotorPort, MotorType.kBrushless);

    frontLeftEncoder = frontLeft.getEncoder();
    frontRightEncoder = frontRight.getEncoder();
    rearLeftEncoder = rearLeft.getEncoder();
    rearRightEncoder = rearRight.getEncoder();
    driveEncoders = new PIDController(kp, ki, kd);  

    m_dDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_dDrive.setSafetyEnabled(false); // MAKE SURE TO DISABLE THIS BEFORE TESTING BOT TODO



  }


  public void drive(double ySpeed, double xSpeed, double zRotation) {
    double y = ySpeed;
    double x = xSpeed;
    double z = zRotation;
    
    driveEncoders.calculate(yMeasurement, y);
    driveEncoders.calculate(xMeasurement, x);
    driveEncoders.calculate(zMeasurement, z);
    m_dDrive.driveCartesian(y, x, z);
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
