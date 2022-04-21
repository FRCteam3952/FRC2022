package frc.robot.commands;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Automatically set shooter power.
 * Currently obsolete due to design choice to set flywheel to power of 0.85 for
 * the entire match.
 */

public class SetShooterPower extends CommandBase {
  private final Shooter shooter;
  private final Limelight limelight;

  private final double HOOP_RADIUS = 0.6096; // in meters
  private final double MIN_DISTANCE = 2.6919; // in meters
  private final double DELTA = 0 - MIN_DISTANCE; // in meters

  public SetShooterPower(Shooter shooter, Limelight limey) {
    this.shooter = shooter;
    this.limelight = limey;
    addRequirements(shooter);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    limelight.turnOnLED();
    if (limelight.getDistance() + HOOP_RADIUS < MIN_DISTANCE + DELTA) {
      System.out.println("Robot too close to hub to shoot, backing up");
      // driveTrain.driveRR(-0.5, 0, 0);
    } else {
      limelight.setLaunchSpeed(); // set launch speed from distance to hoop
      limelight.setShooterRPM(); // set flywheel RPM from necessary launch speed
      shooter.setRPMValue(limelight.getShooterRPM()); // pass RPM value to shooter subsystem
    }
  }

  @Override
  public void end(boolean interrupted) {
    limelight.turnOffLED();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
