// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.ClimberArm;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Gyro;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
// this does climb auto
public class AutoClimb extends CommandBase {
  private enum ClimbingStates {
    LIFTING,
    LIFTING_WITH_ARM,
    SLIDE_HOOK_HIGH,
    MOVE_TO_HIGH,
    WAIT
  }

  private final ClimberHooks hooks;
  private final ClimberArm arm;
  private final double MAX_POSITION = 50; //measured in motor rotations, measure later
  private final double ARM_MOVE_POSITION = 25; //measured in motor rotations, measure later
  private final Gyro gyro;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  private final double HOOK_POWER = 1;
  private final double ARM_POWER = 0.35;
  private ClimbingStates state = ClimbingStates.LIFTING;
  private final double climbingAngle = 50; //degress for climbing under the high bar

  private final double kP = 0.5;

  public AutoClimb(ClimberHooks hooks, ClimberArm arm, Gyro gyro) {
    this.hooks = hooks;
    this.arm = arm;
    this.gyro = gyro;
    addRequirements(hooks, arm, gyro);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("climbing");
    state = ClimbingStates.LIFTING;
    gyro.setGyroAxis(ADIS16470_IMU.IMUAxis.kZ);
  }
  public double maintainClimberAngle(){
    //calculate how off the current climber angle is off from the wanted angle
    double climberAngle = arm.getArmAngleEncoder() + gyro.getGyroAngle();
    double adjustment = (climberAngle - climbingAngle)*kP;

    //keeps adjustment between -1 and 1
    if(adjustment > 1)
      adjustment = 1;
    else if(adjustment < -1)
      adjustment = -1;

    System.out.println(climberAngle);
    return adjustment;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (state) {
      case LIFTING:
        hooks.setHookSpeed(-HOOK_POWER);
        arm.changeArmAngle(maintainClimberAngle());
        if (hooks.bottomLimitPressed()){
          hooks.setHookSpeed(0);
          arm.changeArmAngle(0);
          state = ClimbingStates.WAIT;
        }
        break;
      case LIFTING_WITH_ARM:
        hooks.setHookSpeed(-HOOK_POWER);
        arm.changeArmAngle(-ARM_POWER);
        if (hooks.bottomLimitPressed()) {
          hooks.setHookSpeed(0);
          arm.changeArmAngle(0);
          state = ClimbingStates.SLIDE_HOOK_HIGH;
        }
        break;
      case SLIDE_HOOK_HIGH:
        hooks.setHookSpeed(HOOK_POWER);
        if (hooks.getEncoderPosition() >= MAX_POSITION) {
          hooks.setHookSpeed(0);
          arm.changeArmAngle(0);
          state = ClimbingStates.MOVE_TO_HIGH;
        }
        break;
      
      case MOVE_TO_HIGH:
        hooks.setHookSpeed(-HOOK_POWER);
        if (hooks.bottomLimitPressed()) {
          hooks.setHookSpeed(0);
          arm.changeArmAngle(0);
          if (hooks.bottomLimitPressed()) {
            System.out.println("AutoClimb over, please start manual climbing");
            cancel();
          }
        }
        break;
      
      default:
        System.err.println("No state is true");
        break;
    }
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    hooks.setHookSpeed(0);
    arm.changeArmAngle(0);
    gyro.setGyroAxis(ADIS16470_IMU.IMUAxis.kY);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
