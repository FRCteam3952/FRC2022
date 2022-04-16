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
  private boolean bottomBallLoaded = false;
  private int numberBallShot = 0;
  private double moveBallDownSpeed = -0.3;
  private double indexSpeed = 0.15;

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
    TESTING,
    INDEX_FIRST_BALL,
    INDEX_SECOND_BALL,
    PREPARE_TO_SHOOT,
    ACCELERATE_FLYWHEEL
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
      System.out.println("reset");
    }

    // ROLLS THE BOTTOM INDEXER
    if (RobotContainer.primaryJoystick.joystick.getRawButton(Constants.rollIngesterButtonNumber) && !bottomBallLoaded) {
      bottomIndex.setIndexSpeed(ingestSpeed);
      // System.out.println("i am ingesting");
    } else if (!bottomBallLoaded) {
      bottomIndex.setIndexSpeed(0);
    }

    // SHOOTS THE BALL
    if (RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.shootBallsButtonNumber)) {
      timer.reset();
      //System.out.println("AAAAA SHOOT");
      //topIndex.setIndexSpeed(shootIndexSpeed);
      topIndex.setIndexSpeed(0);
      state = ShootingStates.ACCELERATE_FLYWHEEL;
    }

    // SWITCH STATES FOR INDEXING AND SHOOTING SEQUENCE
    switch (state) {
      case TESTING:
        shoot.setShooterToRPM();
        System.out.println(shoot.getRPMValue());
        System.out.println(shoot.getEncoderRPMValue());
        break;
      case INDEX_FIRST_BALL:
        topIndex.setIndexSpeed(indexSpeed);
        bottomBallLoaded = false;
        // System.out.println("searching for first ball");
        if (shoot.getTopShooterLim()) {
          state = ShootingStates.INDEX_SECOND_BALL;
          topIndex.setIndexSpeed(0);
          System.out.println("start search 2nd ball");
          timer.reset();
        }
        break;

      case INDEX_SECOND_BALL:
        // bottomIndex.setIndexSpeed(ingestSpeed);

        if (shoot.getBottomShooterLim() && timer.hasElapsed(0.5)) {
          bottomBallLoaded = true;
          bottomIndex.setIndexSpeed(0);
          topIndex.setIndexSpeed(0);
          state = ShootingStates.PREPARE_TO_SHOOT;
          System.out.println("i am prep shoot");
          timer.reset();
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

      case ACCELERATE_FLYWHEEL:
        // System.out.println("ACC FLYWHEEL");
        // shoot.setShooterPower(shooterPOWER);
        shoot.setRPMValue(desiredRPM);
        shoot.setShooterToRPM();
        if(shoot.getEncoderRPMValue() >= desiredRPM && numberBallShot == 0){
          topIndex.setIndexSpeed(shootIndexSpeed);
          numberBallShot++;
        }
        if (shoot.getEncoderRPMValue() >= desiredRPM && numberBallShot == 1) {
          bottomIndex.setIndexSpeed(-shootIndexSpeed);
          numberBallShot++;
          timer.reset();
        }
        if (timer.hasElapsed(0.7) && numberBallShot == 2) {
          bottomIndex.setIndexSpeed(0);
          topIndex.setIndexSpeed(0);
          numberBallShot = 0;

          shoot.setRPMValue(0);
          shoot.setShooterToRPM();

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
