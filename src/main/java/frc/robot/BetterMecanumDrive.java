package frc.robot;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

import java.lang.reflect.Field;

public class BetterMecanumDrive extends MecanumDrive {
    private final Field m_Reported_Field;
    private final CANSparkMax frontLeft;
    private final CANSparkMax frontRight;
    private final CANSparkMax rearLeft;
    private final CANSparkMax rearRight;

    private final double flMult, frMult, rlMult, rrMult;
    public BetterMecanumDrive(CANSparkMax frontLeftMotor, CANSparkMax rearLeftMotor, CANSparkMax frontRightMotor, CANSparkMax rearRightMotor) {
        this(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, 1, 1, 1, 1);
    }

    public BetterMecanumDrive(CANSparkMax frontLeftMotor, CANSparkMax rearLeftMotor, CANSparkMax frontRightMotor, CANSparkMax rearRightMotor, double flMult, double frMult, double rlMult, double rrMult) {
        super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

        try {
            m_Reported_Field = MecanumDrive.class.getDeclaredField("m_reported");
            m_Reported_Field.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.frontLeft = frontLeftMotor;
        this.frontRight = frontRightMotor;
        this.rearLeft = rearLeftMotor;
        this.rearRight = rearRightMotor;
        this.flMult = flMult;
        this.frMult = frMult;
        this.rlMult = rlMult;
        this.rrMult = rrMult;
    }

    @Override
    public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
        try {
            if (!m_Reported_Field.getBoolean(this)) {
                HAL.report(
                        FRCNetComm.tResourceType.kResourceType_RobotDrive, FRCNetComm.tInstances.kRobotDrive2_MecanumCartesian, 4);
                m_Reported_Field.set(this, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ySpeed = MathUtil.applyDeadband(ySpeed, m_deadband);
        xSpeed = MathUtil.applyDeadband(xSpeed, m_deadband);

        var speeds = driveCartesianIK(ySpeed, xSpeed, zRotation, gyroAngle);
        double[] moreSpeeds = new double[] {
                speeds.frontLeft * m_maxOutput * this.flMult,
                speeds.frontRight * m_maxOutput * frMult,
                speeds.rearLeft * m_maxOutput * rlMult,
                speeds.rearRight * m_maxOutput * rrMult
        };

        normalize(moreSpeeds);

        this.frontLeft.set(moreSpeeds[0]);
        this.frontRight.set(moreSpeeds[1]);
        this.rearLeft.set(moreSpeeds[2]);
        this.rearRight.set(moreSpeeds[3]);

        feed();
    }
}
