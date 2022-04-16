package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.TopIndexer;
import frc.robot.RobotContainer;
import frc.robot.Constants;

/**
 * Handles all index/ingest/shooting code
 */

public class BallHandling extends CommandBase {
  private final Shooter shoot;
  private final BottomIndexer bottomIndex;
  private final TopIndexer topIndex;
  private final Timer timer = new Timer();

  private double ingestSpeed = -0.8;
  private double shootIndexSpeed = 0.8;
  public double desiredRPM = 5000;
  public boolean previousLimitState;
  private boolean bothBallsLoaded = false;
  private double moveBallDownSpeed = -0.35;
  private double indexSpeed = 0.15;
  private double delta = 50;
  private boolean testing = false;

  // private ShootingStates state = ShootingStates.TESTING;
  private ShootingStates state = ShootingStates.INDEX_FIRST_BALL;

  public BallHandling(Shooter shoot, BottomIndexer bottomIndex, TopIndexer topIndex) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shoot = shoot;
    this.bottomIndex = bottomIndex;
    this.topIndex = topIndex;
    addRequirements(shoot, bottomIndex, topIndex);
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
      bottomIndex.setIndexSpeed(RobotContainer.primaryJoystick.joystick.getRawButton(Constants.rollIngesterButtonNumber) ? ingestSpeed : 0);
    }

    // SHOOTS THE BALL
    if (RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.shootBallsButtonNumber)) {
      shoot.setShooterToRPM();
      state = bothBallsLoaded ? ShootingStates.SHOOT_FIRST_BALL : ShootingStates.SHOOT_LAST_BALL;
    }

    if (testing) {
      shoot.setRPMValue(5000);
      shoot.setShooterToRPM();
      System.out.println(shoot.getRPMValue());
      System.out.println(shoot.getEncoderRPMValue());
    }

    // SWITCH STATES FOR INDEXING AND SHOOTING SEQUENCE
    switch (state) {
      case INDEX_FIRST_BALL:
        shoot.setRPMValue(0);
        shoot.setShooterToRPM();
        topIndex.setIndexSpeed(indexSpeed);
        bothBallsLoaded = false;
        if (shoot.getTopShooterLim()) {
          state = ShootingStates.INDEX_SECOND_BALL;
          topIndex.setIndexSpeed(0);
        }
        break;

      case INDEX_SECOND_BALL:
        if (shoot.getBottomShooterLim()) {
          bothBallsLoaded = true;
          bottomIndex.setIndexSpeed(0);
          topIndex.setIndexSpeed(0);
          timer.reset();
          state = ShootingStates.PREPARE_TO_SHOOT;
        }
        break;

      case PREPARE_TO_SHOOT:
        if(timer.hasElapsed(0.2)) {
          topIndex.setIndexSpeed(0);
          bottomIndex.setIndexSpeed(0);
        } else {
          topIndex.setIndexSpeed(moveBallDownSpeed);
          bottomIndex.setIndexSpeed(-moveBallDownSpeed);
        }
        
        break;

      case SHOOT_FIRST_BALL:
        if (shoot.getEncoderRPMValue() > shoot.getRPMValue() - delta) {
          topIndex.setIndexSpeed(shootIndexSpeed);
          timer.reset();
          state = ShootingStates.WAIT;
        }
        break;

      case WAIT:
        if (timer.hasElapsed(0.346)) {
          state = ShootingStates.SHOOT_LAST_BALL;
        }
        break;
        
      case SHOOT_LAST_BALL:
      if (shoot.getEncoderRPMValue() > shoot.getRPMValue() - delta) {        
        topIndex.setIndexSpeed(shootIndexSpeed);
        bottomIndex.setIndexSpeed(-shootIndexSpeed);
        System.out.println("shoot second ball");
        timer.reset();
        state = ShootingStates.RESET;
      }
        break;
      
      case RESET:
        if (timer.hasElapsed(1)) {
          bottomIndex.setIndexSpeed(0);
          topIndex.setIndexSpeed(0);
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
