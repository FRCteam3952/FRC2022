// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.cscore.CvSink;
import frc.robot.commands.ManualDrive;
import frc.robot.commands.SetShooterPower;
import frc.robot.commands.SetShooterPowerManual;
import frc.robot.commands.ControlArm;
import frc.robot.commands.ControlHooks;
import frc.robot.commands.StartingConfig;
import frc.robot.commands.BallHandling;
import frc.robot.commands.AutoClimb;
import frc.robot.commands.ResetAutoClimb;
import frc.robot.commands.Autonomous;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.TopIndexer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Limelight;

import frc.robot.controllers.FlightJoystickController;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;

public class RobotContainer {
  public static boolean inTeleop = true;

  public static final DriveTrain driveTrain = new DriveTrain();

  public static final Gyro gyro = new Gyro();
  public static final Limelight limelight = new Limelight();

  public static final BottomIndexer bottomIndexer = new BottomIndexer();

  public static final Shooter shooter = new Shooter();
  public static final TopIndexer topIndexer = new TopIndexer();

  /**
   * primaryJoystick - driving and turning
   * secondaryJoystick - shooting and setting angle
   * tertiary joystick - climbing
   */

  public static FlightJoystickController primaryJoystick = new FlightJoystickController(new Joystick(Constants.primaryJoystickPort));
  public static FlightJoystickController secondaryJoystick = new FlightJoystickController(new Joystick(Constants.secondaryJoystickPort));
  public static FlightJoystickController tertiaryJoystick = new FlightJoystickController(new Joystick(Constants.tertiaryJoystickPort)); // climb

  public static final ClimberHooks climberHooks = new ClimberHooks();
  public static final ClimberArm climberArm = new ClimberArm();
  public static final ControlArm controlArm = new ControlArm(climberArm);
  public static final ControlHooks controlHooks = new ControlHooks(climberHooks);
  public static AutoClimb autoClimb = new AutoClimb(climberHooks, climberArm);
  public static final ResetAutoClimb resetAutoClimb = new ResetAutoClimb();

  public static final BallHandling ballHandling = new BallHandling(shooter, bottomIndexer, topIndexer);
  public static final SetShooterPower setShooterPower = new SetShooterPower(shooter, limelight);
  public static final SetShooterPowerManual setShooterPowerManual = new SetShooterPowerManual(shooter);

  public static final StartingConfig startingConfig = new StartingConfig(climberArm, climberHooks);

  public static final Autonomous autonomous = new Autonomous(driveTrain, climberHooks, climberArm, shooter, bottomIndexer,
      topIndexer);
  public static final ManualDrive manualDrive = new ManualDrive(driveTrain, limelight);

  //public static CvSink cvSink;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands. (it's supposed to but....  )
   */
  public RobotContainer() {
    //cvSink = CameraServer.getVideo("Front Camera");

    CameraServer.startAutomaticCapture();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  public void configureButtonBindings() {

    JoystickButton setShooterPowerManualButton = new JoystickButton(secondaryJoystick.joystick,
        Constants.setShooterManualButtonNumber);
    setShooterPowerManualButton.whileHeld(setShooterPowerManual);

    JoystickButton startingConfigButton = new JoystickButton(tertiaryJoystick.joystick, Constants.startingConfigButtonNumber);
    startingConfigButton.whileHeld(startingConfig);

    JoystickButton autoClimbButton = new JoystickButton(tertiaryJoystick.joystick,
        Constants.autoClimbButtonNumber);
    autoClimbButton.whileHeld(autoClimb);

    JoystickButton setShooterPowerButton = new JoystickButton(secondaryJoystick.joystick,
        Constants.setShooterPowerButtonNumber);
    setShooterPowerButton.whileHeld(setShooterPower);

    JoystickButton resetAutoClimbButton = new JoystickButton(tertiaryJoystick.joystick, Constants.resetAutoClimbButtonNumber);
    resetAutoClimbButton.whenPressed(resetAutoClimb);
  }

  public void autonomousInit() {
    inTeleop = false;
    autonomous.schedule();
  }

  public void teleopInit() {
    inTeleop = true;
    autonomous.cancel();
    configureButtonBindings();
    shooter.setDefaultCommand(ballHandling);
    driveTrain.setDefaultCommand(manualDrive);
    climberHooks.setDefaultCommand(controlHooks);
    climberArm.setDefaultCommand(controlArm);

  }
}
