// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * self explanatory
 */
public class ManualDrive extends CommandBase {
  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
  
  private final DriveTrain driveTrain;
  private final Limelight limelight;
  
  private double MICRO_PP = -0.15; // MicroPinpointPositioning™

  public ManualDrive(DriveTrain driveTrain, Limelight limey) {
    this.driveTrain = driveTrain;
    this.limelight = limey;

    addRequirements(driveTrain, limey);
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

    //set Team
    driveTrain.setTeam();
   // System.out.println(Gyro.getGyroAngle());

    // Angle Adjustment Code
    if (RobotContainer.secondaryJoystick.getLateralMovement() != 0 || RobotContainer.secondaryJoystick.getHorizontalMovement() != 0) {
      // set angle
      //System.out.println("setting angle");
      double y = -RobotContainer.secondaryJoystick.getLateralMovement();
      double x = RobotContainer.secondaryJoystick.getHorizontalMovement();
      double angle = Math.toDegrees(Math.atan2(y, x)); // gets angle of the joystick

      if (y < 0) {
        angle += 360; // make sure angle is within 0˚ to 360˚ scale
      }

      angle += angle < 0 ? 270 : -90;
      zRotation = driveTrain.setAngle(angle);
    } else {
      // adjust movement to limelight target
      if(RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.adjustAimButtonNumber)){
        limelight.turnOnLED();
        double angleAdjust = limelight.getAdjustment();
        //System.out.println(limelight.getDistance());
        zRotation += angleAdjust;
      } else {
        limelight.turnOffLED();
      }
      // adjust movement of robot towards ball
      if (RobotContainer.primaryJoystick.joystick.getRawButton(Constants.aimbotToBallButtonNumber)|| RobotContainer.primaryJoystick.joystick.getRawButton(Constants.rollIngesterButtonNumber)) {
        double angleAdjust = driveTrain.getAdjustment();
        zRotation += angleAdjust;
      }
    }

    // microadjustment
    if (RobotContainer.primaryJoystick.joystick.getPOV() == 0) {
      ySpeed += MICRO_PP;
    } else if (RobotContainer.primaryJoystick.joystick.getPOV() == 315) {
      ySpeed += MICRO_PP;
      xSpeed -= MICRO_PP;
    } else if (RobotContainer.primaryJoystick.joystick.getPOV() == 270) {
      xSpeed -= MICRO_PP - 0.05;
    } else if (RobotContainer.primaryJoystick.joystick.getPOV() == 225) {
      ySpeed -= MICRO_PP;
      xSpeed -= MICRO_PP;
    } else if (RobotContainer.primaryJoystick.joystick.getPOV() == 180) {
      ySpeed -= MICRO_PP;
    } else if (RobotContainer.primaryJoystick.joystick.getPOV() == 135) {
      ySpeed -= MICRO_PP;
      xSpeed += MICRO_PP;
    } else if (RobotContainer.primaryJoystick.joystick.getPOV() == 90) {
      xSpeed += MICRO_PP + 0.05;
    } else if (RobotContainer.primaryJoystick.joystick.getPOV() == 45) {
      ySpeed += MICRO_PP;
      xSpeed += MICRO_PP;
    }

    driveTrain.drive(ySpeed, xSpeed, zRotation);

  }

  @Override
  public void end(boolean interrupted) {
    driveTrain.stopMotors();
  }

  @Override
  public boolean isFinished() {
    return false;
  }

}
