// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Joystick;


/**
 * Mecanum drive command
 */
public class DriveMecanum extends CommandBase {
  private final DriveTrain drive_train;
  private Joystick driver = RobotContainer.flightJoystick.joystick;

  public DriveMecanum(DriveTrain subsystem)
  {
      drive_train = subsystem;
      // Use addRequirements() here to declare subsystem dependencies
      addRequirements(subsystem);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize()
  {

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute()
  {
      // get joystick input
      double angle = Math.atan2(driver.getRawAxis(1), driver.getRawAxis(0));
      double magnitude = Math.hypot(driver.getRawAxis(0), driver.getRawAxis(1));
      double twist = driver.getRawAxis(2);

      // use field centric controls by subtracting off the robot angle
      // angle -= drive_train.DRIVE_GYRO.get();

      drive_train.setMecanumDrive(angle, magnitude, twist);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished()
  {
      return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted)
  {
      // sets all drive wheels to 0.0
      drive_train.setMecanumDrive(0.0, 0.0, 0.0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  public void interrupted()
  {
      end(true);
  }
}