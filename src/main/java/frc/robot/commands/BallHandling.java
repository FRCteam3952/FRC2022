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

  private static final double INGEST_SPEED = -0.7;
  private static final double SHOOT_INDEX_SPEED = 0.8;
  public static final double MOVE_BALL_DOWN_SPEED = -0.2;
  private static final double INDEX_SPEED = 0.15;
  private static final double DELTA = 500;

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
    System.out.println("balls loaded: " + this.bothBallsLoaded + ", state: " + this.state);
    // System.out.println(shoot.getBottomShooterLim());

    // RESETS THE INDEXIN
  
    if (RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.setToTarmacRPMButtonNumber)) {
      shooter.setRPMValue(1675);
    }

    if (RobotContainer.secondaryJoystick.joystick.getRawButtonPressed(Constants.resetIndexerAndIngesterButtonNumber)) {
      state = ShootingStates.RESET;
    }    

    // SHOOTS THE BALL
    if (RobotContainer.secondaryJoystick.joystick.getRawButtonPressed(Constants.shootBallsButtonNumber)) {
      shooter.setShooterToRPM();
      state = ShootingStates.SHOOT_FIRST_BALL;
    }
    
    // CONTROLS BALL INGESTING
    if (RobotContainer.primaryJoystick.joystick.getRawButton(Constants.spitBothBallButtonNumber)) { //SPITS BOTH BALLS
      bottomIndexer.setIndexSpeed(1);
      topIndexer.setIndexSpeed(-1);
      state = ShootingStates.INDEX_FIRST_BALL;
    }
    else{
      if (RobotContainer.primaryJoystick.joystick.getRawButton(Constants.spitBottomBallButtonNumber)) { //SPITS THE BOTTOM BALL
        bottomIndexer.setIndexSpeed(1);
        state = ShootingStates.INDEX_FIRST_BALL;
      } else { //ROLLS THE BOTTOM INDEXER
        bottomIndexer.setIndexSpeed(
            RobotContainer.primaryJoystick.joystick.getRawButton(Constants.rollIngesterButtonNumber) ? INGEST_SPEED : 0);
      }
      if (TESTING) {
        shooter.setShooterToRPM();
        System.out.println(shooter.getTargetRPMValue());
        System.out.println(shooter.getEncoderRPMValue());
      }
  
      //System.out.println(shooter.getRPMValue());
      //System.out.println(shooter.getEncoderRPMValue());
  
      // .out.println(state);
      // System.out.println("bottom: " + shooter.bottomShooterLimitPressed());
      // System.out.println("top: " + shooter.topShooterLimitPressed());
  
      // SWITCH STATES FOR INDEXING AND SHOOTING SEQUENCE
      switch (state) {
        case INDEX_FIRST_BALL:
          topIndexer.setIndexSpeed(INDEX_SPEED);
          bothBallsLoaded = false;
  
          if (shooter.topShooterLimitPressed()) {
            topIndexer.setIndexSpeed(0);
            state = ShootingStates.INDEX_SECOND_BALL;
          }
  
          break;
  
        case INDEX_SECOND_BALL:
          if (true) {
            // bothBallsLoaded = true;
            // bottomIndexer.setIndexSpeed(0);
            // topIndexer.setIndexSpeed(0);
            timer.reset();
            state = ShootingStates.PREPARE_TO_SHOOT;
          }
  
          break;
  
        case PREPARE_TO_SHOOT:
          if (timer.hasElapsed(0.5)) {
            topIndexer.setIndexSpeed(0);
            // bottomIndexer.setIndexSpeed(0);
          } else if (timer.hasElapsed(0.3)) {
            //topIndexer.setIndexSpeed(MOVE_BALL_DOWN_SPEED);
            //bottomIndexer.setIndexSpeed(-MOVE_BALL_DOWN_SPEED);
          }
  
          break;
  
        case SHOOT_FIRST_BALL:
          if (shooter.getEncoderRPMValue() > shooter.getTargetRPMValue() - DELTA) {
            topIndexer.setIndexSpeed(SHOOT_INDEX_SPEED);
            timer.reset();
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
