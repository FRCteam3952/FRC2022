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
  private final ClimberHooks climberHooks;

  private final double MAX_POSITION = 282.5; // measured in motor rotations, measure later

  public ControlHooks(ClimberHooks climberHooks) {
    this.climberHooks = climberHooks;

    addRequirements(climberHooks);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    //System.out.println(climberHooks.getHookEncoder());
    // System.out.println("hook");
    // double hookSpeed = RobotContainer.tangoIIController.getZValue();
    double hookSpeed = RobotContainer.tertiaryJoystick.getLateralMovement();
    // if ((hookSpeed > 0 && climber.getEncoderPosition() >= MAX_POSITION) ||
    // (hookSpeed < 0 && climber.getEncoderPosition() <= 0)) ;
    // do nothing
    // else
    if (climberHooks.bottomLimitPressed()) { // if bottom limit switch is pressed then reset encoder to 0
      climberHooks.setHookEncoder(0);
      climberHooks.setHookSpeed(hookSpeed < 0 ? hookSpeed : 0); // if hook going backward then set speed to 0
    } else if (ClimberHooks.getHookEncoder() >= MAX_POSITION) {
      climberHooks.setHookSpeed(hookSpeed > 0 ? hookSpeed : 0); // if hook going forward then set speed to 0
    } else {
      climberHooks.setHookSpeed(hookSpeed);
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
