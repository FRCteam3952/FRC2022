package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.TopIndexer;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Limelight;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Autonomous phase actions - taxis (face away + 180), 
 * shoots one ball
 */

public class AutonomousOneBall extends CommandBase {
  private final DriveTrain driveTrain;
  private final ClimberHooks climberHooks;
  private final ClimberArm climberArm;
  private final Shooter shooter;
  private final BottomIndexer bottomIndexer;
  private final TopIndexer topIndexer;
  private final Limelight limelight;

  private final Timer timer = new Timer();

  private AutonStages stage = AutonStages.CLIMBER_HOOKS;

  private final double MAX_POSITION = 30; // measured in motor rotations, measure later

  public AutonomousOneBall(DriveTrain driveTrain, ClimberHooks climberHooks, ClimberArm climberArm, Shooter shooter,
      BottomIndexer bottomIndexer, TopIndexer topIndexer, Limelight limelight) {

    this.driveTrain = driveTrain;
    this.climberHooks = climberHooks;
    this.climberArm = climberArm;
    this.shooter = shooter;
    this.bottomIndexer = bottomIndexer;
    this.topIndexer = topIndexer;
    this.limelight = limelight;
    timer.start();

    addRequirements(driveTrain, climberHooks, climberArm, shooter, bottomIndexer, topIndexer, limelight);
  }

  private enum AutonStages {
    CLIMBER_HOOKS,
    CLIMBER_ARM_30_AND_INGEST,
    MOVE_TO_POS,
    TURN,
    LOWER_BALLS,
    PREPARE_TO_SHOOT,
    SHOOT_FIRST_BALL,
    WAIT,
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
      
      switch (stage) {
        case CLIMBER_HOOKS:
          if (ClimberHooks.getHookEncoder() < 180) {
            climberHooks.setHookSpeed(-1);
          } else {
            climberHooks.setHookSpeed(0);
            stage = AutonStages.CLIMBER_ARM_30_AND_INGEST;
          }

          break;

        case CLIMBER_ARM_30_AND_INGEST:

          if (!climberArm.climberArmAngleLimitPressed()) {
            climberArm.setArmSpeed(-1);
          } else {
            climberArm.setArmSpeed(0);
            // System.out.println("move to pos");
            stage = AutonStages.MOVE_TO_POS;
          }

          break;

        case MOVE_TO_POS:
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
            driveTrain.drive(0, 0, driveTrain.findZRotationSpeedFromAngle(190));

            if (timer.hasElapsed(5)) {
              driveTrain.drive(0, 0, 0);
              timer.reset();
              stage = AutonStages.LOWER_BALLS;
            }
          }

          break;

        case LOWER_BALLS:
          if(shooter.topShooterLimitPressed()){
            topIndexer.setIndexSpeed(-0.5);
            bottomIndexer.setIndexSpeed(0.5);
          }
          else{
            topIndexer.setIndexSpeed(0);
            bottomIndexer.setIndexSpeed(0);
            //limelight.turnOnLED();
            //limelight.setLaunchSpeed(); // set launch speed from distance to hoop
            //limelight.setShooterRPM(); // set flywheel RPM from necessary launch speed
            //shooter.setRPMValue(limelight.getShooterRPM()); // pass RPM value to shooter subsystem
            shooter.setRPMValue(4000);
            shooter.setShooterToRPM();
            //limelight.turnOffLED();
            stage = AutonStages.SHOOT_FIRST_BALL;

          }
          
          break;

        case SHOOT_FIRST_BALL:
          if (shooter.getEncoderRPMValue() > shooter.getTargetRPMValue() - 50) {
            topIndexer.setIndexSpeed(0.8);
            timer.reset();
            stage = AutonStages.WAIT;
          }
  
          break;
  
        case WAIT:
          if (timer.hasElapsed(1)) {
            stage = AutonStages.SHOOT_SECOND_BALL;
          }
  
          break;
  
        case SHOOT_SECOND_BALL:
          // if (shooter.getEncoderRPMValue() > shooter.getRPMValue() - DELTA) {
            topIndexer.setIndexSpeed(0.8);
            bottomIndexer.setIndexSpeed(-0.8);
            System.out.println("shoot second ball");
            timer.reset();
            stage = AutonStages.FINISH;
          // }
  
          break;

        case FINISH:
          driveTrain.drive(0, 0, 0);
          shooter.setRPMValue(0);
          shooter.setShooterToRPM();
          topIndexer.setIndexSpeed(0);
          bottomIndexer.setIndexSpeed(0);
          cancel();

          break;

        default:
          driveTrain.drive(0, 0, 0);
          System.out.println("no case tru");

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