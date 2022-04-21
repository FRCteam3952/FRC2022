package frc.robot.commands;

import frc.robot.RobotContainer;
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
    RobotContainer.autoClimb = new AutoClimb(RobotContainer.climberHooks, RobotContainer.climberArm);
    System.out.println("reset by button");
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
