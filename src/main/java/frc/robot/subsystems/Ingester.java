// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Ingester extends SubsystemBase {

    //feeding system
    private final Talon collectingRoller;

    



  /** Creates a new ExampleSubsystem. */
  public Ingester() {
    collectingRoller = new Talon(9);
  }


  public void setIngestRollerSpeed(double speed){
    collectingRoller.set(speed);
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
