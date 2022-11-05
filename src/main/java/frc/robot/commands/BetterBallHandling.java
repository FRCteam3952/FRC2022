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

public class BetterBallHandling extends CommandBase {
  private final Shooter shooter;
  private final BottomIndexer bottomIndexer;
  private final TopIndexer topIndexer;
  private final Timer timer = new Timer();

  private boolean bothBallsLoaded = false;

  private static final double INGEST_SPEED = -0.7;
  private static final double SHOOT_INDEX_SPEED = 0.8;
  public static final double MOVE_BALL_DOWN_SPEED = -0.2;
  private static final double INDEX_SPEED = 0.15;
  private static final double DELTA = 500;

  private static final boolean TESTING = false;

  // private ShootingStates state = ShootingStates.TESTING;
  private ShootingStates state = ShootingStates.WAITING_FOR_SHOOT;

  public BetterBallHandling(Shooter shooter, BottomIndexer bottomIndexer, TopIndexer topIndexer) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.bottomIndexer = bottomIndexer;
    this.topIndexer = topIndexer;

    addRequirements(shooter, bottomIndexer, topIndexer);
  }

  private enum ShootingStates {
    WAITING_FOR_SHOOT,
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
    System.out.println("balls loaded: " + this.bothBallsLoaded + ", state: " + this.state);
    // System.out.println(shoot.getBottomShooterLim());

    // RESETS THE INDEXIN
    if (RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.setToLowerHubRPMButtonNumber)) {
      shooter.setRPMValue(1675);
    }

    if (RobotContainer.secondaryJoystick.joystick.getRawButtonPressed(Constants.resetIndexerAndIngesterButtonNumber)) {
      state = ShootingStates.RESET;
    }    

    // SHOOTS THE BALL
    if (RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.shootBallsButtonNumber)) {
      shooter.setShooterToRPM();
      state = true ? ShootingStates.SHOOT_FIRST_BALL : ShootingStates.SHOOT_LAST_BALL;
    }
    
    // CONTROLS BALL INGESTING
    if (RobotContainer.primaryJoystick.joystick.getRawButton(Constants.spitBothBallButtonNumber)) { //SPITS BOTH BALLS
        this.setBottomIndexerSpeed(1);
      topIndexer.setIndexSpeed(-1);
      state = ShootingStates.WAITING_FOR_SHOOT;
    }
    else{
      if (RobotContainer.primaryJoystick.joystick.getRawButton(Constants.spitBottomBallButtonNumber)) { //SPITS THE BOTTOM BALL
        bottomIndexer.setIndexSpeed(1);
        state = ShootingStates.WAITING_FOR_SHOOT;
      } else if (!this.shooter.topShooterLimitPressed()) { //ROLLS THE BOTTOM INDEXER
        this.topIndexer.setIndexSpeed(0);
        this.setBottomIndexerSpeed(
            RobotContainer.primaryJoystick.joystick.getRawButton(Constants.rollIngesterButtonNumber) ? INGEST_SPEED : 0);
      } else {
          System.out.println("top shooter lim pressed");
          this.topIndexer.setIndexSpeed(MOVE_BALL_DOWN_SPEED);
          this.bottomIndexer.setIndexSpeed(0);
      }
      if (TESTING) {
        shooter.setShooterToRPM();
        System.out.println(shooter.getTargetRPMValue());
        System.out.println(shooter.getEncoderRPMValue());
      }
  
      // SWITCH STATES FOR INDEXING AND SHOOTING SEQUENCE

    }
  }

  private void setBottomIndexerSpeed(double speed) {
    if(!this.shooter.topShooterLimitPressed()) {
        bottomIndexer.setIndexSpeed(speed);
    }
  }

  public void resetSwitchState() {
    state = ShootingStates.WAITING_FOR_SHOOT;
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
