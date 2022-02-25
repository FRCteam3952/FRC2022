// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Climber;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoClimb extends CommandBase {
  private final Climber climber;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  private double power;

  public AutoClimb(Climber subsystem) {
    climber = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
    power = 0.5;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      System.out.println("climbing");
      RobotContainer.driverStick.setChannelForStickButton();
      }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (RobotContainer.driverStick.getRotation()>0)
    {
      System.out.println("climb gh\\thing wheee");
      // climber.turnOnHookMotor();
      while (!climber.bottomLimitPressed()) {
        climber.slideHook(-power);
        climber.changeArmAngle(-power); 
      } //lift robot off of floor and position arm underneath second pole
      
      climber.slideHook(0); //reset motor
      climber.changeArmAngle(0); //reset motor

      while (!climber.topLimitPressed()) {
        climber.slideHook(power);
        climber.changeArmAngle(power);
      } //position arm directly underneath second pole and slide hooks up to attatch to second pole

      climber.slideHook(0); //reset motor
      climber.changeArmAngle(0);  //reset motor
      
      while (!climber.bottomLimitPressed()) {
        climber.slideHook(-power);
      } //slide robot up forward

      climber.slideHook(0); //reset motor

      while (!climber.bottomLimitPressed()) {
        climber.slideHook(power);
      } //slide hooks back up to the top

    //either manual control or use third limit switch  
      if (climber.topOrBottomLimitPressed())  
        while (!climber.bottomLimitPressed()) {
          climber.slideHook(power);
          climber.changeArmAngle(-power);
        }   
      else
        while (!climber.bottomLimitPressed()) {
          climber.slideHook(power);
          climber.changeArmAngle(-power);
        }
      
      climber.slideHook(0); //reset motor
      climber.changeArmAngle(0);  //reset motor
    }
    else{
      end(true);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    //boolean manualButton = RobotContainer.
    //double hor = RobotContainer.driverStick.getHorizontalMovement();
    //while(hor > 0) {
    
    //}
    climber.slideHook(0);
    climber.changeArmAngle(0);
      
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return climber.getHookLimitSwitch();
    return true;
  }
}