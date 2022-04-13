// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;

/**
 * self explanatory
 */

public class Shooter extends SubsystemBase {
    
    private CANSparkMax followerMotor; //double motor system
    private CANSparkMax leaderMotor;
    private SparkMaxPIDController pidController;
    private final DigitalInput bottomShooterLim, topShooterLim;
    private double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, rpmValue;
    private double rpmThreshold;

  public Shooter() {
    bottomShooterLim = new DigitalInput(Constants.shooterBottomLimitPort);
    topShooterLim = new DigitalInput(Constants.shooterShootingLimitPort);
    
    leaderMotor = new CANSparkMax(Constants.flywheelPort1, MotorType.kBrushless);
    followerMotor = new CANSparkMax(Constants.flywheelPort2, MotorType.kBrushless);
    leaderMotor.setInverted(false);
    followerMotor.follow(leaderMotor, true); //motor follows leader in inverse
    // setShooterPower(0.85); // rpm 4890
    pidController = leaderMotor.getPIDController();
  
    kP = 6e-5;
    kI = 0;
    kD = 0; 
    kIz = 0; 
    kFF = 0.000015; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    rpmValue = 500;
    rpmThreshold = 3000; // threshold away from desired rpm to activate PID control

    pidController.setP(kP);
    pidController.setI(kI);
    pidController.setD(kD);
    pidController.setIZone(kIz);
    pidController.setFF(kFF);
    pidController.setOutputRange(kMinOutput, kMaxOutput);
  }

  public void setShooterPower(double speed) {
    leaderMotor.set(speed);
    // System.out.println("set the power to speed");
  }

  public void setShooterToRPM(){
    double setPoint = rpmValue;
    pidController.setReference(setPoint, CANSparkMax.ControlType.kVelocity); //uses PID to maintain constant RPM
  }

  public void setRPMValue(double rpm) {
    rpmValue = rpm;
  }

  public double getRPMValue() {
    return rpmValue;
  }

  public boolean getBottomShooterLim() {
    return bottomShooterLim.get();
  }

  public boolean getTopShooterLim() {
    return topShooterLim.get();
  }

  @Override
  public void periodic() {

  }

  @Override
  public void simulationPeriodic() {

  }

}
