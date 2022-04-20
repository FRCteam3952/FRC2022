// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
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
    if(climberArm.climberArmAngleLimitPressed()){
      climberArm.resetArmAngleEncoder();
    }

    if(RobotContainer.tertiaryJoystick.joystick.getRawButton(Constants.setArmAngleto90ButtonNumber)){
      if(climberArm.getArmAngleEncoder() < 89.69){
        climberArm.setArmSpeed(1);
      }
      else{
        climberArm.setArmSpeed(0);
      }
    }
    else{
      if (RobotContainer.tertiaryJoystick.joystick.getRawButton(Constants.moveArmAngleToRobotButtonNumber) && !climberArm.climberArmAngleLimitPressed() && (ClimberHooks.getHookEncoder() > 140 || climberArm.getArmAngleEncoder() > 40)) {
        climberArm.setArmSpeed(-ARM_SPEED);
      } else if (RobotContainer.tertiaryJoystick.joystick.getRawButton(Constants.moveArmAngleAwayFromRobotButtonNumber) && climberArm.getArmAngleEncoder() < 100) {
        climberArm.setArmSpeed(ARM_SPEED);
      } else {
        climberArm.setArmSpeed(0);
      }
      // System.out.println("arm angle = " + climberArm.getArmAngleEncoder());
      System.out.println(climberArm.climberArmAngleLimitPressed());
    }
  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
