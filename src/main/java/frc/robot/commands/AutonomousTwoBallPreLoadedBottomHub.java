package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;

/**
 * Autonomous phase actions -
 * (Starts facing the middle, right against the middle with 2 preloads)
 * Backs up slightly
 * Arm + Hook
 * Shoot 2 balls
 * Run away to get taxi points
 */
public class AutonomousTwoBallPreLoadedBottomHub extends CommandBase {
    private final DriveTrain driveTrain;
    private final ClimberHooks climberHooks;
    private final ClimberArm climberArm;
    private final Shooter shooter;
    private final BottomIndexer bottomIndexer;
    private final TopIndexer topIndexer;
    private final Limelight limelight;

    private AutonStages stage = AutonStages.BACK_UP;

    private final Timer timer = new Timer();

    public static final double SHOOT_LOWER_HUB_RPM = 1675; // i saw this somewhere in the code so this is it now, tune if needed

    public AutonomousTwoBallPreLoadedBottomHub(DriveTrain driveTrain, ClimberHooks climberHooks, ClimberArm climberArm, Shooter shooter,
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
        BACK_UP,
        ARM_AND_HOOK,
        SHOOT_FIRST_BALL,
        SHOOT_SECOND_BALL,
        TAXI_AWAY,
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
        if(RobotContainer.inTeleop) {
            cancel();
        }

        switch(stage) {
            case BACK_UP:
                if(!timer.hasElapsed(1)) { // tune if needed, same w/ ySpeed below
                    driveTrain.driveRR(-0.3, 0, 0);
                } else {
                    driveTrain.drive(0, 0, 0);
                    stage = AutonStages.SHOOT_FIRST_BALL;
                }
                break;
            case ARM_AND_HOOK:
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
                    stage = AutonStages.SHOOT_FIRST_BALL;
                }
                break;
            case SHOOT_FIRST_BALL:
                shooter.setRPMValue(SHOOT_LOWER_HUB_RPM);
                shooter.setShooterToRPM();
                if(shooter.getEncoderRPMValue() > shooter.getTargetRPMValue() - 50) {
                    topIndexer.setIndexSpeed(0.8);
                    timer.reset();
                    stage = AutonStages.SHOOT_SECOND_BALL;
                }
                break;
            case SHOOT_SECOND_BALL:
                if(timer.hasElapsed(3)) {
                    topIndexer.setIndexSpeed(0);
                    bottomIndexer.setIndexSpeed(0);
                    timer.reset();
                    stage = AutonStages.TAXI_AWAY;
                } else if(timer.hasElapsed(1)) {
                    bottomIndexer.setIndexSpeed(-0.8);
                }
                break;
            case TAXI_AWAY:
                if(!timer.hasElapsed(4)) { // tune
                    driveTrain.driveRR(-0.5, 0, 0);
                } else {
                    driveTrain.drive(0, 0, 0);
                }
                break;
            case FINISH:
                driveTrain.drive(0, 0, 0);
                topIndexer.setIndexSpeed(0);
                bottomIndexer.setIndexSpeed(0);
                shooter.setRPMValue(0);
                shooter.setShooterToRPM();
                break;
            default:
                driveTrain.drive(0, 0, 0);
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