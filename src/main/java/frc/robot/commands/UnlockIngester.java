// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.IngesterPositioner;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class UnlockIngester extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final IngesterPositioner ingest;
  public long timeDifference = 0; //milliseconds
  public long timeUntilStop = 1000; // CHANGE THIS WHEN FIND OUT TODO
  public double speed = .5;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public UnlockIngester(IngesterPositioner subsystem) {
    ingest = subsystem;
    addRequirements(ingest);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double delta = 0.7;
    double yDisplacement = RobotContainer.flightJoystick.getLateralMovement();
    //System.out.println(yDisplacement);
    if (yDisplacement > delta) {
      ingest.changeIngestAngle(speed);
    } else if (yDisplacement < -delta) {
      ingest.changeIngestAngle(-speed);
    } else {
      ingest.changeIngestAngle(0);
    }
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
