
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;



public class ClimberHooks extends SubsystemBase {
  // feeding system

  //private final CANSparkMax hook;
  private final DigitalInput topLimitSwitch;
  private final DigitalInput bottomLimitSwitch;
  private final DigitalInput topOrBottomLimitSwitch;

  //private final RelativeEncoder hookEncoder;

  /** Creates a new ExampleSubsystem. */
  public ClimberHooks() {
    //hook = new CANSparkMax(2, MotorType.kBrushless);
    //hookEncoder = hook.getEncoder();
    topLimitSwitch = new DigitalInput(Constants.topLimitSwitchClimberPort); 
    bottomLimitSwitch = new DigitalInput(Constants.bottomLimitSwitchClimberPort);
    topOrBottomLimitSwitch = new DigitalInput(Constants.topOrBottomLimitClimberPort); //only used if third limit switch is used; not used if using manual control
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
    //hook.set(speed);
    return speed;
  }

//return speed of motor for hook motor
  public double getHookSpeed() {
    //System.out.println(hook.get());
    //return hook.get();
    return 0;
  }

//return speed of motor for the arm angle motor
  public boolean topLimitPressed() {
    //System.out.println(topLimitSwitch.get());
    return topLimitSwitch.get();
  }

  public boolean bottomLimitPressed() {
    //System.out.println(bottomLimitSwitch.get());
    return bottomLimitSwitch.get();
  }

  public boolean topOrBottomLimitPressed() {
    return topOrBottomLimitSwitch.get();
  }

  public double getEncoderPosition() {
    //return hookEncoder.getPosition();
    return 0;
  }

  public void setPosition(double position) {
    //hookEncoder.setPosition(position);
  }

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
