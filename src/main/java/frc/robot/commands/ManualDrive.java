// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;


/** An example command that uses an example subsystem. */
public class ManualDrive extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive_train;
  private final AdjustShooterAim adjustShooterAim;
  private final AimbotBall aimBall;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualDrive(DriveTrain subsystem) {
    drive_train = subsystem;
    adjustShooterAim = new AdjustShooterAim(drive_train);
    aimBall = new AimbotBall();
    addRequirements(drive_train);
    // Use addRequirements() here to declare subsystem dependencies.
  }
  

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      double ySpeed = (RobotContainer.flightJoystick.getLateralMovement());
      double xSpeed = (-RobotContainer.flightJoystick.getHorizontalMovement());
      double zRotation = (-RobotContainer.flightJoystick.getRotation());


      System.out.println("y: " + ySpeed + " x: "+ xSpeed + " z: " + zRotation);


      /*
      float Kp = -0.1f;
      NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("limelight");
      float min_command = 0.5f;
      float tx = (float) networkTable.getEntry("tx").getDouble(100000);

      if(!(tx > 98000)) {
        if(joystickButton.get()) {
          float headingError = -tx;
          float steering_adjust = 0.0f;
          if(tx > 1.0) {
            steering_adjust = Kp*headingError - min_command;
          } else if(tx < 1.0) {
            steering_adjust = Kp*headingError + min_command;
          }
          lat += 2*(steering_adjust);
        }
      }
      */

      if (RobotContainer.flightJoystick.getJoystickPOV() == 90 || RobotContainer.flightJoystick.getJoystickPOV() == 270)
        adjustShooterAim.schedule();
      
      aimBall.schedule();
      drive_train.drive(ySpeed, xSpeed, zRotation);

     
    }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive_train.stopMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

}
