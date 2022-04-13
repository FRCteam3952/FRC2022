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
// this does climb auto
public class AutoClimb extends CommandBase {
  private enum ClimbingStates {
    LIFTING_NO_ANGLE,
    LIFTING_WITH_ANGLE,
    MOVE_TO_HIGH,
    SEND_HOOKS_UP,
    MOVE_TO_HIGHS,
    SEND_HOOKS_UP2,
    TRAVERSE,
    WAIT
  }
  private final Timer timer = new Timer();

  private final ClimberHooks hooks;
  private final ClimberArm arm;
  private final double MAX_POSITION = 282.5; //measured in motor rotations, measure later
  private final double ARM_MOVE_POSITION = 25; //measured in motor rotations, measure later

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  private final double HOOK_POWER = 1;
  private final double ARM_POWER = 0.5;
  private ClimbingStates state = ClimbingStates.LIFTING_NO_ANGLE;
  private final double climbingAngle = 46; //degrees for climbing under the high bar

  private final double kP = 0.2; //multiplicative
  private final double kI = 0; //coefficient for integral in PID
  private final double kD = 0; //coefficient for derivative in PID

  private boolean inPos = false;
  private boolean correctAngle = false;


  public AutoClimb(ClimberHooks hooks, ClimberArm arm) {
    this.hooks = hooks;
    this.arm = arm;
    addRequirements(hooks, arm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("climbing");
    //state = ClimbingStates.LIFTING_NO_ANGLE;
    correctAngle = false;
    inPos = false;
    timer.start();
  }
  public double maintainClimberAngle(){
    //calculate how off the current climber angle is off from the wanted angle
    double climberAngle = arm.getArmAngleEncoder();
    double adjustment = (climbingAngle - climberAngle) * kP;
    //keeps adjustment between -1 and 1
    if(adjustment > 1)
      adjustment = 1;
    else if(adjustment < -1)
      adjustment = -1;
    return adjustment;
    
  }

  public boolean checkHookandAngle(double anglePos, double hookPos){
    double angleSpeed = arm.getArmSpeed();
    double hookSpeed = hooks.getHookSpeed();
    int truth_count = 0;
    if(angleSpeed >= 0 && arm.getArmAngleEncoder() >= anglePos){
      arm.changeArmAngle(0);
      System.out.println("angle done");
      truth_count++;
    }
    else if(angleSpeed <= 0 && arm.getArmAngleEncoder() <= anglePos){
      arm.changeArmAngle(0);
      System.out.println("angle done");
      truth_count++;
    }
    if(hookSpeed >= 0 && hooks.getEncoderPosition() <= hookPos){
      hooks.setHookSpeed(0);
      System.out.println("hooks done");
      truth_count++;
    }
    else if(hookSpeed <= 0 && hooks.getEncoderPosition() >= hookPos){
      hooks.setHookSpeed(0);
      System.out.println("hooks done");
      truth_count++;
    }
    return (truth_count == 2);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (RobotContainer.tertiaryJoystick.joystick.getRawButtonPressed(Constants.resetAutoClimbButtonNumber)) {
      state = ClimbingStates.LIFTING_NO_ANGLE;
    }
    switch (state) {
      case LIFTING_NO_ANGLE:
        arm.changeArmAngle(0);
        hooks.setHookSpeed(0.6);
        if (hooks.getEncoderPosition() < 110){
          arm.changeArmAngle(-1);
          hooks.setHookSpeed(0.4);
          state = ClimbingStates.LIFTING_WITH_ANGLE;
        }
        break;
      case LIFTING_WITH_ANGLE:
        //System.out.println("angle: " + arm.getArmAngleEncoder());
        if (checkHookandAngle(climbingAngle, 30)){
          System.out.println("I've hit 30");
          hooks.setHookSpeed(0.5);
          arm.changeArmAngle(1);
          state = ClimbingStates.MOVE_TO_HIGH;
        }
      break;
      case MOVE_TO_HIGH:
        System.out.println("GO UNDER THE BAR");
        if(checkHookandAngle(90,0)){
          System.out.println("DONE");
          state = ClimbingStates.SEND_HOOKS_UP;
        }
      break;

      case SEND_HOOKS_UP:
        hooks.setHookSpeed(-0.8);
        if(hooks.getEncoderPosition() > MAX_POSITION){
          hooks.setHookSpeed(0);
          timer.reset();
          state = ClimbingStates.MOVE_TO_HIGHS;
        }
      break;   

      case MOVE_TO_HIGHS:
        if(timer.hasElapsed(3)){
          hooks.setHookSpeed(0.6);
        }
        if(hooks.bottomLimitPressed()){
          hooks.setHookSpeed(0);
          state = ClimbingStates.WAIT;
        }
      break;   

      case SEND_HOOKS_UP2:
        hooks.setHookSpeed(-0.8);
        if (hooks.getEncoderPosition() > 100)
          arm.changeArmAngle(0.5);
        if (hooks.getEncoderPosition() >= MAX_POSITION) {
          state = ClimbingStates.TRAVERSE;
          timer.reset();
        }
      break;

      case TRAVERSE:
        hooks.setHookSpeed(0.8);
        if (timer.hasElapsed(0.8)) {
          hooks.setHookSpeed(0);
          state = ClimbingStates.WAIT;
        }
        
      default:
        //System.err.println("No state is true");
        break;
    }
  }


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    hooks.setHookSpeed(0);
    arm.changeArmAngle(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
