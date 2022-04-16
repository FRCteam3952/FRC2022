package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.RobotContainer;

/**
 * Manual setting of shooter power.
 * Obsolete due to design choice to set flywheel to power of 0.85 for the entire
 * match.
 */

public class SetShooterPowerManual extends CommandBase {
  private final Shooter shooter;

  private double maxRPM = 5800;
  private double minRPM = 0;
  private double shooterRPM = minRPM;

  public SetShooterPowerManual(Shooter shooter) {
    this.shooter = shooter;
    addRequirements(shooter);
  }

  public void setShooterRPM() {
    double sliderValue = RobotContainer.secondaryJoystick.joystick.getRawAxis(2); // axis channel for slider
    sliderValue += 1;
    sliderValue /= 2;
    shooterRPM = maxRPM - (minRPM + sliderValue * (maxRPM - minRPM));
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    setShooterRPM();
    shooter.setRPMValue(shooterRPM); // pass RPM value to shooter subsystem
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
