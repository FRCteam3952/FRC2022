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
  private final BottomIndexer bottomIndexer;
  private final ClimberArm climberArm;
  private final ClimberHooks climberHooks;

  private final double ARM_SPEED = 0.5;
  private final double HOOK_SPEED = 0.6;
  private final double STARTING_ARM_ANGLE = 45; // find later

  /**
   * stage 1 sets the servo and arm to limit switch (i believe)
   * stage 2 sets the arm to the correct angle and the hooks to the limit switch
   */
  private int stage = 1;

  public StartingConfig(BottomIndexer bottomIndexer, ClimberArm climberArm, ClimberHooks climberHooks) {
    this.bottomIndexer = bottomIndexer;
    this.climberArm = climberArm;
    this.climberHooks = climberHooks;

    addRequirements(bottomIndexer, climberArm, climberHooks);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    switch (stage) {
      case 1:
        bottomIndexer.setServoRotation(-1);

        if (!climberArm.climberArmAngleLimitPressed()) {
          climberArm.setArmSpeed(-ARM_SPEED);
        } else {
          climberArm.setArmSpeed(0);
          climberArm.resetArmAngleEncoder();
          stage = 2;
        }

        break;

      case 2:
        if (climberArm.getArmAngleEncoder() < STARTING_ARM_ANGLE)
          climberArm.setArmSpeed(ARM_SPEED);
        if (!climberHooks.bottomLimitPressed())
          climberHooks.setHookSpeed(HOOK_SPEED);
        if (climberArm.getArmAngleEncoder() >= STARTING_ARM_ANGLE && climberHooks.bottomLimitPressed()) {
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
