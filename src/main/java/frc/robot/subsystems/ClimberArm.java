
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * self explanatory
 */

public class ClimberArm extends SubsystemBase {
  private final CANSparkMax armAngle;
  private final DigitalInput angleLimitSwitch;
  private final RelativeEncoder armAngleEncoder;

  public ClimberArm() {
    armAngle = new CANSparkMax(Constants.armAnglePort, MotorType.kBrushless);
    armAngleEncoder = armAngle.getEncoder();
    angleLimitSwitch = new DigitalInput(Constants.angleLimitSwitchClimberPort);
  }

  public void resetArmAngleEncoder() {
    setArmAngleEncoder(0);
  }

  public void setArmAngleEncoder(double position) {
    armAngleEncoder.setPosition(position);
  }

  public double getArmAngleEncoder() {
    return armAngleEncoder.getPosition() * 0.213 + 32;
  }

  public double changeArmAngle(double speed) {
    armAngle.set(speed);
    if (climberArmAngleLimitPressed()) {
      resetArmAngleEncoder();
    }
    return speed;
  }

  public double getArmSpeed() {
    return armAngle.get();
  }

  public boolean climberArmAngleLimitPressed() {
    return angleLimitSwitch.get();
  }

  @Override
  public void periodic() {

  }

  @Override
  public void simulationPeriodic() {

  }

}
