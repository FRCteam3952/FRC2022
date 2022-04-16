package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;

/**
 * Automatically set shooter power.
 * Currently obsolete due to design choice to set flywheel to power of 0.85 for
 * the entire match.
 */

public class SetShooterPower extends CommandBase {
  private final DriveTrain driveTrain;
  private final Shooter shooter;
  private final Limelight limelight;

  private double launchSpeed = 0;
  private double shooterRPM = 0;

  private final double HOOP_HEIGHT = 2.6416; // in meters
  private final double HOOP_RADIUS = 0.6096; // in meters
  private final double WHEEL_RADIUS = 0.0619125; // in meters
  private final double BALL_MASS = 0.26932047; // in kilograms
  private final double WHEEL_MASS = 0.144582568 * 2; // in kilograms
  private final double GRAVITY = 9.80665; // in meters per second squared
  private final double ANGLE = 75; // degrees, measure later
  private final double SHOOTER_HEIGHT = 0.65; // in meters
  private final double MIN_DISTANCE = 2.6919; // in meters
  private final double delta = 3 - MIN_DISTANCE;

  public SetShooterPower(Shooter shooter, DriveTrain driveTrain, Limelight limey) {
    this.shooter = shooter;
    this.driveTrain = driveTrain;
    this.limelight = limey;
    addRequirements(shooter, driveTrain);

  }

  public void setLaunchSpeed() {
    limelight.turnOnLED();
    double x = limelight.getDistance() + HOOP_RADIUS;
    double y = HOOP_HEIGHT - SHOOTER_HEIGHT;
    double a = Math.toRadians(ANGLE);
    double g = GRAVITY;
    double velocity = Math.sqrt((-(g / 2) * Math.pow(x, 2)) / ((y - x * Math.tan(a)) * Math.pow(Math.cos(a), 2)));
    launchSpeed = velocity;
    limelight.turnOffLED();
  }

  public void setShooterRPM() {
    double wheelTanSpeed = 2 * launchSpeed * ((WHEEL_MASS + ((7 / 5) * BALL_MASS)) / WHEEL_MASS);
    double angularVelocity = wheelTanSpeed / WHEEL_RADIUS;
    shooterRPM = (angularVelocity * 60) / (2 * Math.PI);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    if (limelight.getDistance() + HOOP_RADIUS < MIN_DISTANCE + delta) {
      System.out.println("Robot too close to hub to shoot, backing up");
      launchSpeed = 0;
      driveTrain.driveRR(-0.5, 0, 0);
    } else {
      setLaunchSpeed(); // set launch speed from distance to hoop
      setShooterRPM(); // set flywheel RPM from necessary launch speed
      shooter.setRPMValue(shooterRPM); // pass RPM value to shooter subsystem
    }
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
