// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Indexer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class IndexBalls extends CommandBase {
  private final Indexer index;
  private double power;
  private double indexStage;
  long timeDifference; //milliseconds
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public IndexBalls(Indexer subsystem) {
    index = subsystem;
    power = 0.5;
    indexStage = 0;
    timeDifference = 0;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (index.bottomShooterPressed() && indexStage == 0){
      indexStage++;
      index.setIndexSpeed(power);
      timeDifference += 20; 
      if(timeDifference > 500){
        index.setIndexSpeed(0);
        indexStage++;
      }
    }
    else if(index.bottomShooterPressed() && indexStage == 2){
      index.setIndexSpeed(power);
      indexStage++;
    }
    else if(index.ballShooterPressed() && indexStage == 3){
      index.setIndexSpeed(0);
      indexStage++;
    }
    else if (index.getRevPerSec() == 69 && indexStage == 4)
    {
      index.setIndexSpeed(power);
      if (index.getRevPerSec() <= 60)
        indexStage++;
    }
    else if (indexStage == 5){
      index.setIndexSpeed(0);
      cancel();
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
