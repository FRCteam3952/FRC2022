// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimberArm;

/**
 * self-explanatory
 */

public class ControlArm extends CommandBase {
  private final ClimberArm arm;
  private final double armSpeed = 1;

  public ControlArm(ClimberArm subsystem) {
    arm = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    // System.out.println("arm");
    // double armSpeed = RobotContainer.tangoIIController.getXValue();
    // double armSpeed = RobotContainer.secondaryJoystick.getHorizontalMovement();
    // armSpeed = 0;
    if (RobotContainer.tertiaryJoystick.joystick.getRawButton(Constants.moveArmAngleToRobotButtonNumber)
        && !arm.angleLimitPressed()) {
      arm.changeArmAngle(-armSpeed);
    } else if (RobotContainer.tertiaryJoystick.joystick.getRawButton(Constants.moveArmAngleAwayFromRobotButtonNumber)) {
      arm.changeArmAngle(armSpeed);
    } else {
      arm.changeArmAngle(0);
    }
    if (RobotContainer.tertiaryJoystick.joystick.getRawButtonPressed(Constants.resetClimberEncoderButtonNumber)) {
      arm.resetEncoder();
    }
    // System.out.println("arm angle = " + arm.getArmAngleEncoder());
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
