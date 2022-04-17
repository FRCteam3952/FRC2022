package frc.robot.commands;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.TopIndexer;
import frc.robot.RobotContainer;
import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;

/**
 * Handles all index/ingest/shooting code
 */

public class BallHandling extends CommandBase {
  private final Shooter shooter;
  private final BottomIndexer bottomIndexer;
  private final TopIndexer topIndexer;
  private final Timer timer = new Timer();

  private boolean bothBallsLoaded = false;

  private final double INGEST_SPEED = -0.8;
  private final double SHOOT_INDEX_SPEED = 0.8;
  private final double MOVE_BALL_DOWN_SPEED = -0.3;
  private final double INDEX_SPEED = 0.15;
  private final double DELTA = 50;

  private final boolean TESTING = false;

  // private ShootingStates state = ShootingStates.TESTING;
  private ShootingStates state = ShootingStates.INDEX_FIRST_BALL;

  public BallHandling(Shooter shooter, BottomIndexer bottomIndexer, TopIndexer topIndexer) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.bottomIndexer = bottomIndexer;
    this.topIndexer = topIndexer;

    addRequirements(shooter, bottomIndexer, topIndexer);
  }

  private enum ShootingStates {
    INDEX_FIRST_BALL,
    INDEX_SECOND_BALL,
    PREPARE_TO_SHOOT,
    SHOOT_FIRST_BALL,
    WAIT,
    SHOOT_LAST_BALL,
    RESET
  }

  @Override
  public void initialize() {
    timer.start(); // Note to future us: remember to always timer.start();
  }

  @Override
  public void execute() {
    // System.out.println(shoot.getBottomShooterLim());

    // RESETS THE INDEXING
    if (RobotContainer.secondaryJoystick.joystick.getRawButtonPressed(Constants.resetIndexerAndIngesterButtonNumber)) {
      state = ShootingStates.INDEX_FIRST_BALL;
    }

    // ROLLS THE BOTTOM INDEXER
    if (!bothBallsLoaded) {
      bottomIndexer.setIndexSpeed(
          RobotContainer.primaryJoystick.joystick.getRawButton(Constants.rollIngesterButtonNumber) ? INGEST_SPEED : 0);
    }

    // SHOOTS THE BALL
    if (RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.shootBallsButtonNumber)) {
      shooter.setShooterToRPM();
      state = bothBallsLoaded ? ShootingStates.SHOOT_FIRST_BALL : ShootingStates.SHOOT_LAST_BALL;
    }

    if (TESTING) {
      shooter.setShooterToRPM();
      System.out.println(shooter.getRPMValue());
      System.out.println(shooter.getEncoderRPMValue());
    }

    System.out.println(shooter.getRPMValue());
    System.out.println(shooter.getEncoderRPMValue());

    // System.out.println(state);
    // System.out.println("bottom: " + shooter.bottomShooterLimitPressed());
    // System.out.println("top: " + shooter.topShooterLimitPressed());

    // SWITCH STATES FOR INDEXING AND SHOOTING SEQUENCE
    switch (state) {
      case INDEX_FIRST_BALL:
        topIndexer.setIndexSpeed(INDEX_SPEED);
        bothBallsLoaded = false;

        if (shooter.topShooterLimitPressed()) {
          state = ShootingStates.INDEX_SECOND_BALL;
          topIndexer.setIndexSpeed(0);
        }

        break;

      case INDEX_SECOND_BALL:
        if (shooter.bottomShooterLimitPressed()) {
          bothBallsLoaded = true;
          bottomIndexer.setIndexSpeed(0);
          topIndexer.setIndexSpeed(0);
          timer.reset();
          state = ShootingStates.PREPARE_TO_SHOOT;
        }

        break;

      case PREPARE_TO_SHOOT:
        if (timer.hasElapsed(0.5)) {
          topIndexer.setIndexSpeed(0);
          bottomIndexer.setIndexSpeed(0);
        } else if (timer.hasElapsed(0.3)) {
          topIndexer.setIndexSpeed(MOVE_BALL_DOWN_SPEED);
          bottomIndexer.setIndexSpeed(-MOVE_BALL_DOWN_SPEED);
        }

        break;

      case SHOOT_FIRST_BALL:
        if (shooter.getEncoderRPMValue() > shooter.getRPMValue() - DELTA) {
          topIndexer.setIndexSpeed(SHOOT_INDEX_SPEED);
          timer.reset();
          state = ShootingStates.WAIT;
        }

        break;

      case WAIT:
        if (timer.hasElapsed(0.35)) {
          state = ShootingStates.SHOOT_LAST_BALL;
        }

        break;

      case SHOOT_LAST_BALL:
        // if (shooter.getEncoderRPMValue() > shooter.getRPMValue() - DELTA) {
          topIndexer.setIndexSpeed(SHOOT_INDEX_SPEED);
          bottomIndexer.setIndexSpeed(-SHOOT_INDEX_SPEED);
          System.out.println("shoot second ball");
          timer.reset();
          state = ShootingStates.RESET;
        // }

        break;

      case RESET:
        if (timer.hasElapsed(1)) {
          bottomIndexer.setIndexSpeed(0);
          topIndexer.setIndexSpeed(0);
          shooter.stopShooter();
          state = ShootingStates.INDEX_FIRST_BALL;
        }

        break;

      default:
        System.err.println("No state is true");
        break;
    }

  }

  public void resetSwitchState() {
    state = ShootingStates.INDEX_FIRST_BALL;
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
