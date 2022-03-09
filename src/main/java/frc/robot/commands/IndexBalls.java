// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Ingester;
import frc.robot.RobotContainer;

public class IndexBalls extends CommandBase {
  private final Indexer index;
  private final Ingester ingest;
  private double lowPower = -0.20;
  private double highPower = -0.35;
  private IndexBallState indexState = IndexBallState.SEARCHING;
  private double delta = 0;
  private double shooterSpeed = 69; //change later, rpm? rps? test later lol
  private double shooterFinishedSpeed = 60; //speed of shooter wheel when indexer wheels should stop, test later
  private boolean shooterSpeedReached = false;

  private enum IndexBallState {
    SEARCHING, //searching for first ball
    PULLING, //pulling first ball with higher power
    SEARCHING2, //searching for second ball
    PULLING2, //pulling second ball with higher power
    REVERSEINDEX, //waits for shooter trigger and starts reversing indexing
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
          index.setIndexSpeed(highPower); //switch to highpower once L1 is detected
          System.out.println("switching to pulling");
          indexState = IndexBallState.PULLING;
        }
        break;

      case PULLING:
        if (delta < 50) {
          System.out.println("switching t" + delta);
          index.setIndexSpeed(highPower);
          delta++;
        } 
        else {
          System.out.println("stopped pulling");
          delta = 0;
          index.setIndexSpeed(lowPower);
          indexState = IndexBallState.SEARCHING2;
        }
        break;

      case SEARCHING2:
        if (index.bottomShooterPressed()) {
          index.setIndexSpeed(highPower); //switch to highpower once L1 is detected
          System.out.println("switching to pulling");
          indexState = IndexBallState.PULLING2;
        }
        break;

      case PULLING2:
        if (delta < 30) {
          System.out.println("switching t 2 " + delta);
          index.setIndexSpeed(highPower);
          delta++;
        } 
        else {
          System.out.println("stopped pulling 2");
          index.setIndexSpeed(lowPower);
          if(RobotContainer.driverStick.button5Pressed()) {
            delta = 0;
            indexState = IndexBallState.REVERSEINDEX;
          }
        }
        break;
        
      case REVERSEINDEX: 
        System.out.println("reversing index");
        index.setIndexSpeed(-lowPower);
        if(index.ballShooterPressed()) {
          indexState = IndexBallState.SHOOTING;
          index.setIndexSpeed(0);
        }
        break;
        
      case SHOOTING:
        if (index.getShooterRevPerSec() <= shooterFinishedSpeed && !shooterSpeedReached) 
          index.setIndexSpeed(0);
        if (index.getShooterRevPerSec() >= shooterSpeed) {
          index.setIndexSpeed(lowPower);
          shooterSpeedReached = true;
        }
        if (index.getShooterRevPerSec() <= shooterFinishedSpeed && shooterSpeedReached) 
          indexState = IndexBallState.SEARCHING;
        break;
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
