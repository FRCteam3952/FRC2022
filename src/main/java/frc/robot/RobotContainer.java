// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;

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

  public final static DriveTrain driveTrain = new DriveTrain();

  public final static Gyro gyro = new Gyro();
  public final static Limelight limelight = new Limelight();

  public final static BottomIndexer bottomIndexer = new BottomIndexer();

  public final static Shooter shooter = new Shooter();
  public final static TopIndexer topIndexer = new TopIndexer();

  /**
   * primaryJoystick - driving and turning
   * secondaryJoystick - shooting and setting angle
   * tertiary joystick - climbing
   */

  public static FlightJoystickController primaryJoystick = new FlightJoystickController(new Joystick(Constants.primaryJoystickPort));
  public static FlightJoystickController secondaryJoystick = new FlightJoystickController(new Joystick(Constants.secondaryJoystickPort));
  public static FlightJoystickController tertiaryJoystick = new FlightJoystickController(new Joystick(Constants.tertiaryJoystickPort)); // climb

  public final static ClimberHooks climberHooks = new ClimberHooks();
  public final static ClimberArm climberArm = new ClimberArm();
  public final static ControlArm controlArm = new ControlArm(climberArm);
  public final static ControlHooks controlHooks = new ControlHooks(climberHooks);
  public final static AutoClimb autoClimb = new AutoClimb(climberHooks, climberArm);
  public final static ResetAutoClimb resetAutoClimb = new ResetAutoClimb();

  public final static BallHandling ballHandling = new BallHandling(shooter, bottomIndexer, topIndexer);
  public final static SetShooterPower setShooterPower = new SetShooterPower(shooter, limelight);
  public final static SetShooterPowerManual setShooterPowerManual = new SetShooterPowerManual(shooter);

  public final static StartingConfig startingConfig = new StartingConfig(climberArm, climberHooks);

  public final static Autonomous autonomous = new Autonomous(driveTrain, climberHooks, climberArm, shooter, bottomIndexer,
      topIndexer);
  public final static ManualDrive manualDrive = new ManualDrive(driveTrain, limelight);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
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
