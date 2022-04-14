// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.LimeLight;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * self explanatory
 */
public class ManualDrive extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  private final DriveTrain drive_train;
  private final LimeLight lime_light;
  private double microPP = -0.1; // microPinpointPositioning™

  public ManualDrive(DriveTrain subsystem, LimeLight limey) {
    drive_train = subsystem;
    lime_light = limey;
    addRequirements(drive_train, lime_light);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    //input joystick controls
    double ySpeed = (RobotContainer.primaryJoystick.getLateralMovement());
    double xSpeed = (-RobotContainer.primaryJoystick.getHorizontalMovement());
    double zRotation = (-RobotContainer.primaryJoystick.getRotation());

    // Angle Adjustment Code
    if (RobotContainer.secondaryJoystick.getLateralMovement() != 0 || RobotContainer.secondaryJoystick.getHorizontalMovement() != 0) {
      // set angle
      System.out.println("setting angle");
      double y = -RobotContainer.secondaryJoystick.getLateralMovement();
      double x = RobotContainer.secondaryJoystick.getHorizontalMovement();
      double angle = Math.toDegrees(Math.atan2(y, x)); // gets angle of the joystick
      if (y < 0)
        angle += 360; // make sure angle is within 0˚ to 360˚ scale
      angle += angle < 0 ? 270 : -90;
      zRotation = drive_train.setAngle(angle);
    }
    else{
      // adjust movement to limelight target
      if(RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.adjustAimButtonNumber)){
        lime_light.turnOnLED();
        double angleAdjust = lime_light.getAdjustment();
        zRotation += angleAdjust;
      }
      else{
        lime_light.turnOffLED();
      }
      // adjust movement of robot towards ball
      if (RobotContainer.primaryJoystick.joystick.getRawButton(Constants.aimbotToBallButtonNumber)) {
        double angleAdjust = drive_train.getAdjustment();
        zRotation += angleAdjust;
      }
    }


    // microadjustment
    if (RobotContainer.primaryJoystick.getJoystickPOV() == 0) {
      ySpeed += microPP;
    } else if (RobotContainer.primaryJoystick.getJoystickPOV() == 315) {
      ySpeed += microPP;
      xSpeed -= microPP;
    } else if (RobotContainer.primaryJoystick.getJoystickPOV() == 270) {
      xSpeed -= microPP;
    } else if (RobotContainer.primaryJoystick.getJoystickPOV() == 225) {
      ySpeed -= microPP;
      xSpeed -= microPP;
    } else if (RobotContainer.primaryJoystick.getJoystickPOV() == 180) {
      ySpeed -= microPP;
    } else if (RobotContainer.primaryJoystick.getJoystickPOV() == 135) {
      ySpeed -= microPP;
      xSpeed += microPP;
    } else if (RobotContainer.primaryJoystick.getJoystickPOV() == 90) {
      xSpeed += microPP;
    } else if (RobotContainer.primaryJoystick.getJoystickPOV() == 45) {
      ySpeed += microPP;
      xSpeed += microPP;
    }

    drive_train.drive(ySpeed, xSpeed, zRotation);

  }

  @Override
  public void end(boolean interrupted) {
    drive_train.stopMotors();
  }

  @Override
  public boolean isFinished() {
    return false;
  }

}
