
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.util.*;

public class Climber extends SubsystemBase {
  // feeding system

  private final Talon mArmAngleTalon;
  private final Talon mHookTalon;
  private final DigitalInput topLimitSwitch;
  private final DigitalInput bottomLimitSwitch;
  //private final DigitalInput topOrBottomLimitSwitch;

  /** Creates a new ExampleSubsystem. */
  public Climber() {
    mArmAngleTalon = new Talon(6);
    mHookTalon = new Talon(7);
    topLimitSwitch = new DigitalInput(1); 
    bottomLimitSwitch = new DigitalInput(2);
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

  public void slideHook(double speed) {
    mHookTalon.set(speed);
  }

  public void changeArmAngle(double speed) {
    mArmAngleTalon.set(speed);
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
