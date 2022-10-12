package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.controller.BangBangController;

/**
 * Attempting to use a BangBangController for Shooter since I saw it on WPILIB docs website and it said it was good for high speed stuff like shooter
 */
public class BangBangShooter extends Shooter {
    private static final double TOLERANCE = Double.POSITIVE_INFINITY; // default for no-args constructor

    private final BangBangController bangBangController;
    public BangBangShooter() {
        super();

        this.bangBangController = new BangBangController(TOLERANCE);
        this.leaderMotor.set(bangBangController.calculate(leaderMotor.getEncoder().getVelocity(), rpmValue));
    }

    @Override
    protected void init() {
        kFF = 1e-6;
        kMaxOutput = 1;
        kMinOutput = 0;

        this.pidController.setFF(kFF);
        this.pidController.setOutputRange(kMinOutput, kMaxOutput);
    }

    @Override
    public void periodic() {
        // idk but i can't get a standard feedforward on the motor, so i have to use their prebuilt PID controller (which should be fine)
        this.pidController.setReference(bangBangController.calculate(this.leaderEncoder.getVelocity(), rpmValue), CANSparkMax.ControlType.kVelocity);
    }
}

