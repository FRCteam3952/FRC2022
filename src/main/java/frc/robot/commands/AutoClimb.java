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
 */

public class AutoClimb extends CommandBase {
  public static enum ClimbingStates {
    LIFTING_OFF_GROUND,
    LIFTING_SET_ANGLE,
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
  private final double CLIMBING_ANGLE = 45; // degrees for climbing under the high bar

  public static ClimbingStates state = ClimbingStates.LIFTING_OFF_GROUND;
  

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
      climberArm.setArmSpeed(0);
      truth_count++;
    } else if (angleSpeed <= 0 && climberArm.getArmAngleEncoder() <= anglePos) {
      climberArm.setArmSpeed(0);
      truth_count++;
    }
    if (hookPos <= 0.5){
      if(climberHooks.bottomLimitPressed()){
        climberHooks.setHookSpeed(0);
        truth_count++;
      }
    } else if (hookSpeed >= 0 && (ClimberHooks.getHookEncoder() <= hookPos)) {
      climberHooks.setHookSpeed(0);
      truth_count++;
    } else if (hookSpeed <= 0 && ClimberHooks.getHookEncoder() >= hookPos) {
      climberHooks.setHookSpeed(0);
      truth_count++;
    }

    return (truth_count == 2);
  }

  @Override
  public void execute() {
    if (RobotContainer.tertiaryJoystick.joystick.getRawButtonPressed(Constants.resetAutoClimbButtonNumber)) {
      state = ClimbingStates.LIFTING_OFF_GROUND;
      System.out.println("reset");
    }

    switch (state) {
      case LIFTING_OFF_GROUND:
        climberHooks.setHookSpeed(1);
        climberArm.setArmSpeed(-0.85);
        System.out.println("lift set angle");
        state = ClimbingStates.LIFTING_SET_ANGLE;
      
      break;
      
      case LIFTING_SET_ANGLE:
        if (checkHookandAngle(CLIMBING_ANGLE,50)) {
          climberArm.setArmSpeed(-1);
          climberHooks.setHookSpeed(1);
          System.out.println("lift w/angle");
          state = ClimbingStates.LIFTING_WITH_ANGLE;
        }

        break;

      case LIFTING_WITH_ANGLE:
        if (checkHookandAngle(40, 30)) {
          climberHooks.setHookSpeed(1);
          climberArm.setArmSpeed(1);
          System.out.println("move high");
          state = ClimbingStates.MOVE_TO_HIGH;
        }

        break;

      case MOVE_TO_HIGH:
        if (checkHookandAngle(50, 0)) {
          state = ClimbingStates.SEND_HOOKS_UP;
          System.out.println("send hook up");
          climberHooks.setHookSpeed(-1);
          climberArm.setArmSpeed(1);
        }

        break;

      case SEND_HOOKS_UP:
      
        if (checkHookandAngle(100, MAX_POSITION)) {          
          state = ClimbingStates.MOVE_TO_HIGHS;
          System.out.println("move highs");
        }

        break;

      case MOVE_TO_HIGHS:
        climberHooks.setHookSpeed(1);

        if (climberHooks.bottomLimitPressed()) {
          climberHooks.setHookSpeed(-1);
          climberArm.setArmSpeed(-1);
          System.out.println("send hook 2");
          state = ClimbingStates.SEND_HOOKS_UP_2;
        }

        break;

      case SEND_HOOKS_UP_2:
        if(checkHookandAngle(50, MAX_POSITION)) {
          timer.reset();
          state = ClimbingStates.TRAVERSE;
          System.out.println("traverse");
        }
        break;

      case TRAVERSE:
        climberHooks.setHookSpeed(1);

        if (timer.hasElapsed(0.69)) {
          climberHooks.setHookSpeed(0);

          state = ClimbingStates.WAIT;
          System.out.println("wait");
        }
        break;

      case WAIT:
        // haha do nothing
        break;

      default:
        // System.err.println("No state is true");
        break;
    }
  }

  @Override
  public void end(boolean interrupted) {
    climberHooks.setHookSpeed(0);
    climberArm.setArmSpeed(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
