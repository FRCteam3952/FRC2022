// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Servos extends SubsystemBase {

    //feeding system

    public final Servo unlockServo1;
    public final Servo unlockServo2;



  /** Creates a new ExampleSubsystem. */
  public Servos() {
    unlockServo1 = new Servo(Constants.unlockServo1Port);
    unlockServo2 = new Servo(Constants.unlockServo2Port);
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
