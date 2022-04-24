
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

public class ClimberHooks extends SubsystemBase {

  private final CANSparkMax hook;
  private final DigitalInput bottomLimitSwitch;

  private static RelativeEncoder hookEncoder;

  public ClimberHooks() {
    hook = new CANSparkMax(Constants.hookPort, MotorType.kBrushless);
    hookEncoder = hook.getEncoder();
    bottomLimitSwitch = new DigitalInput(Constants.bottomLimitSwitchClimberPort);
  }

  // sets the angle and speed for sliding hook for VSPX talon
  public double setHookSpeed(double speed) {
    if(!bottomLimitPressed()) {
      hook.set(speed);
    } else {
      hook.set(speed < 0 ? speed : 0);
      resetHookEncoder();
      //System.out.println("hook hitting limit switch, stopping");
    }
    return speed;
  }

  // return speed of motor for hook motor
  public double getHookSpeed() {
    // System.out.println(hook.get());
    return hook.get();
  }

  public boolean bottomLimitPressed() {
    // System.out.println(bottomLimitSwitch.get());
    return !bottomLimitSwitch.get(); // must be negated
  }

  public static double getHookEncoder() {
    return -hookEncoder.getPosition(); // must be negated
  }

  public void setHookEncoder(double position) {
    hookEncoder.setPosition(position);
  }

  public void resetHookEncoder() {
    setHookEncoder(0);
  }

  @Override
  public void periodic() {
    //System.out.println("hook lim press: " + bottomLimitPressed());
    //System.out.println("hook enc: " + getHookEncoder());


  }

  @Override
  public void simulationPeriodic() {

  }

}
