// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

    //shooter structure
    // private final Talon shooterRollerL;
    // private final Talon shooterRollerR;
    // private final MotorControllerGroup shooter;
    private final Talon shooterRollers;

    private double autoShootRPM = 0;


  /** Creates a new ExampleSubsystem. */
  public Shooter() {
    // shooterRollerL = new Talon(Constants.shooterRollerLPort);
    // shooterRollerR = new Talon(Constants.shooterRollerRPort);
    // shooter = new MotorControllerGroup(shooterRollerL, shooterRollerR);
    shooterRollers = new Talon(Constants.shooterRollersPort);
  }


  public void setShooterSpeed(double speed){
    // shooter.set(speed);
    shooterRollers.set(speed);
  }

  public void setAutoShootRPM(double rpm) {
    autoShootRPM = rpm;
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
