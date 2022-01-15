// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    //shooter structure
    private final Talon shooterRollerL;
    private final Talon shooterRollerR;
    private final MotorControllerGroup shooter;


  /** Creates a new ExampleSubsystem. */
  public Shooter() {
    shooterRollerL = new Talon(3);
    shooterRollerR = new Talon(5);
    shooter = new MotorControllerGroup(shooterRollerL, shooterRollerR);
  }


  public void setShooterSpeed(double speed){
    shooter.set(speed);
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
