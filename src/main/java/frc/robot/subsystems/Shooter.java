// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

/**
 * self explanatory
 */

public class Shooter extends SubsystemBase {

  private CANSparkMax followerMotor; // double motor system
  private CANSparkMax leaderMotor;
  private RelativeEncoder leaderEncoder;
  private SparkMaxPIDController pidController;
  private final DigitalInput bottomShooterLim, topShooterLim;
  private double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, rpmValue;

  public Shooter() {
    bottomShooterLim = new DigitalInput(Constants.shooterBottomLimitPort);
    topShooterLim = new DigitalInput(Constants.shooterTopLimitPort);

    leaderMotor = new CANSparkMax(Constants.flywheelPort1, MotorType.kBrushless);
    followerMotor = new CANSparkMax(Constants.flywheelPort2, MotorType.kBrushless);
    leaderMotor.setInverted(true);
    followerMotor.follow(leaderMotor, true); // motor follows leader in inverse
    // setShooterPower(0.85); // rpm 4890
    pidController = leaderMotor.getPIDController();
    leaderEncoder = leaderMotor.getEncoder();

    kP = 4.169e-5;
    kI = 2.5e-10;
    kD = 0;
    kIz = 0;
    kFF = 1.68e-4;
    kMaxOutput = 1;
    kMinOutput = -1;
    rpmValue = 0;

    pidController.setP(kP);
    pidController.setI(kI);
    pidController.setD(kD);
    pidController.setIZone(kIz);
    pidController.setFF(kFF);
    pidController.setOutputRange(kMinOutput, kMaxOutput);
  }

  public void setShooterPower(double speed) {
    leaderMotor.set(speed);
    // leaderMotor.set(speed);
    // System.out.println("set the power to speed");
  }

  public void setShooterToRPM() {
    double setPoint = rpmValue;
    pidController.setReference(setPoint, CANSparkMax.ControlType.kVelocity); // uses PID to maintain constant RPM
  }

  public void setRPMValue(double rpm) {
    rpmValue = rpm;
  }

  public void stopShooter() {
    pidController.setReference(0, CANSparkMax.ControlType.kVelocity);
  }

  public double getEncoderRPMValue() {
    return leaderEncoder.getVelocity();
  }

  public double getRPMValue() {
    return rpmValue;
  }

  public boolean bottomShooterLimitPressed() {
    return bottomShooterLim.get();
  }

  public boolean topShooterLimitPressed() {
    return topShooterLim.get();
  }

  @Override
  public void periodic() {
    // System.out.println(getRPMValue());
  }

  @Override
  public void simulationPeriodic() {

  }

}
