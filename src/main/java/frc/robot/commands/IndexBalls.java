// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Ingester;

public class IndexBalls extends CommandBase {
  private final Indexer index;
  private final Ingester ingest;
  private double power = -0.35;
  private IndexBallState indexState = IndexBallState.SEARCHING;
  private double delta = 0;
  private double shooterSpeed = 69; //change later, rpm? rps? test later lol
  private double shooterFinishedSpeed = 60; //speed of shooter wheel when indexer wheels should stop, test later
  private boolean shooterSpeedReached = false;

  private enum IndexBallState {
    SEARCHING, //searching for first ball
    PULLING, //index first ball into shooter
    SEARCHING2, //searching for second ball
    PULLING2, //index second ball into shooter
    SHOOTING, //wait for shooter wheel to gain enough speed, then index balls into shooter
  }

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public IndexBalls(Indexer indexer, Ingester ingester) {
    index = indexer;
    ingest = ingester;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(index, ingest);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("init indexballs");
    index.setIndexSpeed(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (indexState) {
      case SEARCHING:
        if (index.bottomShooterPressed()) {
          System.out.println("switching to pulling");
          indexState = IndexBallState.PULLING;
        }
        break;
      case PULLING:
        if (delta != 20) {
          System.out.println("switching t");
          index.setIndexSpeed(power);
          delta++;
        } 
        else {
          System.out.println("stopped pulling");
          delta = 0;
          index.setIndexSpeed(0);
          indexState = IndexBallState.SEARCHING2;
        }
        break;
      case SEARCHING2:
        if (index.bottomShooterPressed()) {
          System.out.println("switching to pulling2");
          indexState = IndexBallState.PULLING2;
        }
        break;
      case PULLING2:
        if (delta != 20) {
          System.out.println("switching t2");
          index.setIndexSpeed(power);
          delta++;
        } 
        else {
          System.out.println("stopped pulling2");
          delta = 0;
          index.setIndexSpeed(0);
          indexState = IndexBallState.SHOOTING;
        }
        break;
      case SHOOTING:
        if (index.getShooterRevPerSec() >= shooterSpeed) {
          index.setIndexSpeed(power);
          shooterSpeedReached = true;
        }
        if (index.getShooterRevPerSec() <= shooterFinishedSpeed) 
          indexState = IndexBallState.SEARCHING;
      default:
      }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    indexState = IndexBallState.SEARCHING;
    shooterSpeedReached = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
