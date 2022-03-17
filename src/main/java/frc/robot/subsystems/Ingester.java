// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Ingester extends SubsystemBase {

    //feeding system
    private final Talon collectingRollerBottom;
    private final Talon collectingRollerTop;

    
//pushing ball backwards in limit switch is activated, greenwheel at bottom spins when ingesting


  /** Creates a new ExampleSubsystem. */
  public Ingester() {
    collectingRollerBottom = new Talon(Constants.ingesterCollectingRollerBottomPort);
    collectingRollerTop = new Talon(Constants.ingesterCollectingRollerTopPort);
  }


  public void setIngestRollerSpeed(double speed){
    collectingRollerBottom.set(speed);
  }

  public void setTopRollerSpeed(double speed){
    collectingRollerTop.set(speed);
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
