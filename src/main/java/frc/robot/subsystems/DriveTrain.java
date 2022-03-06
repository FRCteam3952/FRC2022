// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import frc.robot.Constants;
public class DriveTrain extends SubsystemBase {

  private final Talon frontLeft;
  private final Talon frontRight;
  private final Talon rearLeft;
  private final Talon rearRight;

  private MotorControllerGroup left;
  private MotorControllerGroup right;

  private DifferentialDrive m_dDrive;
  /** Creates a new ExampleSubsystem. */
  public DriveTrain() {
    frontLeft = new Talon(Constants.frontLeftMotorPort);
    frontRight = new Talon(Constants.frontRightMotorPort);
    rearLeft = new Talon(Constants.rearLeftMotorPort);
    rearRight = new Talon(Constants.rearRighttMotorPort);

    left = new MotorControllerGroup(frontRight, frontLeft);
    right = new MotorControllerGroup(rearLeft, rearRight);
  
    m_dDrive = new DifferentialDrive(left,right);
    m_dDrive.setSafetyEnabled(false); // MAKE SURE TO DISABLE THIS BEFORE TESTING BOT TODO

    //m_left.setInverted(true);

  }


  public void drive(double speed, double rot){
    m_dDrive.arcadeDrive(speed, rot);
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
