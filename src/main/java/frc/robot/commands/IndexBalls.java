// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class IndexBalls extends CommandBase {
  private final Indexer index;
  private double power;
  private double indexStage = 0;
  long startTime;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public IndexBalls(Indexer subsystem) {
    index = subsystem;
    power = 0.5;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }
  public boolean timer(long time){
    return (System.currentTimeMillis() - startTime) > time;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (index.L1Pressed() && indexStage == 0){
      indexStage++;
      index.setIndexSpeed(power);
      startTime = System.currentTimeMillis();nce = System.currentTimeMillis() - startTime;
      if(timeDifference > 500){
        index.setIndexSpeed(0);
        indexStage++;
      }
    }
    else if(index.L1Pressed() && indexStage == 2){
      index.setIndexSpeed(power);
      indexStage++;
    }
    else if(index.L2Pressed() && indexStage == 3){
      index.setIndexSpeed(0);
      indexStage++;
    }
    else if (index.getRevPerSec() == 69 && indexStage == 4)
    {
      index.setIndexSpeed(power);
      startTime = System.currentTimeMillis();
      indexStage++;
    }
    else if (indexStage == 5){

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
