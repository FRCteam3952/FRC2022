// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.networktables.NetworkTable;


/** An example command that uses an example subsystem. */
public class ShooterAimer extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive_train;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ShooterAimer(DriveTrain subsystem) {
    drive_train = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println("executing shooter aimer thingy");
    /*
    if(!RobotContainer.driverStick.joystick.getRawButtonPressed(1)){
      System.out.print("canceled");
    }
    */

    // System.out.print("IT WAS PRESED");
      double hor = 0;
      double lat = 0;

      // drive_train.drive(lat, hor);

      // System.out.println("Lat: " + lat + " Hor: "+ hor);

      float Kp = -0.1f;
      NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("limelight");
      float min_command = 0.5f;
      float tx = (float) networkTable.getEntry("tx").getDouble(100000);

      if(!(tx > 98000)) {
        //if(RobotContainer.driverStick.getTriggerPressed()) {
          float headingError = -tx;
          float steering_adjust = 0.0f;
          if(tx > 1.0) {
            steering_adjust = Kp*headingError - min_command;
          } else if(tx < 1.0) {
            steering_adjust = Kp*headingError + min_command;
          }
          lat += 2*(steering_adjust);
          System.out.println("button pressed");
        //}
      }
      // System.out.println("Lat: " + lat + " Hor: "+ hor);
      System.out.println("this is auto aim btw");
      drive_train.drive(lat, hor);
    }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
