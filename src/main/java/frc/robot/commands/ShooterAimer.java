// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;

import java.util.function.Consumer;


/** An example command that uses an example subsystem. */
public class ShooterAimer extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive_train;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShooterAimer(DriveTrain subsystem) {
    drive_train = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {    

      double speed = 0;
      double rot = 0;

      float Kp = -0.1f; //proportional control constant
      NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("limelight");
      float min_command = 0.05f;
      float tx = (float) networkTable.getEntry("tx").getDouble(0.0);

      float heading_error = -tx;
      float steering_adjust = 0.0f;
      if (tx > 1.0) {
        steering_adjust = Kp * heading_error - min_command;
      }
      else if (tx < 1.0) {
        steering_adjust = Kp * heading_error + min_command;
      }
      rot += steering_adjust;
      System.out.println("auto aim rotation: " + rot);

      drive_train.drive(speed, rot);
    }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
