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
 * shoots the 2 balls that are pre-loaded (Beach Blitz changes),
 * then turns another 180 and tries to grab a third ball
 * <p>
 * This should hopefully be superseded by {@link frc.robot.commands.AutonomousThreeBall}
 */

public class AutonomousTwoBallPreLoaded extends CommandBase {
  private final DriveTrain driveTrain;
  private final ClimberHooks climberHooks;
  private final ClimberArm climberArm;
  private final Shooter shooter;
  private final BottomIndexer bottomIndexer;
  private final TopIndexer topIndexer;
  private final Limelight limelight;

  private final Timer timer = new Timer();

  private AutonStages stage = AutonStages.CLIMBER_HOOKS_ARMS;

  private static final double MAX_POSITION = 25; // measured in motor rotations, measure later
  private static final double GET_NEXT_BALL_ADD_ON_ROTATIONS = 5;

  public AutonomousTwoBallPreLoaded(DriveTrain driveTrain, ClimberHooks climberHooks, ClimberArm climberArm, Shooter shooter,
      BottomIndexer bottomIndexer, TopIndexer topIndexer, Limelight limelight) {

    this.driveTrain = driveTrain;
    this.climberHooks = climberHooks;
    this.climberArm = climberArm;
    this.shooter = shooter;
    this.bottomIndexer = bottomIndexer;
    this.topIndexer = topIndexer;
    this.limelight = limelight;
    addRequirements(driveTrain, climberHooks, climberArm, shooter, bottomIndexer, topIndexer, limelight);
  }

  private enum AutonStages {
    CLIMBER_HOOKS_ARMS,
    MOVE_TO_POS,
    TURN,
    AIM,
    LOWER_BALLS,
    SHOOT_FIRST_BALL,
    SHOOT_SECOND_BALL,
    TURN_FOR_THIRD_BALL,
    GET_THIRD_BALL,
    PREPARE_AIM_THIRD_BALL,
    FINISH
  }

  @Override
  public void initialize() {
    driveTrain.resetFrontLeftEncoder();
    Gyro.resetGyroAngle();
    climberHooks.resetHookEncoder();
    timer.start();
  }

  @Override
  public void execute() {

    if (RobotContainer.inTeleop) {
      System.out.println("in teleop");
      // cancel();
    } else {
      
      switch (stage) {
        case CLIMBER_HOOKS_ARMS:
          // topIndexer.setIndexSpeed(0);
          if (ClimberHooks.getHookEncoder() < 180) {
            climberHooks.setHookSpeed(-1);
          } else {
            climberHooks.setHookSpeed(0);
          }

          if (!climberArm.climberArmAngleLimitPressed() && ClimberHooks.getHookEncoder() > 90) {
            climberArm.setArmSpeed(-1);
          } else if (ClimberHooks.getHookEncoder() > 90){
            climberArm.setArmSpeed(0);
            climberArm.resetArmAngleEncoder();
            stage = AutonStages.MOVE_TO_POS;
          }
          


          break;

        case MOVE_TO_POS:
          if (!shooter.bottomShooterLimitPressed()) {
            bottomIndexer.setIndexSpeed(-0.8);
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
          if (timer.hasElapsed(1)) {
            driveTrain.drive(0, 0, driveTrain.findZRotationSpeedFromAngle(190));
          }
          if (timer.hasElapsed(3)) {
            driveTrain.drive(0, 0, 0);
            timer.reset();
            limelight.turnOnLED();
            stage = AutonStages.LOWER_BALLS;
          }
        break;

        case AIM:
          driveTrain.drive(0, 0, limelight.getAdjustment());
          if(timer.hasElapsed(3)){
            limelight.turnOffLED();
            stage = AutonStages.LOWER_BALLS;
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
            shooter.setRPMValue(4000);
            shooter.setShooterToRPM();
            stage = AutonStages.SHOOT_FIRST_BALL;
          }

          break;

          case SHOOT_FIRST_BALL:
          if (shooter.getEncoderRPMValue() > shooter.getTargetRPMValue() - 50) {
            topIndexer.setIndexSpeed(0.8);
            timer.reset();
            stage = AutonStages.SHOOT_SECOND_BALL;
          }
  
          break;
          
        case SHOOT_SECOND_BALL:
          if (timer.hasElapsed(1)) {
            bottomIndexer.setIndexSpeed(-0.8);
            System.out.println("shoot second ball");
            timer.reset();
            stage = AutonStages.TURN_FOR_THIRD_BALL;
          }
          break;

        case TURN_FOR_THIRD_BALL:
          if(timer.hasElapsed(1)) {
            driveTrain.drive(0, 0, driveTrain.findZRotationSpeedFromAngle(190));
          }
          if(timer.hasElapsed(3)) {
            driveTrain.drive(0, 0, 0);
            timer.reset();
            limelight.turnOnLED();
            stage = AutonStages.GET_THIRD_BALL;
          }
          break;

        case GET_THIRD_BALL:
          if(!shooter.bottomShooterLimitPressed()) {
            bottomIndexer.setIndexSpeed(-0.8);
          }
          else {
            bottomIndexer.setIndexSpeed(0);
            stage = AutonStages.PREPARE_AIM_THIRD_BALL;
          }
          if(driveTrain.getFrontLeftEncoder() <= MAX_POSITION + GET_NEXT_BALL_ADD_ON_ROTATIONS) {
            driveTrain.driveRR(-0.3, 0, 0);
          }
          else {
            driveTrain.drive(0, 0, 0);
            timer.reset();
            stage = AutonStages.PREPARE_AIM_THIRD_BALL;
          }
          break;
        case PREPARE_AIM_THIRD_BALL:
            if(timer.hasElapsed(1)) {
                driveTrain.drive(0, 0, driveTrain.findZRotationSpeedFromAngle(190));
            }

          if (timer.hasElapsed(3)) {
            driveTrain.drive(0, 0, 0);
            //timer.reset();
            //limelight.turnOnLED();
            stage = AutonStages.FINISH;
          }
          break;
        case FINISH:
          //if (timer.hasElapsed(2)) {
            driveTrain.drive(0, 0, 0);
            topIndexer.setIndexSpeed(0);
            bottomIndexer.setIndexSpeed(0);
            shooter.setRPMValue(0);
            shooter.setShooterToRPM();
            //cancel();
          //}
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