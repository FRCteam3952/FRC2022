
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

public class ClimberHooks extends SubsystemBase {

  private final CANSparkMax hook;
  private final DigitalInput bottomLimitSwitch;

  private final RelativeEncoder hookEncoder;

  public ClimberHooks() {
    hook = new CANSparkMax(Constants.hookPort, MotorType.kBrushless);
    hookEncoder = hook.getEncoder();
    bottomLimitSwitch = new DigitalInput(Constants.bottomLimitSwitchClimberPort);
  }

  // sets the angle and speed for sliding hook for VSPX talon
  public double setHookSpeed(double speed) {
    hook.set(speed);
    return speed;
  }

  // return speed of motor for hook motor
  public double getHookSpeed() {
    // System.out.println(hook.get());
    return hook.get();
  }

  public boolean bottomLimitPressed() {
    // System.out.println(bottomLimitSwitch.get());
    return !bottomLimitSwitch.get();
  }

  public double getHookEncoder() {
    return -hookEncoder.getPosition();
  }

  public void setHookEncoder(double position) {
    hookEncoder.setPosition(position);
  }

  public void resetHookEncoder() {
    setHookEncoder(0);
  }

  @Override
  public void periodic() {

  }

  @Override
  public void simulationPeriodic() {

  }

}
