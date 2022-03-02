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
    power = 0.5;
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
    System.out.println("climb");
      // climber.turnOnHookMotor();
      if (bottomCount == 0) {
        climber.slideHook(-power);
        climber.changeArmAngle(-power); 
        System.out.println("climb1");
        if (climber.bottomLimitPressed()) 
          bottomCount++;
      } //lift robot off of floor and position arm underneath second pole
      else{
        if (topCount == 0) {
          climber.slideHook(power);
          climber.changeArmAngle(power);
          System.out.println("climb2");
          if (climber.topLimitPressed())
            topCount++;
        } //position arm directly underneath second pole and slide hooks up to attatch to second pole
        else{
          if (bottomCount == 1) {
            climber.slideHook(-power);
            System.out.println("climb3");
            if (climber.bottomLimitPressed())
              bottomCount++;
          } //slide robot up forward
          else{
            if (topCount == 1) {
              climber.slideHook(power);
              System.out.println("climb4");
              topCount++;
            }
          }
          
    
          climber.slideHook(0); //reset motor
        }
        System.out.println("notClimb2");
        climber.slideHook(0); //reset motor
        climber.changeArmAngle(0);  //reset motor
      }
      System.out.println("notClimb1");
      climber.slideHook(0); //reset motor
      climber.changeArmAngle(0); //reset motor
      
       //slide hooks back up to the top

      //twas decided we will use manual control
    /*either manual control or use third limit switch  
      if (climber.topOrBottomLimitPressed())  
        while (!climber.bottomLimitPressed() && auto) {
          climber.slideHook(power);
          climber.changeArmAngle(-power);
          System.out.println("climb5");
        }   
      else
        while (!climber.bottomLimitPressed() && auto) {
          climber.slideHook(power);
          climber.changeArmAngle(-power);
          System.out.println("climb5");
        }
      
      climber.slideHook(0); //reset motor
      climber.changeArmAngle(0);  //reset motor
    */
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