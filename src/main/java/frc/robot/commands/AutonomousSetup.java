package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Tachometer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.IngesterPositioner;
import frc.robot.subsystems.Tachometer;

public class AutonomousSetup extends CommandBase {
  /**
   * Creates a new AutonomousCommand.
   */
  private final DriveTrain drive;
  private final ClimberHooks climber;
  private final ClimberArm arm;
  private final IngesterPositioner ingester;
  private final Shooter shooter;
  private final Tachometer indexer;
  private final Timer timer = new Timer();
  private STATES state = STATES.SlideHookToTop;

  private final double SHOOTER_SPEED = 0.2;
  private final double INDEX_SPEED = -0.3;
  private final double SLIDE_HOOK_SPEED = .4;

  public enum STATES {
    SlideHookToTop,
    Halt
  }

  public AutonomousSetup(DriveTrain drive, ClimberHooks climber, ClimberArm arm, IngesterPositioner ingester, Shooter shooter,
      Tachometer indexer) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.drive = drive;
    this.climber = climber;
    this.arm = arm;
    this.ingester = ingester;
    this.shooter = shooter;
    this.indexer = indexer;
    addRequirements(drive, climber, arm, ingester, shooter, indexer);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!timer.hasElapsed(3)) {
      drive.drive(.8 * (timer.get()/ 5), 0, 0);
    } else {
      drive.drive(0, 0, 0);
    }


    // if (!timer.hasElapsed(3)) {
    //   ingester.changeIngestAngle(-.5);
    // } else {
    //   ingester.changeIngestAngle(0);
    // }

    
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