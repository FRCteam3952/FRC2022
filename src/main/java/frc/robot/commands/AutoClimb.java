// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.ClimberHooks;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ClimberArm;
import edu.wpi.first.wpilibj2.command.CommandBase;

import edu.wpi.first.wpilibj.Timer;

/**
 * self-explanatory
 * 
 * TESTING REQUIRED --
 * numbers required,
 * testing top or bottom of traversal bar
 * --
 */

public class AutoClimb extends CommandBase {
  private enum ClimbingStates {
    LIFTING_NO_ANGLE,
    LIFTING_WITH_ANGLE,
    MOVE_TO_HIGH,
    SEND_HOOKS_UP,
    MOVE_TO_HIGHS,
    SEND_HOOKS_UP_2,
    TRAVERSE,
    WAIT
  }

  private final Timer timer = new Timer();

  private final ClimberHooks climberHooks;
  private final ClimberArm climberArm;
  private final double MAX_POSITION = 282.5; // measured in motor rotations, measure later

  private ClimbingStates state = ClimbingStates.LIFTING_NO_ANGLE;
  private final double climbingAngle = 46; // degrees for climbing under the high bar

  public AutoClimb(ClimberHooks climberHooks, ClimberArm climberArm) {
    this.climberHooks = climberHooks;
    this.climberArm = climberArm;
    addRequirements(climberHooks, climberArm);
  }

  @Override
  public void initialize() {
    System.out.println("climbing");
    // state = ClimbingStates.LIFTING_NO_ANGLE;
    timer.start();
  }

  public boolean checkHookandAngle(double anglePos, double hookPos) {
    double angleSpeed = climberArm.getArmSpeed();
    double hookSpeed = climberHooks.getHookSpeed();
    int truth_count = 0;

    if (angleSpeed >= 0 && climberArm.getArmAngleEncoder() >= anglePos) {
      climberArm.changeArmAngle(0);
      truth_count++;
    } else if (angleSpeed <= 0 && climberArm.getArmAngleEncoder() <= anglePos) {
      climberArm.changeArmAngle(0);
      truth_count++;
    }

    if (hookSpeed >= 0 && (climberHooks.getHookEncoder() <= hookPos || climberHooks.bottomLimitPressed())) {
      climberHooks.setHookSpeed(0);
      truth_count++;
    } else if (hookSpeed <= 0 && climberHooks.getHookEncoder() >= hookPos) {
      climberHooks.setHookSpeed(0);
      truth_count++;
    }
    return (truth_count == 2);
  }

  @Override
  public void execute() {
    if (RobotContainer.tertiaryJoystick.joystick.getRawButtonPressed(Constants.resetAutoClimbButtonNumber)) {
      state = ClimbingStates.LIFTING_NO_ANGLE;
    }
    switch (state) {
      case LIFTING_NO_ANGLE:
        climberArm.changeArmAngle(0);
        climberHooks.setHookSpeed(1);
        if (climberHooks.getHookEncoder() < 110) {
          climberArm.changeArmAngle(-1);
          climberHooks.setHookSpeed(0.4);
          state = ClimbingStates.LIFTING_WITH_ANGLE;
        }
        break;
      case LIFTING_WITH_ANGLE:
        if (checkHookandAngle(climbingAngle, 30)) {
          climberHooks.setHookSpeed(0.5);
          climberArm.changeArmAngle(1);
          state = ClimbingStates.MOVE_TO_HIGH;
        }
        break;
      case MOVE_TO_HIGH:
        if (checkHookandAngle(90, 0)) {
          state = ClimbingStates.SEND_HOOKS_UP;
        }
        break;

      case SEND_HOOKS_UP:
        climberHooks.setHookSpeed(-0.8);
        if (climberHooks.getHookEncoder() > MAX_POSITION) {
          climberHooks.setHookSpeed(0);
          timer.reset();
          state = ClimbingStates.MOVE_TO_HIGHS;
        }
        break;

      case MOVE_TO_HIGHS:
        if (timer.hasElapsed(3)) {
          climberHooks.setHookSpeed(0.6);
        }
        if (climberHooks.bottomLimitPressed()) {
          climberHooks.setHookSpeed(0);
          state = ClimbingStates.WAIT;
        }
        break;

      case SEND_HOOKS_UP_2:
        climberHooks.setHookSpeed(-0.8);
        if (climberHooks.getHookEncoder() > 100)
          climberArm.changeArmAngle(0.5);
        if (climberHooks.getHookEncoder() >= MAX_POSITION) {
          state = ClimbingStates.TRAVERSE;
          timer.reset();
        }
        break;

      case TRAVERSE:
        climberHooks.setHookSpeed(0.8);
        if (timer.hasElapsed(0.8)) {
          climberHooks.setHookSpeed(0);
          state = ClimbingStates.WAIT;
        }

      default:
        // System.err.println("No state is true");
        break;
    }
  }

  @Override
  public void end(boolean interrupted) {
    climberHooks.setHookSpeed(0);
    climberArm.changeArmAngle(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
