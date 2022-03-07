// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.counter.Tachometer;
import frc.robot.Constants;


public class Indexer extends SubsystemBase {

  //feeding system
  private final Talon indexWheels;
  private final DigitalInput bottomShooterLim;
  private final DigitalInput ballShooterLim;
  private final Tachometer tacheo; 


  /** Creates a new ExampleSubsystem. */
  public Indexer() {
    indexWheels = new Talon(Constants.indexerWheelsPort);
    bottomShooterLim = new DigitalInput(Constants.bottomLimitSwitchClimberPort);
    ballShooterLim = new DigitalInput(Constants.topLimitSwitchClimberPort);
    tacheo = new Tachometer(new DigitalInput(Constants.shooterTachometerPort));
  }


  public void setIndexSpeed(double speed){
    indexWheels.set(speed);
  }

  public boolean bottomShooterPressed() {
    return bottomShooterLim.get();
  }

  public boolean ballShooterPressed() {
    return ballShooterLim.get();
  }

  public double getShooterRevPerSec() {
    return tacheo.getRevolutionsPerSecond();
   
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
