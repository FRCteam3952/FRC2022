// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;


public class Indexer extends SubsystemBase {

  //feeding system
  private final Talon indexWheels;
  private final DigitalInput L1;
  private final DigitalInput L2;



  /** Creates a new ExampleSubsystem. */
  public Indexer() {
    indexWheels = new Talon(5);
    L1 = new DigitalInput(3);
    L2 = new DigitalInput(4);
  }


  public void setIndexSpeed(double speed){
    indexWheels.set(speed);
  }

  public boolean L1Pressed() {
    return L1.get();
  }

  public boolean L2Pressed() {
    return L2.get();
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
