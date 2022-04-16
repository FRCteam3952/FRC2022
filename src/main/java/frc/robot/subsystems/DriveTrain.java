// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 * self explanatory
 */

public class DriveTrain extends SubsystemBase {

  private static CANSparkMax frontLeft;
  private static CANSparkMax frontRight;
  private static CANSparkMax rearLeft;
  private static CANSparkMax rearRight;

  private final RelativeEncoder frontLeftEncoder;
  private final RelativeEncoder frontRightEncoder;
  private final RelativeEncoder rearLeftEncoder;
  private final RelativeEncoder rearRightEncoder;

  private NetworkTableInstance inst;
  private NetworkTable table;
  private NetworkTableEntry ball;
  private NetworkTableEntry seeBall;

  private MecanumDrive m_dDrive;

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

    frontRight.setInverted(false);
    rearRight.setInverted(false);
    frontLeft.setInverted(true);
    rearLeft.setInverted(true);

    m_dDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_dDrive.setSafetyEnabled(false);

  }

  public void drive(double ySpeed, double xSpeed, double zRotation) {
    m_dDrive.driveCartesian(ySpeed, xSpeed, zRotation, -Gyro.getGyroAngle());
    if (RobotContainer.primaryJoystick.joystick.getRawButtonPressed(Constants.resetGyroButtonNumber)) {
      Gyro.resetGyroAngle();
    }
  }

  public void driveRR(double ySpeed, double xSpeed, double zRotation) {
    m_dDrive.driveCartesian(ySpeed, xSpeed, zRotation, 0);
    if (RobotContainer.primaryJoystick.joystick.getRawButtonPressed(Constants.resetGyroButtonNumber)) {
      Gyro.resetGyroAngle();
    }
  }

  public double setAngle(double angle) {

    double angleDifference = angle - Gyro.getGyroAngle(); // gets angle difference

    if (Math.abs(angleDifference) >= 180) {
      /**
       * ensures that angleDifference is the smallest possible movement to the
       * destination
       */
      angleDifference = angleDifference + (angleDifference > 0 ? -360 : 360);
    }

    /**
     * positive angleDifference -> turn clockwise, negative angleDifference -> turn
     * counterclockwise
     * strength of turning power is proportional to size of angleDifference
     */
    double zRotation = angleDifference / 120;
    if (zRotation > 1)
      zRotation = 1;
    else if (zRotation < -1)
      zRotation = -1;
    return zRotation;
  }

  public double getAdjustment() {
    double adjustAngle = 0;
    if (seeBall.getBoolean(false)) {
      double adjustment = ball.getNumber(0).doubleValue()/2;
      adjustAngle = adjustment;

    }
    return adjustAngle;
  }

  public double getPosition() {
    double[] encoderPositions = { frontLeftEncoder.getPosition(), frontRightEncoder.getPosition(),
        rearLeftEncoder.getPosition(), rearRightEncoder.getPosition() };
    double sum = 0;
    for (double i : encoderPositions)
      sum += i;
    return sum / 4;
  }

  public double getFrontLeftEncoder() {
    System.out.println(frontLeftEncoder.getPosition());
    return Math.abs(frontLeftEncoder.getPosition());
  }

  public void resetFrontLeftEncoder() {
    frontLeftEncoder.setPosition(0);
  }

  public void setAllEncoders(double position) {
    frontLeftEncoder.setPosition(position);
    frontRightEncoder.setPosition(position);
    rearLeftEncoder.setPosition(position);
    rearRightEncoder.setPosition(position);
  }

  public void setTeam() {
    if(RobotContainer.primaryJoystick.joystick.getRawAxis(3) > 0.5)
      table.getEntry("blueBall").setBoolean(true);
    else
      table.getEntry("blueBall").setBoolean(false);
  }

  @Override
  public void periodic() {
  
  }

  @Override
  public void simulationPeriodic() {

  }

  public void stopMotors() {
    m_dDrive.stopMotor();
  }

}
