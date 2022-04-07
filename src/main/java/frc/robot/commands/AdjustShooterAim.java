// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;

/** An example command that uses an example subsystem. */
public class AdjustShooterAim extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain driveTrain;
  private NetworkTableInstance inst;
  private NetworkTable table;
  private PIDController pidcontrol;
  private final float kp = 0.03f;
  private final float ki = 0.01f;
  private final float kd = 0.02f; 
  
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public AdjustShooterAim(DriveTrain subsystem) {
    pidcontrol = new PIDController(1, ki, kd);
    driveTrain = subsystem;
    addRequirements(subsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("limelight");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    float tx = table.getEntry("tx").getNumber(0).floatValue() * kp;
    double steering_adjust = pidcontrol.calculate(tx);
    driveTrain.drive(0, 0, steering_adjust);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  } 
}
