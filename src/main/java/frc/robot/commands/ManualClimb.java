// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ManualClimb extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Climber climber;
  private boolean isClimbing;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualClimb(Climber subsystem) {
    climber = subsystem;
    isClimbing = false;
    addRequirements(subsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double armSpeed = RobotContainer.climberStick.getYValue();
    double hookSpeed = RobotContainer.climberStick.getZValue();

    // System.out.print("" + armSpeed + " " + hookSpeed);

    climber.changeArmAngle((armSpeed));
    climber.slideHook(hookSpeed);
    // System.out.println("Arm speed: " + climber.getArmAngleSpeed() + " Hook speed: " + climber.getHookSpeed());    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      System.out.println("auto time");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  
}
