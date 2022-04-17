package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.TopIndexer;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Handles all autonomous-phase actions
 */

public class Autonomous extends CommandBase {
  private final DriveTrain driveTrain;
  private final ClimberHooks climberHooks;
  private final ClimberArm climberArm;
  private final Shooter shooter;
  private final BottomIndexer bottomIndexer;
  private final TopIndexer topIndexer;

  private final Timer timer = new Timer();

  private AutonStages stage = AutonStages.CLIMBER_HOOKS;

  private final double MAX_POSITION = 20; // measured in motor rotations, measure later

  public Autonomous(DriveTrain driveTrain, ClimberHooks climberHooks, ClimberArm climberArm, Shooter shooter,
      BottomIndexer bottomIndexer, TopIndexer topIndexer) {

    this.driveTrain = driveTrain;
    this.climberHooks = climberHooks;
    this.climberArm = climberArm;
    this.shooter = shooter;
    this.bottomIndexer = bottomIndexer;
    this.topIndexer = topIndexer;
    timer.start();

    addRequirements(driveTrain, climberHooks, climberArm, shooter, bottomIndexer, topIndexer);
  }

  private enum AutonStages {
    CLIMBER_HOOKS,
    CLIMBER_ARM_30_AND_INGEST,
    MOVE_TO_POS,
    TURN,
    ACCELERATE_FLYWHEEL,
    SHOOT_FIRST_BALL,
    ACCELERATE_FLYWHEEL_2,
    SHOOT_SECOND_BALL,
    FINISH
  }

  @Override
  public void initialize() {
    driveTrain.resetFrontLeftEncoder();
    Gyro.resetGyroAngle();
    climberHooks.resetHookEncoder();
  }

  @Override
  public void execute() {
    // System.out.println("auton");
    if (RobotContainer.inTeleop) {
      System.out.println("in teleop");
      // cancel();
    } else {
      shooter.setRPMValue(5000);
      shooter.setShooterToRPM();
      switch (stage) {
        case CLIMBER_HOOKS:
          if (ClimberHooks.getHookEncoder() < 180) {
            climberHooks.setHookSpeed(-0.5);
          } else {
            climberHooks.setHookSpeed(0);
            stage = AutonStages.CLIMBER_ARM_30_AND_INGEST;
          }

          break;

        case CLIMBER_ARM_30_AND_INGEST:

          if (!climberArm.climberArmAngleLimitPressed()) {
            climberArm.setArmSpeed(-0.5);
          } else {
            climberArm.setArmSpeed(0);
            // System.out.println("move to pos");
            stage = AutonStages.MOVE_TO_POS;
          }

          break;

        case MOVE_TO_POS:
          if (!shooter.bottomShooterLimitPressed()) {
            bottomIndexer.setIndexSpeed(-0.4);
          } else {
            bottomIndexer.setIndexSpeed(0);
          }
          // topIndexer.setIndexSpeed(0.2);
          if (driveTrain.getFrontLeftEncoder() <= MAX_POSITION) {
            // xSpeed += drive.getAdjustment()[0];
            // System.out.println("im mooving");
            driveTrain.driveRR(-0.3, 0, 0);
          } else {
            driveTrain.drive(0, 0, 0);
            // System.out.println("shoot 1");
            timer.reset();
            // topIndexer.setIndexSpeed(0.8);
            stage = AutonStages.TURN;
          }

          break;

        case TURN:
          // System.out.println("turn " + timer.get());
          if (timer.hasElapsed(2)) {
            driveTrain.drive(0, 0, driveTrain.setAngle(190));

            if (timer.hasElapsed(5)) {
              topIndexer.setIndexSpeed(0.8);
              bottomIndexer.setIndexSpeed(0);
              driveTrain.drive(0, 0, 0);
              timer.reset();
              stage = AutonStages.ACCELERATE_FLYWHEEL;
            }
          }

          break;

        case ACCELERATE_FLYWHEEL:
          // drive.drive(0, 0, 0);
          if (timer.hasElapsed(3)) {
            bottomIndexer.setIndexSpeed(0);
            topIndexer.setIndexSpeed(0);
            stage = AutonStages.FINISH;
          } else if (timer.hasElapsed(2)) {
            bottomIndexer.setIndexSpeed(-0.8);
            // topIndexer.setIndexSpeed(0);
          } else {
            bottomIndexer.setIndexSpeed(0);
          }

          break;

        case FINISH:
          driveTrain.drive(0, 0, 0);
          shooter.setRPMValue(0);
          shooter.setShooterToRPM();
          cancel();

          break;

        default:
          driveTrain.drive(0, 0, 0);

          break;
      }
    }

  }

  @Override
  public void end(boolean interrupted) {
    driveTrain.drive(0, 0, 0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}