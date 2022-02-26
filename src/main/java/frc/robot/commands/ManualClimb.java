// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.AutoClimb;

/** An example command that uses an example subsystem. */
public class ManualClimb extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final Climber climber;
  private final AutoClimb autoClimb;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualClimb(Climber subsystem) {
    climber = subsystem;
    autoClimb = new AutoClimb(climber);
    addRequirements(subsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Manual climb initiated");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!RobotContainer.driverStick.rightShoulderPressed()) {
      autoClimb.autoClimbOff();
      double armSpeed = RobotContainer.driverStick.getLateralMovement();
      double hookSpeed = RobotContainer.driverStick.getRotation();
      armSpeed *= 10;
      hookSpeed *= 10;
      armSpeed = (int) armSpeed;
      hookSpeed = (int) hookSpeed;
      armSpeed /= 10.0;
      hookSpeed /= 10.0; //rounds speeds to one decimal
      climber.changeArmAngle(armSpeed);
      climber.slideHook(hookSpeed);
      System.out.println("Arm speed: " + armSpeed + " Hook speed: "+ hookSpeed);
    } else {
      autoClimb.autoClimbOn();
      autoClimb.schedule();
    }
    
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
