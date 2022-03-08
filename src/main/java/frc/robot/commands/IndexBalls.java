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
  private final StopIngester stopIngest;
  private double power;
  private double indexStage;
  private boolean ingestStopped;
  private long timeDifference; //milliseconds
  private long timeUntilStop = 1000; //milliseconds CHANGE TODO
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public IndexBalls(Indexer indexer, Ingester ingester) {
    index = indexer;
    ingest = ingester;
    stopIngest = new StopIngester(ingest);
    power = -0.5;
    ingestStopped = false;
    indexStage = 0;
    timeDifference = 0;
    
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(index, ingest);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (index.bottomShooterPressed() && indexStage == 0) {

      index.setIndexSpeed(power);
      if(!index.bottomShooterPressed()){
        index.setIndexSpeed(0);
        indexStage++;
      }
    } //load first ball
    else if (index.bottomShooterPressed() && indexStage == 1) {
      //index.setIndexSpeed(power);
     // if(index.ballShooterPressed())
       // indexStage++;
    } //load second ball
    else if (indexStage == 2) {
      index.setIndexSpeed(0);
      if (!ingestStopped) {
        stopIngest.schedule();
        ingestStopped = true;
      }
      if (index.getShooterRevPerSec() >= 69D) {
        indexStage++;
      }
    } //stop ingester and wait until shooter wheel is ready
    else if (indexStage == 3) {
      index.setIndexSpeed(power);
      if (index.getShooterRevPerSec() <= 60D)
        indexStage++;
    } //shoot balls
    else if (indexStage == 4) {
      index.setIndexSpeed(0);
      stopIngest.cancel();
      ingestStopped = false;
      indexStage = 0;
    } //reset index wheels and start ingester

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
