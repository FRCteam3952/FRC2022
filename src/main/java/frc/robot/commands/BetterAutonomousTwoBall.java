package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.commands.BetterBallHandling.ShootingStates;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;

public class BetterAutonomousTwoBall extends CommandBase {
    private final DriveTrain driveTrain;
  private final ClimberHooks climberHooks;
  private final ClimberArm climberArm;
  private final Limelight limelight;
  private final Shooter shooter;

  private final Timer timer = new Timer();

  private double defaultRPM = 4000;

  private static enum AutonStages {
    CLIMBER_HOOKS_ARMS,
    CLIMBER_ARM_30_AND_INGEST,
    MOVE_TO_POS,
    TURN,
    AIM,
    SHOOT_BALLS,
    FINISH
  }


  private AutonStages stage = AutonStages.CLIMBER_HOOKS_ARMS;

  private final double MAX_POSITION = 30; // measured in motor rotations, measure later

  public BetterAutonomousTwoBall(DriveTrain driveTrain, ClimberHooks climberHooks, ClimberArm climberArm, Limelight limelight, Shooter shooter) {

    this.driveTrain = driveTrain;
    this.climberHooks = climberHooks;
    this.climberArm = climberArm;
    this.limelight = limelight;
    this.shooter = shooter;
    addRequirements(driveTrain, climberHooks, climberArm, limelight);
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
          //RobotContainer.secondaryJoystick.setOverrideButtonPressShooterValue(true);
          // topIndexer.setIndexSpeed(0.2);
          if (driveTrain.getFrontLeftEncoder() <= MAX_POSITION) {
            // xSpeed += drive.getAdjustment()[0];
            // System.out.println("im mooving");
            driveTrain.driveRR(-0.3, 0, 0);
          } else {
            driveTrain.drive(0, 0, 0);
            //RobotContainer.secondaryJoystick.setOverrideButtonPressShooterValue(false);
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
            stage = AutonStages.AIM;
          }
        break;

        case AIM:
          driveTrain.drive(0, 0, limelight.getAdjustment());
          if(timer.hasElapsed(3)){
            // limelight.setLaunchSpeed(); // set launch speed from distance to hoop
            // limelight.setShooterRPM(); // set flywheel RPM from necessary launch speed
            shooter.setRPMValue(defaultRPM); // pass RPM value to shooter subsystem
            shooter.setShooterToRPM();
            stage = AutonStages.SHOOT_BALLS;
            RobotContainer.betterBallHandling.setState(ShootingStates.SHOOT_FIRST_BALL);

          }
        break;

        case SHOOT_BALLS:
          
          break;

        case FINISH:
          if (timer.hasElapsed(2)) {
            driveTrain.drive(0, 0, 0);
            //cancel();
          }
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
