// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Ingester;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class GrabPoles extends CommandBase {
  private final Climber climber;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public GrabPoles(Climber subsystem) {
    climber = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      System.out.println("climbing");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      climber.turnOnHookMotor();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      climber.turnOffHookMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return climber.getHookLimitSwitch();
  }
}
