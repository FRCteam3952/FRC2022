// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


/** An example command that uses an example subsystem. */
public class ManualDrive extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive_train;
  private final AdjustShooterAim adjustShooterAim;
  private NetworkTableInstance inst;
  private NetworkTable table;
  private NetworkTableEntry ball;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualDrive(DriveTrain subsystem) {
    drive_train = subsystem;
    adjustShooterAim = new AdjustShooterAim(drive_train);
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("Vision");
    ball = table.getEntry("PID");
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

    if (RobotContainer.flightJoystick.button2Pressed()) {
        xSpeed += ball.getNumber(0).doubleValue();
    }

    if (RobotContainer.flightJoystick.getJoystickPOV() == 90 || RobotContainer.flightJoystick.getJoystickPOV() == 270)
      adjustShooterAim.schedule();
      
    if (xSpeed > 1)
      xSpeed = 1;
    if (xSpeed < -1)
      xSpeed = -1;

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
