package frc.robot.commands;

import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class AutonomousShootBall extends CommandBase {
  /**
   * Creates a new AutonomousCommand.
   */
  private final DriveTrain drive;
  private final ClimberHooks climber;
  private final ClimberArm arm;
  private final Shooter shooter;
  private final Timer timer = new Timer();

  private final double SHOOTER_SPEED = 0.2;
  private final double INDEX_SPEED = -0.3;
  private final double SLIDE_HOOK_SPEED = .4;


  public AutonomousShootBall(DriveTrain drive, ClimberHooks climber, ClimberArm arm, Shooter shooter) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.drive = drive;
    this.climber = climber;
    this.arm = arm;
    this.shooter = shooter;
    addRequirements(drive, climber, arm, shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    timer.reset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}