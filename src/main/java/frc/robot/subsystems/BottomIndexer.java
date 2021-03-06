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

public class BottomIndexer extends SubsystemBase {
  private final VictorSPX bottomIndexer;
  /**
   * pushing ball backwards in limit switch is activated, greenwheel at bottom
   * spins when ingesting
   */

  public BottomIndexer() {
    bottomIndexer = new VictorSPX(Constants.bottomIndexerPort);
  }

  public void setIndexSpeed(double speed) {
    bottomIndexer.set(ControlMode.PercentOutput, speed);
  }

  @Override
  public void periodic() {

  }

  @Override
  public void simulationPeriodic() {

  }

}
