
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;



public class UnlockIngestSub extends SubsystemBase {
  // feeding system

  private final Talon unlockIngestAngle;

  //private final DigitalInput topOrBottomLimitSwitch;

  /** Creates a new ExampleSubsystem. */
  public UnlockIngestSub() {
    unlockIngestAngle = new Talon(Constants.releaseIngesterPort);
    //topOrBottomLimitSwitch = new DigitalInput(3); //only used if third limit switch is used; not used if using manual control
  }


  public double changeIngestAngle(double speed) {
    unlockIngestAngle.set(speed);
    return speed;
  }

//return speed of motor for the arm angle motor
  public double getIngestSpeed() {
    return unlockIngestAngle.get();
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
