// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Sets starting config for the robot
 */

public class StartingConfig extends CommandBase {
  private final BottomIndexer ingest;
  private final ClimberArm arm;
  private final ClimberHooks hooks;
  private double armSpeed = 0.5;
  private double hookSpeed = 0.6;
  private final double STARTING_ARM_ANGLE = 45; // find later
  private int stage = 1;

  public StartingConfig(BottomIndexer ingest, ClimberArm arm, ClimberHooks hooks) {
    this.ingest = ingest;
    this.arm = arm;
    this.hooks = hooks;
    addRequirements(ingest, arm, hooks);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    switch (stage) {
      case 1:
        ingest.setServo(-1);
        if (!arm.angleLimitPressed()) {
          arm.changeArmAngle(-armSpeed);
        } else {
          arm.changeArmAngle(0);
          arm.resetEncoder();
          stage = 2;
        }
        break;

      case 2:
        if (arm.getArmAngleEncoder() < STARTING_ARM_ANGLE)
          arm.changeArmAngle(armSpeed);
        if (!hooks.bottomLimitPressed())
          hooks.setHookSpeed(hookSpeed);
        if (arm.getArmAngleEncoder() >= STARTING_ARM_ANGLE && hooks.bottomLimitPressed()) {
          stage = 1;
          cancel();
        }
        break;

      default:
        System.err.println("No case is true");
        break;
    }
  }

  @Override
  public void end(boolean interrupted) {
    // ingest.setIndexSpeed(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
