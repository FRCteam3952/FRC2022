package frc.robot.commands;

import frc.robot.subsystems.Shooter;
import frc.robot.RobotContainer;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Manual setting of shooter power.
 * Obsolete due to design choice to set flywheel to power of 0.85 for the entire
 * match.
 */

public class AdjustShooterPowerManual extends CommandBase {
  private final Shooter shooter;

  private final double MAX_RPM = 500;
  private final double MIN_RPM = -500;
  private double shooterRPM = MIN_RPM;
  private double previousShooterRPM = 0;

  public AdjustShooterPowerManual(Shooter shooter) {
    this.shooter = shooter;
    addRequirements(shooter);
  }

  public void setShooterRPM() {
    double sliderValue = RobotContainer.tertiaryJoystick.joystick.getRawAxis(2); // axis channel for slider
    sliderValue += 1;
    sliderValue /= 2;

    shooterRPM = sliderValue * (MAX_RPM - MIN_RPM) - Math.abs(MIN_RPM);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    setShooterRPM();
    previousShooterRPM = shooter.getRPMValue();
    shooter.setRPMValue(previousShooterRPM + shooterRPM); // pass new RPM value to shooter subsystem
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
