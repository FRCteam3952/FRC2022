
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



public class ClimberHooks extends SubsystemBase {
  // feeding system

  private final CANSparkMax hook;
  private final DigitalInput bottomLimitSwitch;
  //private final DigitalInput topOrBottomLimitSwitch;

  private final RelativeEncoder hookEncoder;

  /** Creates a new ExampleSubsystem. */
  public ClimberHooks() {
    hook = new CANSparkMax(Constants.hookPort, MotorType.kBrushless);
    hookEncoder = hook.getEncoder();
    bottomLimitSwitch = new DigitalInput(Constants.bottomLimitSwitchClimberPort);
    // topOrBottomLimitSwitch = new DigitalInput(Constants.topOrBottomLimitClimberPort); //only used if third limit switch is used; not used if using manual control
  }

//sets the angle and speed for sliding hook for VSPX talon
  public double slideHook(double speed) {
    hook.set(speed);
    return speed;
  }

//return speed of motor for hook motor
  public double getHookSpeed() {
    //System.out.println(hook.get());
    return hook.get();
  }

  public boolean bottomLimitPressed() {
    //System.out.println(bottomLimitSwitch.get());
    return bottomLimitSwitch.get();
  }

  public boolean topOrBottomLimitPressed() {
    return bottomLimitPressed();
  }

  public double getEncoderPosition() {
    return hookEncoder.getPosition();
  }

  public void setPosition(double position) {
    hookEncoder.setPosition(position);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

}
