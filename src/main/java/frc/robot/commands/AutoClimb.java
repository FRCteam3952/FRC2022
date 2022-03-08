// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Climber;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.CommandBase;
// this does climb auto
public class AutoClimb extends CommandBase {
  private final Climber climber;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  private double power;
  private int bottomCount;
  private int topCount;

  public AutoClimb(Climber subsystem) {
    climber = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
    power = 0.3;
    bottomCount = 0;
    topCount = 0;
  }


  

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      System.out.println("climbing");
      }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // climber.turnOnHookMotor();
    if (!RobotContainer.climberStick.rightShoulderPressed())
      cancel();

    if (bottomCount == 0) {
      double h = climber.slideHook(-power);
      double a = climber.changeArmAngle(-power); 
      System.out.println("1. Arm speed: " + a + " Hook speed: " + h);
      if (climber.bottomLimitPressed()) {
        bottomCount++;
        climber.slideHook(0);
        climber.changeArmAngle(0);
      }
    } //lift robot off of floor and position arm underneath second pole
    else if (topCount == 0) {
      double h = climber.slideHook(power);
      System.out.println("2. Arm speed: " + 0 + " Hook speed: " + h);      
      if (climber.topLimitPressed()) {
        topCount++;
        climber.slideHook(0);
        climber.changeArmAngle(0);
      }
    } //slide hooks up to attatch to second pole
    else if (bottomCount == 1) {
      double h = climber.slideHook(-power);
      System.out.println("3. Arm speed: 0 Hook speed: " + h);      
      if (climber.bottomLimitPressed()) {
        bottomCount++;
        climber.slideHook(0);
        climber.changeArmAngle(0);
      }
    } //slide robot up forward
    else if (topCount == 1) {
      double h = climber.slideHook(power);
      System.out.println("4. Arm speed: 0 Hook speed: " + h);      
      if (climber.topLimitPressed()) {
        cancel();
        System.out.println("auto finished, please return to manual drive by releasing right shoulder button");
        climber.slideHook(0);
        climber.changeArmAngle(0);
        
      }
    } //slide hooks back up
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    
    climber.slideHook(0);
    climber.changeArmAngle(0);
    bottomCount = 0;
    topCount = 0;
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return climber.getHookLimitSwitch();
    return false;
  }
}