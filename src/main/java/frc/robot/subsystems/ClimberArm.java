
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;




public class ClimberArm extends SubsystemBase {
  private final CANSparkMax armAngle;
  private final RelativeEncoder armAngleEncoder;
  
  /** Creates a new ExampleSubsystem. */
  public ClimberArm() {
    armAngle = new CANSparkMax(Constants.armAnglePort, MotorType.kBrushless);
    armAngleEncoder = armAngle.getEncoder();
  }
  
  public void resetClimbEncoder() {
    armAngleEncoder.setPosition(0);
  }

  public double getArmAngleEncoder() {
    return armAngleEncoder.getPosition();
  }

  public double changeArmAngle(double speed) {
    armAngle.set(speed);
    return speed;
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
