// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ClimberArm;

import edu.wpi.first.wpilibj2.command.CommandBase;


/**
 * self-explanatory
 */

public class ControlArm extends CommandBase {
  private final ClimberArm climberArm;
  
  private final double ARM_SPEED = 1;

  public ControlArm(ClimberArm climberArm) {
    this.climberArm = climberArm;

    addRequirements(climberArm);
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
        && !climberArm.climberArmAngleLimitPressed()) {
      climberArm.setArmSpeed(-ARM_SPEED);
    } else if (RobotContainer.tertiaryJoystick.joystick.getRawButton(Constants.moveArmAngleAwayFromRobotButtonNumber)) {
      climberArm.setArmSpeed(ARM_SPEED);
    } else {
      climberArm.setArmSpeed(0);
    }

    if (RobotContainer.tertiaryJoystick.joystick.getRawButtonPressed(Constants.resetClimberEncoderButtonNumber)) {
      climberArm.resetArmAngleEncoder();
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
