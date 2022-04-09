// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.ClimberHooks;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ControlHooks extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ClimberHooks climber;
  private final double MAX_POSITION = 50; //measured in motor rotations, measure later
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ControlHooks(ClimberHooks subsystem) {
    climber = subsystem;
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
    // System.out.println("hook");
    //double hookSpeed = RobotContainer.tangoIIController.getZValue(); 
    double hookSpeed = RobotContainer.secondaryJoystick.getLateralMovement(); 
    //hookSpeed = 0;
    if (climber.bottomLimitPressed()) {
      climber.setPosition(0);
    }

    // if ((hookSpeed > 0 && climber.getEncoderPosition() >= MAX_POSITION) || (hookSpeed < 0 && climber.getEncoderPosition() <= 0)) ;
      //do nothing
    // else
      climber.slideHook(hookSpeed);


    // System.out.print("" + armSpeed + " " + hookSpeed);
    // System.out.println("Arm speed: " + climber.getArmAngleSpeed() + " Hook speed: " + climber.getHookSpeed());    
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
