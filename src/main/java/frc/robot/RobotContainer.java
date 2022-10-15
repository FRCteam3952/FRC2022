// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.controllers.FlightJoystickController;
import frc.robot.subsystems.*;

public class RobotContainer {
  /**
   * KEY FOR AUTON BELOW:
   * <p>
   * 0: {@link frc.robot.commands.AutonomousOneBall}
   * <p>
   * 1: {@link frc.robot.commands.AutonomousTaxiOnly}
   * <p>
   * 2: {@link frc.robot.commands.AutonomousTwoBall}
   * <p>
   * 3: {@link frc.robot.commands.AutonomousTwoBallNoShoot}
   * <p>
   * 4: {@link frc.robot.commands.AutonomousTwoBallPreLoadedBottomHub}
   * <p>
   * 5: {@link frc.robot.commands.AutonomousTwoBallPreLoaded}
   * <p>
   * 6: {@link frc.robot.commands.AutonomousThreeBall}
   */
  public static final int autonToUse = 4;

  public static boolean inTeleop = true;

  /**
   * handleSwitchToAuton is for TESTING PURPOSES ONLY
   * when we switch to auton from teleop,
   * top index wheel speed is not set to 0 upon disable
   * so in {@link frc.robot.Robot#autonomousPeriodic()} we
   * use this to handle a one-time check to disable it
   */
  public static boolean handledSwitchToAuton = false;

  

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

  public static FlightJoystickController primaryJoystick = new FlightJoystickController(
      new Joystick(Constants.primaryJoystickPort));
  public static FlightJoystickController secondaryJoystick = new FlightJoystickController(
      new Joystick(Constants.secondaryJoystickPort));
  public static FlightJoystickController tertiaryJoystick = new FlightJoystickController(
      new Joystick(Constants.tertiaryJoystickPort)); // climb

  public static final ClimberHooks climberHooks = new ClimberHooks();
  public static final ClimberArm climberArm = new ClimberArm();
  public static final ControlArm controlArm = new ControlArm(climberArm);
  public static final ControlHooks controlHooks = new ControlHooks(climberHooks);
  public static AutoClimb autoClimb = new AutoClimb(climberHooks, climberArm);
  public static final ResetAutoClimb resetAutoClimb = new ResetAutoClimb();

  public static final BallHandling ballHandling = new BallHandling(shooter, bottomIndexer, topIndexer);
  public static final SetShooterPower setShooterPower = new SetShooterPower(shooter, limelight);
  public static final SetShooterPowerManual setShooterPowerManual = new SetShooterPowerManual(shooter);
  public static final AdjustShooterPowerManual adjustShooterPowerManual = new AdjustShooterPowerManual(shooter);

  public static final StartingConfig startingConfig = new StartingConfig(climberArm, climberHooks);

  public static final AutonomousTwoBall autonomousTwoBall = new AutonomousTwoBall(driveTrain, climberHooks, climberArm,
      shooter, bottomIndexer,
      topIndexer, limelight);
  public static final AutonomousOneBall autonomousOneBall = new AutonomousOneBall(driveTrain, climberHooks, climberArm,
      shooter, bottomIndexer, topIndexer, limelight);
  public static final AutonomousTaxiOnly autonomousTaxiOnly = new AutonomousTaxiOnly(driveTrain, climberHooks,
      climberArm, shooter, bottomIndexer, topIndexer, limelight);
  public static final AutonomousTwoBallNoShoot autonomousTwoBallNoShoot = new AutonomousTwoBallNoShoot(driveTrain,
      climberHooks, climberArm, shooter, bottomIndexer, topIndexer);
  public static final AutonomousTwoBallPreLoadedBottomHub autonomousTwoBallPreLoadedBottomHub = new AutonomousTwoBallPreLoadedBottomHub(driveTrain, climberHooks, climberArm, shooter, bottomIndexer, topIndexer);
  public static final AutonomousTwoBallPreLoaded autonomousTwoBallPreLoaded = new AutonomousTwoBallPreLoaded(driveTrain, climberHooks, climberArm, shooter, bottomIndexer, topIndexer, limelight);
  public static final AutonomousThreeBall autonomousThreeBall = new AutonomousThreeBall(driveTrain, climberHooks, climberArm, shooter, bottomIndexer, topIndexer, limelight);

  public static final ManualDrive manualDrive = new ManualDrive(driveTrain, limelight);

  // public static CvSink cvSink;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   * (it's supposed to but.... )
   */
  public RobotContainer() {
    // cvSink = CameraServer.getVideo("Front Camera");
    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA INITIALIZE");
    
    /*
    p.addListener(event -> {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA changed p to " + event.value.getValue());
        RobotContainer.shooter.pidController.setP(event.value.getDouble());
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    i.addListener(event -> {
        System.out.println("changed i to " + event.value.getValue());
        RobotContainer.shooter.pidController.setI(event.value.getDouble());
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    d.addListener(event -> {
        System.out.println("changed d to " + event.value.getValue());
        RobotContainer.shooter.pidController.setD(event.value.getDouble());
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    */
    // CameraServer.startAutomaticCapture();
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

    JoystickButton adjustShooterPowerManualButton = new JoystickButton(tertiaryJoystick.joystick,
        Constants.adjustShooterPowerManualButtonNumber);
    adjustShooterPowerManualButton.whenPressed(adjustShooterPowerManual);

    JoystickButton startingConfigButton = new JoystickButton(tertiaryJoystick.joystick,
        Constants.startingConfigButtonNumber);
    startingConfigButton.whileHeld(startingConfig);

    JoystickButton autoClimbButton = new JoystickButton(tertiaryJoystick.joystick,
        Constants.autoClimbButtonNumber);
    autoClimbButton.whileHeld(autoClimb);

    JoystickButton setShooterPowerButton = new JoystickButton(secondaryJoystick.joystick,
        Constants.setShooterPowerButtonNumber);
    setShooterPowerButton.whileHeld(setShooterPower);

    JoystickButton resetAutoClimbButton = new JoystickButton(tertiaryJoystick.joystick,
        Constants.resetAutoClimbButtonNumber);
    resetAutoClimbButton.whenPressed(resetAutoClimb);
  }

  public void autonomousInit() {
    inTeleop = false;

    topIndexer.setIndexSpeed(0);
    switch(autonToUse) {
      case 0:
        autonomousOneBall.schedule();
        System.out.println("auton one ball scheduled");
        break;
      case 1:
        autonomousTaxiOnly.schedule();
        System.out.println("auton taxi only scheduled");
        break;
      case 2:
        autonomousTwoBall.schedule();
        System.out.println("auton two ball scheduled");
        break;
      case 3:
        autonomousTwoBallNoShoot.schedule();
        System.out.println("auton two ball w/ no shoot scheduled");
        break;
      case 4:
        autonomousTwoBallPreLoadedBottomHub.schedule();
        break;
      case 5:
        autonomousTwoBallPreLoaded.schedule();
        break;
      case 6:
        autonomousThreeBall.schedule();
      default:
        break; // CHOOSE A DEFAULT EVEN THOUGH IT SHOULD NEVER BE HERE
    }
    
  }

  public void teleopInit() {
    inTeleop = true;
    
    autonomousOneBall.cancel();
    autonomousTaxiOnly.cancel();
    autonomousTwoBall.cancel();
    autonomousTwoBallNoShoot.cancel();

    configureButtonBindings();
    shooter.setDefaultCommand(ballHandling);
    driveTrain.setDefaultCommand(manualDrive);
    climberHooks.setDefaultCommand(controlHooks);
    climberArm.setDefaultCommand(controlArm);

  }
}
