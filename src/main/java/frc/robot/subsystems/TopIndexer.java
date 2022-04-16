// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;


/**
 * self explanatory
 */

public class TopIndexer extends SubsystemBase {

  // feeding system
  private final VictorSPX topIndexer;

  public TopIndexer() {
    topIndexer = new VictorSPX(Constants.topIndexerPort);
  }

  public void setIndexSpeed(double speed) {
    topIndexer.set(ControlMode.PercentOutput, speed);
  }

  @Override
  public void periodic() {

  }

  @Override
  public void simulationPeriodic() {

  }

}