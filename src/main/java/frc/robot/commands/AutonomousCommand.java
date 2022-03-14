package frc.robot.commands;

import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.IngesterPositioner;

public class AutonomousCommand extends CommandBase {
  /**
   * Creates a new AutonomousCommand.
   */
  private final DriveTrain drive;
  private final ClimberHooks climber;
  private final ClimberArm arm;
  private final IngesterPositioner ingester;
  private final Shooter shooter;
  private final Indexer indexer;
  private final Timer timer = new Timer();
  private STATES state = STATES.SlideHookToTop;

  private final double SHOOTER_SPEED = 0.2;
  private final double INDEX_SPEED = -0.3;
  private final double SLIDE_HOOK_SPEED = .4;

  public enum STATES {
    SlideHookToTop,
    Halt
  }

  public AutonomousCommand(DriveTrain drive, ClimberHooks climber, ClimberArm arm, IngesterPositioner ingester, Shooter shooter, Indexer indexer) {
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
    shooter.setShooterSpeed(0.8);
    System.out.println(indexer.getShooterRPM());
    // shooter.setShooterSpeed(.3);
    if (!timer.hasElapsed(1.25)) {
      //arm.changeArmAngle(-.4);
      drive.drive(.6, 0);
    } else {
      //arm.changeArmAngle(0);
      drive.drive(0, 0);
    }
    //if (!timer.hasElapsed(3))
      //ingester.changeIngestAngle(-.5);
    //else
      //ingester.changeIngestAngle(0);
    
    // if (timer.hasElapsed(3) && !timer.hasElapsed(10))
    //   shooter.setShooterSpeed(SHOOTER_SPEED);
    // else 
    //   shooter.setShooterSpeed(0);
    // if (timer.hasElapsed(8) && !timer.hasElapsed(10))
    //   indexer.setIndexSpeed(INDEX_SPEED);
    // else 
    //   indexer.setIndexSpeed(0);

    /* switch (state) {
      case SlideHookToTop:
        climber.slideHook(SLIDE_HOOK_SPEED);
      
        if (climber.topLimitPressed())
          state = STATES.Halt;
        break;
      default:
        climber.slideHook(0);
    } */
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