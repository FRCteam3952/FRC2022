// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;

/**
 * To be removed
 */

public class Tachometer extends SubsystemBase {

  // feeding system
  private static Counter tacheo;

  public Tachometer() {

    tacheo = new Counter(new DigitalInput(Constants.shooterTachometerPort));

  }

  public static double getShooterRPM() {
    double period = tacheo.getPeriod();
    return (1 / period) * 60;
  }

  public void resetT() {
    tacheo.reset();
  }

  @Override
  public void periodic() {

  }

  @Override
  public void simulationPeriodic() {

  }

}