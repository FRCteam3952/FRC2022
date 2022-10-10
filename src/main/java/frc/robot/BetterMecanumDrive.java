package frc.robot;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

import java.lang.reflect.Field;

public class BetterMecanumDrive extends MecanumDrive {
    public static final double X_SPEED_MINIMUM = 0;

    private final Field fieldM_Reported;
    private final CANSparkMax frontLeft;
    private final CANSparkMax frontRight;
    private final CANSparkMax rearLeft;
    private final CANSparkMax rearRight;

    private final double[] mults;
    public BetterMecanumDrive(CANSparkMax frontLeftMotor, CANSparkMax rearLeftMotor, CANSparkMax frontRightMotor, CANSparkMax rearRightMotor) {
        this(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, 1, 1, 1, 1);
    }

    public BetterMecanumDrive(CANSparkMax frontLeftMotor, CANSparkMax rearLeftMotor, CANSparkMax frontRightMotor, CANSparkMax rearRightMotor, double flMult, double frMult, double rlMult, double rrMult) {
        super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

        /*
         * This is a hack to get around the fact that the MecanumDrive class doesn't expose the reported field.
         * Since I think this is necessary for {@link #driveCartesian(double, double, double, double)} to not explode, I have to make it accessible and hope it doesn't explode
         *
         * Only reason we need this is because the back motors are going wack and now we have to give those motors more power
         */
        try {
            // Use reflection to get the field from the class (field called "m_reported")
            fieldM_Reported = MecanumDrive.class.getDeclaredField("m_reported");
            // And allow it to be read and changed
            fieldM_Reported.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.frontLeft = frontLeftMotor;
        this.frontRight = frontRightMotor;
        this.rearLeft = rearLeftMotor;
        this.rearRight = rearRightMotor;

        this.mults = new double[]{flMult, frMult, rlMult, rrMult};
    }

    @Override
    public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
        try {
            // Now we can get the value
            if (!fieldM_Reported.getBoolean(this)) {
                HAL.report(
                        FRCNetComm.tResourceType.kResourceType_RobotDrive, FRCNetComm.tInstances.kRobotDrive2_MecanumCartesian, 4);
                // and change it
                fieldM_Reported.set(this, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ySpeed = MathUtil.applyDeadband(ySpeed, m_deadband);
        xSpeed = MathUtil.applyDeadband(xSpeed, m_deadband);

        var speeds = driveCartesianIK(ySpeed, xSpeed, zRotation, gyroAngle);
        double[] moreSpeeds = new double[] {
                speeds.frontLeft * m_maxOutput,
                speeds.frontRight * m_maxOutput,
                speeds.rearLeft * m_maxOutput,
                speeds.rearRight * m_maxOutput
        };

        // Since the problems only happen when strafing left/right, only apply multiplier when the xSpeed is not close to 0 (to avoid floating point comparison a delta is used).
        // We also have to normalize() the speeds so that the proportions are the same.
        if(Math.abs(xSpeed) >= X_SPEED_MINIMUM) {
            for(int i = 0; i < moreSpeeds.length; i++) {
                moreSpeeds[i] *= mults[i];
            }
        }
        normalize(moreSpeeds);

        this.frontLeft.set(moreSpeeds[0]);
        this.frontRight.set(moreSpeeds[1]);
        this.rearLeft.set(moreSpeeds[2]);
        this.rearRight.set(moreSpeeds[3]);

        feed();
    }
}
