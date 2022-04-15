// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.ManualDrive;
import frc.robot.commands.SetShooterPower;
import frc.robot.commands.SetShooterPowerManual;
import frc.robot.commands.ControlArm;
import frc.robot.commands.ControlHooks;
import frc.robot.commands.StartingConfig;
import frc.robot.commands.BallHandling;
import frc.robot.commands.AdjustShooterAim;
import frc.robot.commands.AutoClimb;
import frc.robot.commands.Autonomous;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.TopIndexer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Gyro;
import frc.robot.subsystems.Tachometer;
import frc.robot.subsystems.LimeLight;

import frc.robot.controllers.FlightJoystickController;
import edu.wpi.first.wpilibj.Joystick;

public class RobotContainer {
  public final static Tachometer tachometer = new Tachometer();
  public static boolean inTeleop = true;
  public final static DriveTrain driveTrain = new DriveTrain();

  public final static Gyro gyro = new Gyro();
  public final static LimeLight limelight = new LimeLight();

  public final static BottomIndexer bottomIndexer = new BottomIndexer();

  public final static Shooter shooter = new Shooter();
  public final static TopIndexer topIndexer = new TopIndexer();

  /**
   * primaryJoystick - driving and turning
   * secondaryJoystick - shooting and setting angle
   * tertiary joystick - climbing
   */

  public static FlightJoystickController primaryJoystick = new FlightJoystickController(new Joystick(0));
  public static FlightJoystickController secondaryJoystick = new FlightJoystickController(new Joystick(1));
  public static FlightJoystickController tertiaryJoystick = new FlightJoystickController(new Joystick(2)); // climb

  public final static ClimberHooks hooks = new ClimberHooks();
  public final static ClimberArm arm = new ClimberArm();
  public final static ControlArm controlArm = new ControlArm(arm);
  public final static ControlHooks controlHooks = new ControlHooks(hooks);
  public final static AutoClimb autoClimb = new AutoClimb(hooks, arm);

  public final static BallHandling shootBalls = new BallHandling(shooter, bottomIndexer, topIndexer);
  public final static AdjustShooterAim adjustShooterAim = new AdjustShooterAim(driveTrain);
  public final static SetShooterPower setShooterPower = new SetShooterPower(shooter, driveTrain);
  public final static SetShooterPowerManual setShooterPowerManual = new SetShooterPowerManual(shooter);

  public final static StartingConfig startingConfigCmd = new StartingConfig(bottomIndexer, arm, hooks);

  public final static Autonomous autonomous = new Autonomous(driveTrain, hooks, arm, shooter, bottomIndexer, topIndexer);
  public final static ManualDrive driveCommand = new ManualDrive(driveTrain, limelight);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

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

    JoystickButton setShooterManualButton = new JoystickButton(secondaryJoystick.joystick, Constants.setShooterManualButtonNumber);
    setShooterManualButton.whileHeld(setShooterPowerManual);

    JoystickButton startingConfig = new JoystickButton(tertiaryJoystick.joystick, Constants.startingConfigButtonNumber);
    startingConfig.whileHeld(startingConfigCmd);

    JoystickButton activateAutoClimbButton = new JoystickButton(tertiaryJoystick.joystick, Constants.activateAutoClimbButtonNumber);
    activateAutoClimbButton.whileHeld(autoClimb);
  }

  public void autonomousInit() {
    inTeleop = false;
    autonomous.schedule();
  }

  public void teleopInit() {
    inTeleop = true;
    autonomous.cancel();
    configureButtonBindings();
    shooter.setDefaultCommand(shootBalls);
    driveTrain.setDefaultCommand(driveCommand);
    hooks.setDefaultCommand(controlHooks);
    arm.setDefaultCommand(controlArm);

  }
}
