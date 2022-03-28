// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.ClimberArm;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
// this does climb auto
public class AutoClimb extends CommandBase {
  private enum ClimbingStates {
    LIFTING,
    SLIDE_HOOK_HIGH,
    MOVE_TO_HIGH,
    SLIDE_HOOK_TRAVERSAL_TOP,
    SLIDE_HOOK_TRAVERSAL_BOTTOM,
    TRAVERSE,
  }

  private final ClimberHooks hooks;
  private final ClimberArm arm;
  private final double MAX_POSITION = 50; //measured in motor rotations, measure later
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  private final double HOOK_POWER = 1;
  private final double ARM_POWER = 0.35;
  private ClimbingStates state = ClimbingStates.LIFTING;
  private Timer timer = new Timer();

  public AutoClimb(ClimberHooks hooks, ClimberArm arm) {
    this.hooks = hooks;
    this.arm = arm;
    addRequirements(hooks, arm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("climbing");
    state = ClimbingStates.LIFTING;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (state) {
      case LIFTING:
        hooks.slideHook(-HOOK_POWER);
        arm.changeArmAngle(-ARM_POWER);
        if (hooks.bottomLimitPressed()) {
          hooks.slideHook(0);
          arm.changeArmAngle(0);
          state = ClimbingStates.SLIDE_HOOK_HIGH;
        }
        break;
      
      case SLIDE_HOOK_HIGH:
        hooks.slideHook(HOOK_POWER);
        if (hooks.getEncoderPosition() >= MAX_POSITION) {
          hooks.slideHook(0);
          arm.changeArmAngle(0);
          state = ClimbingStates.MOVE_TO_HIGH;
        }
        break;
      
      case MOVE_TO_HIGH:
        hooks.slideHook(-HOOK_POWER);
        if (hooks.bottomLimitPressed()) {
          hooks.slideHook(0);
          arm.changeArmAngle(0);
          if (hooks.topOrBottomLimitPressed())
            state = ClimbingStates.SLIDE_HOOK_TRAVERSAL_TOP;
          else
            state = ClimbingStates.SLIDE_HOOK_TRAVERSAL_BOTTOM;
        }
        break;
      
      case SLIDE_HOOK_TRAVERSAL_TOP:
        hooks.slideHook(HOOK_POWER);
        arm.changeArmAngle(-ARM_POWER);
        if (hooks.getEncoderPosition() >= MAX_POSITION) {
          hooks.slideHook(0);
          arm.changeArmAngle(0);
          timer.reset();
          state = ClimbingStates.TRAVERSE;
        }
        break;
      
      case SLIDE_HOOK_TRAVERSAL_BOTTOM:
        hooks.slideHook(HOOK_POWER);
        if (hooks.getEncoderPosition() >= MAX_POSITION) {
          hooks.slideHook(0);
          arm.changeArmAngle(0);
          state = ClimbingStates.TRAVERSE;
        }
        break;

      case TRAVERSE:
        hooks.slideHook(-HOOK_POWER);
        if (hooks.getEncoderPosition() <= 40) {
          hooks.slideHook(0);
          cancel();
        }
        break;

      default:
        System.err.println("god forgive me");
    }
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    hooks.slideHook(0);
    arm.changeArmAngle(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
