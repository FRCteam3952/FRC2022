// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.ClimberHooks;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * self-explanatory
 */
public class ControlHooks extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final ClimberHooks climber;
  private final double MAX_POSITION = 282.5; // measured in motor rotations

  public ControlHooks(ClimberHooks subsystem) {
    climber = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {

    double hookSpeed = RobotContainer.tertiaryJoystick.getLateralMovement();

    if (climber.bottomLimitPressed()) { // if bottom limit switch is pressed then reset encoder to 0
      climber.setPosition(0);
      climber.setHookSpeed(hookSpeed < 0 ? hookSpeed : 0); // if hooks are at bottom limit, restrict movement downwards
    } 
    else if (climber.getEncoderPosition() >= MAX_POSITION) {
      climber.setHookSpeed(hookSpeed > 0 ? hookSpeed : 0); // if hooks are at upper limit, restrict movement upwards
    } 
    else {
      climber.setHookSpeed(hookSpeed);
    }

    // System.out.print("" + armSpeed + " " + hookSpeed);
    // System.out.println("Arm speed: " + climber.getArmAngleSpeed() + " Hook speed:
    // " + climber.getHookSpeed());
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }

}
