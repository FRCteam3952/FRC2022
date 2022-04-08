// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ManualDrive extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive_train;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualDrive(DriveTrain subsystem) {
    drive_train = subsystem;
    addRequirements(drive_train);
    // Use addRequirements() here to declare subsystem dependencies.
  }
  

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double ySpeed = (RobotContainer.primaryJoystick.getLateralMovement());
    double xSpeed = (-RobotContainer.primaryJoystick.getHorizontalMovement());
    double zRotation = (-RobotContainer.primaryJoystick.getRotation());

    // adjust movement of robot towards ball
    if (RobotContainer.primaryJoystick.button2Pressed()) {

      // x and y movement adjustment values
      double[] xySpeed = drive_train.getAdjustment();
      xSpeed += xySpeed[0];
      ySpeed += xySpeed[1];

      if (xSpeed > 1)
        xSpeed = 1;
      if (xSpeed < -1)
        xSpeed = -1;

      if (ySpeed > 1)
        ySpeed = 1;
      if (ySpeed < -1)
        ySpeed = -1;
    }

    //set angle
    if (RobotContainer.secondaryJoystick.getLateralMovement() != 0 || RobotContainer.secondaryJoystick.getHorizontalMovement() != 0) {
      System.out.println("setting angle");
      double y = RobotContainer.secondaryJoystick.getLateralMovement();
      double x = RobotContainer.secondaryJoystick.getHorizontalMovement();
      double angle = Math.toDegrees(Math.atan2(y, x)); //gets angle of the joystick

      //if (y < 0)
      //  angle += 360; //make sure angle is within 0˚ to 360˚ scale
      angle %= angle % 360;

      double angleDifference = angle - drive_train.getGyroAngle(); //gets angle difference

      if (Math.abs(angleDifference) >= 180)
        angleDifference = angleDifference + (angleDifference > 0 ? -360 : 360); //ensures that angleDifference is the smallest possible angle to destination

      // positive angleDifference -> turn clockwise, negative angleDifference -> turn counterclockwise
      // strength of turning power is proportional to size of angleDifference
      zRotation = angleDifference/180/3;
    }

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
