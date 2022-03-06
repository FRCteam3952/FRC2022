
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;



public class Climber extends SubsystemBase {
  // feeding system

  private final VictorSPX armAngle;
  private final VictorSPX hook;
  private final DigitalInput topLimitSwitch;
  private final DigitalInput bottomLimitSwitch;
  //private final DigitalInput topOrBottomLimitSwitch;

  /** Creates a new ExampleSubsystem. */
  public Climber() {
    armAngle = new VictorSPX(Constants.armAnglePort);
    hook = new VictorSPX(Constants.hookPort);
    topLimitSwitch = new DigitalInput(Constants.topLimitSwitchClimberPort); 
    bottomLimitSwitch = new DigitalInput(Constants.bottomLimitSwitchClimberPort);
    //topOrBottomLimitSwitch = new DigitalInput(3); //only used if third limit switch is used; not used if using manual control
  }

  /* public void setMotorSpeed(double speed) {
    if (speed < 0) {


      if (topLimitSwitch.get()) {
        mHookTalon.set(0); 
        mArmAngleTalon.set(0);
        if (bottomLimitSwitch.get()) {
             mHookTalon.set(0);
        } else {
          mHookTalon.set(speed);
          mArmAngleTalon.set(speed);
         }


          
      } else {
        mHookTalon.set(speed);
      }
    }
  }
  */

//sets the angle and speed for sliding hook for VSPX talon
  public double slideHook(double speed) {
    hook.set(ControlMode.PercentOutput, speed);
    return speed;
  }

//return speed of motor for hook motor
  public double getHookSpeed() {
    return hook.getMotorOutputPercent();
  }

  public double changeArmAngle(double speed) {
    armAngle.set(ControlMode.PercentOutput, speed);
    return speed;
  }

//return speed of motor for the arm angle motor
  public double getArmAngleSpeed() {
    return armAngle.getMotorOutputPercent();
  }

  public boolean topLimitPressed() {
    //System.out.println(topLimitSwitch.get());
    return topLimitSwitch.get();
  }

  public boolean bottomLimitPressed() {
    //System.out.println(bottomLimitSwitch.get());
    return bottomLimitSwitch.get();
  }

  /*public boolean topOrBottomLimitPressed() {
    return topOrBottomLimitSwitch.get();
  }*/

  // logic to be continues TODO

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

}
