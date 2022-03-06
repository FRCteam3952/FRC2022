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
  private final AutoClimb autoClimb;
  private boolean isClimbing;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualClimb(Climber subsystem) {
    climber = subsystem;
    autoClimb = new AutoClimb(climber);
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
    if (!RobotContainer.climberStick.rightShoulderPressed()) {
      if (isClimbing) {
        autoClimb.cancel();
        isClimbing = false;
      }
      double armSpeed = RobotContainer.climberStick.getYValue() * 1/3;
      double hookSpeed = RobotContainer.climberStick.getZValue() * 1/3;
      climber.changeArmAngle((armSpeed));
      climber.slideHook((hookSpeed));
      System.out.println("Arm speed: " + climber.getArmAngleSpeed() + " Hook speed: " + climber.getHookSpeed());
     
      
    }  
    else {
      if (!isClimbing) {
        isClimbing = true;
        autoClimb.schedule();
      }
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
