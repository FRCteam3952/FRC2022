package frc.robot;

import com.revrobotics.CANSparkMax;

public class BetterCANSparkMax extends CANSparkMax {
    private double multiplier;

    public BetterCANSparkMax(int deviceId, MotorType type) {
        super(deviceId, type);
        this.multiplier = 1.2;
    }

    public BetterCANSparkMax(int deviceId, MotorType type, double multiplier) {
        super(deviceId, type);
        this.multiplier = multiplier;
    }

    
    @Override
    public void set(double speed) {
        super.set(speed * this.multiplier);
    }
}
