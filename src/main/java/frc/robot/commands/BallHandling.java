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

  private double ingestSpeed = -1;
  private double shootIndexSpeed = 0.8;
  public double desiredRPM = 1000;
  public boolean previousLimitState;
  private boolean bottomBallLoaded = false;

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
    //System.out.println(shoot.getBottomShooterLim());
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
        bottomBallLoaded = false;
        shoot.setRPMValue(0);
        shoot.setShooterToRPM();
        // System.out.println("searching for first ball");
        if (shoot.getBottomShooterLim()) {
          state = ShootingStates.INDEX_SECOND_BALL;
          topIndex.setIndexSpeed(-0.1);
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
          timer.reset();
          state = ShootingStates.PREPARE_TO_SHOOT;
          System.out.println("i am prep shoot");
        }
        break;

      case PREPARE_TO_SHOOT:
        timer.reset();
        break;

      case ACCELERATE_FLYWHEEL:
        // System.out.println("ACC FLYWHEEL");
        // shoot.setShooterPower(shooterPOWER);
        shoot.setRPMValue(5000);
        shoot.setShooterToRPM();
        if(timer.hasElapsed(0.5)){
          topIndex.setIndexSpeed(shootIndexSpeed);
        }
        if (timer.hasElapsed(1)) {
          bottomIndex.setIndexSpeed(-shootIndexSpeed);
          // topIndex.setIndexSpeed(0);

        }
        if (timer.hasElapsed(2.5)) {
          bottomIndex.setIndexSpeed(0);
          topIndex.setIndexSpeed(0);
          state = ShootingStates.INDEX_FIRST_BALL;
        }
        /*
         * currentRPM = Tachometer.getShooterRPM();
         * System.out.println(currentRPM);
         * desiredRPM = shoot.getRPMValue();
         * //shoot.setShooterToRPM();
         * shoot.setShooterPower(0.8);
         * if (currentRPM >= desiredRPM - delta1 && currentRPM <= desiredRPM + delta2) {
         * // if(currentRPM >= desiredRPM) {
         * 
         * state = ShootingStates.SHOOT_FIRST_BALL;
         * }
         */

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
