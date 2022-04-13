// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.robot.Constants;

/**
 * self explanatory
 */

public class BottomIndexer extends SubsystemBase {
  private final VictorSPX bottomIndexer;
  private final Servo servo;

  // pushing ball backwards in limit switch is activated, greenwheel at bottom
  // spins when ingesting

  public BottomIndexer() {
    bottomIndexer = new VictorSPX(Constants.bottomIndexerPort);
    servo = new Servo(Constants.ingesterServoPort);
  }

  public void setIndexSpeed(double speed) {
    bottomIndexer.set(ControlMode.PercentOutput, speed);
  }

  public void releaseServo() {
    servo.set(1);
  }

  public void setServo(double pos) {
    servo.set(pos);
  }

  @Override
  public void periodic() {

  }

  @Override
  public void simulationPeriodic() {

  }

}
