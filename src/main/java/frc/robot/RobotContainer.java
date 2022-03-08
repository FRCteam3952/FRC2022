// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.ManualClimb;
import frc.robot.commands.ManualDrive;
import frc.robot.commands.SetShooterDistance;
import frc.robot.commands.ShooterAimer;
import frc.robot.commands.IngestBalls;
import frc.robot.commands.IndexBalls;
import frc.robot.commands.ShootBalls;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Ingester;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import frc.robot.controllers.*;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveTrain driveTrain = new DriveTrain();

  private final Shooter shooter = new Shooter();

  private final ShootBalls shootBalls = new ShootBalls(shooter);
 
  private final Ingester ingester = new Ingester();

  private final IngestBalls ingest = new IngestBalls(ingester);

  private final Indexer indexer = new Indexer();

  private final ManualDrive driveCommand = new ManualDrive(driveTrain);

  private final IndexBalls index = new IndexBalls(indexer, ingester);

  public static RemoteController climberStick = new RemoteController(new JoystickPlus(0));

  public static FlightJoystickController driverStick = new FlightJoystickController(new JoystickPlus(1));

  private final AutonomousCommand autonomousCommand = new AutonomousCommand(driveTrain, shootBalls);

  private final Climber climber = new Climber();

  private final ManualClimb manualClimb = new ManualClimb(climber);
  
  //declare new shooter airmer to be ran, for driveTrain
  private final SetShooterDistance setShooterDistance = new SetShooterDistance(driveTrain);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    ingester.setDefaultCommand(ingest);
    climber.setDefaultCommand(manualClimb);
    driveTrain.setDefaultCommand(driveCommand);
    indexer.setDefaultCommand(index);
    shooter.setDefaultCommand(shootBalls);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    /*
    coDriverStick.btn_1.whileHeld(shootBall);
    coDriverStick.btn_12.whenPressed(turretPresets);
    */
    //controller might not actually have any buttons, might be useless code
    /*JoystickButton dL = new JoystickButton(driverStick.joystick, 1);
    dL.whenPressed(servoMove); */
    //JoystickButton collectButton = new JoystickButton(driverStick.joystick, 1);
    //collectButton.whileHeld(autonomousCommand);


    //when press button "1" on frc will run shooterAimer, follow shooterAimer for more info
    // Joystick joystick = new Joystick(0);
    JoystickButton joystickButton = new JoystickButton(driverStick.joystick, 1);
    System.out.print("joystick made");
    joystickButton.whenHeld(setShooterDistance);
    
    

    

    /**
   * Get the slider position of the HID.
   *
   * @return the z position
   */

    //dR.whenPressed(servoMove);
    //JoystickButton manualButton = new JoystickButton(driverStick.joystick, 2);
    //manualButton.whenHeld(autoClimb);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return autonomousCommand;
  }
}
