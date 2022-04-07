// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Ingester;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class IngestBalls extends CommandBase {
  private final Ingester ingest;
  private final double INGEST_SPEED = 0.6;
  private final double INDEX_SPEED = 0.4;
  public boolean isTopHit = false;
  
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public IngestBalls(Ingester subsystem) {
    ingest = subsystem;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      ingest.setIngestRollerSpeed(0);
      System.out.println("ingesting");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ingest.setIngestRollerSpeed(-INGEST_SPEED);
    // index.setIndexSpeed(INDEX_SPEED * (index.ballShooterPressed() ? 1: -1));
    // index.setIndexSpeed(-INDEX_SPEED);
    /*
    if (index.ballShooterPressed()) {
      isTopHit = true;
    }
    if (!isTopHit && !index.ballShooterPressed()) {
      ingest.setTopRollerSpeed(-INDEX_SPEED);
      // ingest.setIngestRollerSpeed(0);
    } else if (isTopHit && !index.bottomShooterPressed()) {
      ingest.setTopRollerSpeed(INDEX_SPEED);
      ingest.setIngestRollerSpeed(0);
    } else {

    }
    */
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    ingest.setIngestRollerSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
