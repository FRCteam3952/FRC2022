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
  private double indexSpeed = 0.2;
  private double shootIndexSpeed = 0.8;
  /**
   * allowed variance of RPM lower threshold (in case of PID inaccuracy),
   * and upper (in case of tachometer RPM spikes)
   */
  private double delta1 = 50;
  private double delta2 = 50;
  public double desiredRPM = 1000;
  public boolean previousLimitState;
  private boolean bothBallsLoaded = false;
  private boolean testing = false;

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
    SHOOT_LAST_BALL,
    RESET
  }

  @Override
  public void initialize() {
    timer.start(); // Note to future us: remember to always timer.start();
  }

  @Override
  public void execute() {
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
      state = bothBallsLoaded ? ShootingStates.SHOOT_FIRST_BALL : ShootingStates.SHOOT_LAST_BALL;
    }

    shoot.setShooterToRPM();

    if (testing) {
      System.out.println("Desired RPM: " + shoot.getRPMValue());
      System.out.println("Measured RPM: " + shoot.getEncoderRPMValue());
    }

    // SWITCH STATES FOR INDEXING AND SHOOTING SEQUENCE
    switch (state) {

      case INDEX_FIRST_BALL:
        bothBallsLoaded = false;
        shoot.setRPMValue(0);
        topIndex.setIndexSpeed(indexSpeed);
        if (shoot.getTopShooterLim()) {
          topIndex.setIndexSpeed(0);
          state = ShootingStates.INDEX_SECOND_BALL;
        }
        break;

      case INDEX_SECOND_BALL:
        if (shoot.getBottomShooterLim()) {
          bothBallsLoaded = true;
          bottomIndex.setIndexSpeed(0);
          state = ShootingStates.PREPARE_TO_SHOOT;
        }
        break;

      case PREPARE_TO_SHOOT:
        // waiting for driver to set RPM using either SetShooterPowerManual or SetShooterPower
        break;
      
      case SHOOT_FIRST_BALL:
        if (shoot.getEncoderRPMValue() > shoot.getRPMValue() - delta1 && shoot.getEncoderRPMValue() < shoot.getRPMValue() + delta2) {
          topIndex.setIndexSpeed(shootIndexSpeed);
          state = ShootingStates.SHOOT_LAST_BALL;
        }
        break;

      case SHOOT_LAST_BALL:
        if (shoot.getEncoderRPMValue() > shoot.getRPMValue() - delta1 && shoot.getEncoderRPMValue() < shoot.getRPMValue() + delta2) {
          topIndex.setIndexSpeed(shootIndexSpeed);
          bottomIndex.setIndexSpeed(-shootIndexSpeed);
          timer.reset();
          state = ShootingStates.RESET;
        }
        break;
      
      case RESET:
        if (timer.hasElapsed(2)) {
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


  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
