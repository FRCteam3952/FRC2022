// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.robot.Constants;


public class TopIndexer extends SubsystemBase {

  //feeding system
  private final VictorSPX topIndexer;


  /** Creates a new ExampleSubsystem. */
  public TopIndexer() {
    topIndexer = new VictorSPX(Constants.topIndexerPort);
    
    
  }


  public void setIndexSpeed(double speed){
    topIndexer.set(ControlMode.PercentOutput, speed);
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