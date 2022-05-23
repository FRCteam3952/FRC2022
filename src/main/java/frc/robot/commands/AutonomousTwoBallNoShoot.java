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
 * ingests a second ball along-
 * side the pre-load. Does not shoot.
 */

public class AutonomousTwoBallNoShoot extends CommandBase {
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

  public AutonomousTwoBallNoShoot(DriveTrain driveTrain, ClimberHooks climberHooks, ClimberArm climberArm, Shooter shooter,
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
          // topIndexer.setIndexSpeed(0);
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
          if (timer.hasElapsed(2)) {
            driveTrain.drive(0, 0, driveTrain.setAngle(190));

            if (timer.hasElapsed(5)) {
              driveTrain.drive(0, 0, 0);
              timer.reset();
              //RobotContainer.setShooterPower.schedule();
              // shooter.setShooterToRPM();
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
              //shooter.setRPMValue(4000);
              //shooter.setShooterToRPM();
              //limelight.turnOffLED();
              stage = AutonStages.FINISH;
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
          System.err.println("no case is true");

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