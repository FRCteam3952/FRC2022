package frc.robot.commands;

import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Tachometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.IngesterPositioner;

public class AutonomousDriveToBall extends CommandBase {
  /**
   * Creates a new AutonomousCommand.
   */
  private final DriveTrain drive;
  private final ClimberHooks climber;
  private final ClimberArm arm;
  private final IngesterPositioner ingester;
  private final Shooter shooter;
  private final Tachometer tacheo;
  private final Timer timer = new Timer();

  private final double SHOOTER_SPEED = 0.2;
  private final double INDEX_SPEED = -0.3;
  private final double SLIDE_HOOK_SPEED = .4;
  private double xSpeed= 0;
  private double ySpeed= 0; 
  private double zRotation=0;
  private final double MAX_POSITION = 50; //measured in motor rotations, measure later


  public AutonomousDriveToBall(DriveTrain drive, ClimberHooks climber, ClimberArm arm, IngesterPositioner ingester, Shooter shooter,
      Tachometer tacheo) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.drive = drive;
    this.climber = climber;
    this.arm = arm;
    this.ingester = ingester;
    this.shooter = shooter;
    this.tacheo = tacheo;
    addRequirements(drive, climber, arm, ingester, shooter, tacheo);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ySpeed = 0.5;
    xSpeed = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (drive.getPosition() <= MAX_POSITION) {
      xSpeed += drive.getAdjustment()[0];
      ySpeed += drive.getAdjustment()[1];
      drive.drive(ySpeed, xSpeed, 0);
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