// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;



/** An example command that uses an example subsystem. */
public class AdjustShooter extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive_train;
  private final AdjustShooterAim adjustAim;
  private final SetShooterDistance setDistance;
  private boolean settingDistance = true;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public AdjustShooter(DriveTrain subsystem) {
    drive_train = subsystem;
    adjustAim = new AdjustShooterAim(subsystem);
    setDistance = new SetShooterDistance(subsystem);
    addRequirements(drive_train);
    // Use addRequirements() here to declare subsystem dependencies.
  }
  
  // public void set

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (settingDistance) {
      setDistance.schedule();
      
        settingDistance = false;
    }
    else {
      adjustAim.schedule();
    }
  }
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    settingDistance = true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
