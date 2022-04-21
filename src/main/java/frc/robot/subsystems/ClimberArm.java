
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
    return armAngleEncoder.getPosition() * 0.1804 + 31;
  }

  public double setArmSpeed(double speed) {
    if(!climberArmAngleLimitPressed()) {
      armAngle.set(speed);
    } else {
      armAngle.set(speed < 0 ? 0 : speed);
      resetArmAngleEncoder();
      System.out.println("arm hitting limit switch, stopping");
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
    //System.out.println("climber lim press: " + climberArmAngleLimitPressed());
    //System.out.println("climber arm enc: " + getArmAngleEncoder());

  }

  @Override
  public void simulationPeriodic() {

  }

}
