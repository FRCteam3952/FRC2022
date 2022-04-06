// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.ADIS16470_IMU;


/** An example command that uses an example subsystem. */
public class ManualDrive extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive_train;
  private static ADIS16470_IMU gyro;
  private NetworkTableInstance inst;
  private NetworkTable table;
  private NetworkTableEntry ball;
  private NetworkTableEntry seeBall;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualDrive(DriveTrain subsystem) {
    drive_train = subsystem;

    gyro = new ADIS16470_IMU();
    gyro.setYawAxis(ADIS16470_IMU.IMUAxis.kY);

    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("Vision");
    ball = table.getEntry("PID");
    seeBall = table.getEntry("seeBall");
    addRequirements(drive_train);
    // Use addRequirements() here to declare subsystem dependencies.
  }
  

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double ySpeed = (RobotContainer.primaryJoystick.getLateralMovement());
    double xSpeed = (-RobotContainer.primaryJoystick.getHorizontalMovement());
    double zRotation = (-RobotContainer.primaryJoystick.getRotation());


    //System.out.println("y: " + ySpeed + " x: "+ xSpeed + " z: " + zRotation);

    if (RobotContainer.primaryJoystick.button2Pressed()) {

      // x and y movement adjustment values
      xSpeed += drive_train.getAdjustment()[0];
      ySpeed += drive_train.getAdjustment()[1];
      zRotation += drive_train.getAdjustment()[2];

      if (xSpeed > 1)
        xSpeed = 1;
      if (xSpeed < -1)
        xSpeed = -1;

      if (ySpeed > 1)
        ySpeed = 1;
      if (ySpeed < -1)
        ySpeed = -1;

    }

//seperate variable that will be permanent till done rotating
//boolean that will hold what is there 

    if (RobotContainer.secondaryJoystick.getLateralMovement() > 0 || RobotContainer.secondaryJoystick.getHorizontalMovement() > 0) {
      double y = RobotContainer.secondaryJoystick.getLateralMovement();
      double x =RobotContainer.secondaryJoystick.getHorizontalMovement();
      double angle = Math.atan2(y, x);
      if (x < 0)
        angle += 180;

      if ((360 - Math.abs(angle - gyro.getAngle()) > 180)) { //difference in angle greater than 180, turn counterclockwise // barggio said put the 360 - there
        angle = 360 - Math.abs(angle - gyro.getAngle());
        if (gyro.getAngle() > angle) {
          zRotation = 0.5;
        } else if (gyro.getAngle() < angle) {
          zRotation = -0.5;
        }
      }
      else { //difference in angle less than or equal to 180, turn clockwise
        if (gyro.getAngle() > angle) {
          zRotation = -0.5;
        } else if (gyro.getAngle() < angle) {
          zRotation = 0.5;
        }
      }
    }
    drive_train.drive(ySpeed, xSpeed, zRotation);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive_train.stopMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

}
