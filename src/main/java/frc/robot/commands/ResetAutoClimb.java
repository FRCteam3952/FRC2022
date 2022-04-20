package frc.robot.commands;

import frc.robot.commands.AutoClimb.ClimbingStates;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Reset auto climb stage with a (skechy) static var change
 */

public class ResetAutoClimb extends CommandBase {

  public ResetAutoClimb() {

  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    AutoClimb.state = ClimbingStates.LIFTING_OFF_GROUND;
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
