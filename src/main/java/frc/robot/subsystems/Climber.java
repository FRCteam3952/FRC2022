// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

    //feeding system

    private final Talon mArmTalon;
    private final Talon mHookTalon;



  /** Creates a new ExampleSubsystem. */
  public Climber() {
    mArmTalon = new Talon(8);
    mHookTalon = new Talon(7);
  }


  private void setArmHeight(double height) {
    mArmTalon.set(height);
  }

  private void setHookDisplacement(double displacement) {
      
}

// logic to be continues TODO

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  
}
