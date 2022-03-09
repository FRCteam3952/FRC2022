// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AutoClimb;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.ManualClimb;
import frc.robot.commands.ManualDrive;
import frc.robot.commands.SetShooterDistance;
import frc.robot.commands.ShooterAimer;
import frc.robot.commands.IngestBalls;
import frc.robot.commands.IndexBalls;
import frc.robot.commands.ShootBalls;
import frc.robot.commands.AdjustShooter;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Ingester;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import frc.robot.controllers.*;
public class RobotContainer {
  private final DriveTrain driveTrain = new DriveTrain();
  private final ManualDrive driveCommand = new ManualDrive(driveTrain);

  private final Ingester ingester = new Ingester();
  private final IngestBalls ingest = new IngestBalls(ingester);

  private final Indexer indexer = new Indexer();
  private final IndexBalls index = new IndexBalls(indexer, ingester);

  private final Shooter shooter = new Shooter();
  private final ShootBalls shootBalls = new ShootBalls(shooter, indexer);
 
  public static RemoteController climberStick = new RemoteController(new JoystickPlus(0));
  public static FlightJoystickController driverStick = new FlightJoystickController(new JoystickPlus(1));

  private final AutonomousCommand autonomousCommand = new AutonomousCommand(driveTrain, shootBalls);

  private final Climber climber = new Climber();
  private final ManualClimb manualClimb = new ManualClimb(climber);
  private final AutoClimb autoClimb = new AutoClimb(climber);
  
  //declare new shooter airmer to be ran, for driveTrain
  private final AdjustShooter adjustShooter = new AdjustShooter(driveTrain);
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    // ingester.setDefaultCommand(ingest);
    // climber.setDefaultCommand(manualClimb);
    // driveTrain.setDefaultCommand(driveCommand);
    // indexer.setDefaultCommand(index);

    CameraServer.startAutomaticCapture();
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
    JoystickButton shooterButton = new JoystickButton(driverStick.joystick, 1);
    shooterButton.whenHeld(adjustShooter);
    
    Trigger autoTrigger = new Trigger(() -> climberStick.getSlider() > .5);
    autoTrigger.whenActive(autoClimb);
    autoTrigger.whenInactive(manualClimb);

    // JoystickButton spitBallButton = new JoystickButton(driverStick.joystick, 6);
    // spitBallButton.whenHeld();
    // spitBallButton.whenReleased();

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
