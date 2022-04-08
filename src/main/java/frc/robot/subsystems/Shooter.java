// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

    //shooter structure
    // private final Talon shooterRollerL;
    // private final Talon shooterRollerR;
    // private final MotorControllerGroup shooter;
    // private final Talon shooterRollers;
    
    private CANSparkMax followerMotor = new CANSparkMax(Constants.flywheelPort2, MotorType.kBrushless);
  
    private CANSparkMax leaderMotor = new CANSparkMax(Constants.flywheelPort1, MotorType.kBrushless);

    private SparkMaxPIDController pidController = leaderMotor.getPIDController();

    private RelativeEncoder leaderEncoder = leaderMotor.getEncoder();

    private final DigitalInput bottomShooterLim, topShooterLim;
    
    private double kP = 0.005, kI = 0, kD = 0, kIz = 0, kFF = 0.000015, kMaxOutput = 1, kMinOutput = -1, autoShootRPM = 0;


  /** Creates a new ExampleSubsystem. */
  public Shooter() {
    // shooterRollerL = new Talon(Constants.shooterRollerLPort);
    // shooterRollerR = new Talon(Constants.shooterRollerRPort);
    // shooter = new MotorControllerGroup(shooterRollerL, shooterRollerR);
    // shooterRollers = new Talon(Constants.shooterRollersPort);
    bottomShooterLim = new DigitalInput(Constants.shooterBottomLimitPort);
    topShooterLim = new DigitalInput(Constants.shooterShootingLimitPort);
    followerMotor.follow(leaderMotor);
    pidController.setP(kP);
    pidController.setI(kI);
    pidController.setD(kD);
    pidController.setIZone(kIz);
    pidController.setFF(kFF);
    pidController.setOutputRange(kMinOutput, kMaxOutput);
  }


  public void setShooterRPM(){
    double setPoint = autoShootRPM;
    pidController.setReference(setPoint, ControlType.kVelocity); //uses PID to maintain constant RPM
  }
    
  public void setShooterPower(double speed) {
    leaderMotor.set(speed);
  }

  public void setAutoShootRPM(double rpm) {
    autoShootRPM = rpm;
  }

  public double getAutoShootRPM() {
    return autoShootRPM;
  }

  public boolean getBottomShooterLimPressed() {
    return bottomShooterLim.get();
  }

  public boolean getTopShooterLimPressed() {
    return topShooterLim.get();
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
