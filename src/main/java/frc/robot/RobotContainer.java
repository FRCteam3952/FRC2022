// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AutoClimb;
import frc.robot.commands.AutonomousGroup;
import frc.robot.commands.AutonomousSetup;
import frc.robot.commands.ControlArm;
import frc.robot.commands.FlywheelShooter;
import frc.robot.commands.ManualClimb;
import frc.robot.commands.ManualDrive;
import frc.robot.commands.SetShooterDistance;
import frc.robot.commands.ShooterAimer;
import frc.robot.commands.UnlockIngester;
import frc.robot.commands.IngestBalls;
import frc.robot.commands.IndexBalls;
import frc.robot.commands.ShootBalls;
import frc.robot.commands.AdjustShooter;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Ingester;
import frc.robot.subsystems.IngesterPositioner;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import frc.robot.controllers.*;

public class RobotContainer {
  public static boolean inTeleop = true;
  private final static DriveTrain driveTrain = new DriveTrain();
  private final static ManualDrive manualDrive = new ManualDrive(driveTrain);

  private final static Ingester ingester = new Ingester();
  private final static IngesterPositioner ingestPos = new IngesterPositioner();
  private final static UnlockIngester unlockIngester = new UnlockIngester(ingestPos);

  private final static Shooter shooter = new Shooter();

  private final static Indexer indexer = new Indexer();

  private final static ShootBalls shootBalls = new ShootBalls(shooter, indexer);

  public static RemoteController climberStick = new RemoteController(new JoystickPlus(0));
  public static FlightJoystickController driverStick = new FlightJoystickController(new JoystickPlus(1));

  private final static Climber climber = new Climber();
  private final static Arm arm = new Arm();
  private final static ControlArm controlArm = new ControlArm(arm);
  private final static ManualClimb manualClimb = new ManualClimb(climber);
  // private final static AutoClimb autoClimb = new AutoClimb(climber);

  // declare new shooter airmer to be ran, for driveTrain
  private final static AdjustShooter adjustShooter = new AdjustShooter(driveTrain);
  private final static IndexBalls index = new IndexBalls(indexer, shooter);
  private final static IngestBalls ingest = new IngestBalls(ingester, indexer);
  private final static FlywheelShooter flywheelShooter = new FlywheelShooter(indexer, shooter, index);
  private final static AutonomousSetup autonomousCommand = new AutonomousSetup(driveTrain, climber, arm, ingestPos, shooter,
  indexer);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings

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
  private void configureButtonBindings() {
    /*
     * coDriverStick.btn_1.whileHeld(shootBall);
     * coDriverStick.btn_12.whenPressed(turretPresets);
     */
    // controller might not actually have any buttons, might be useless code
    /*
     * JoystickButton dL = new JoystickButton(driverStick.joystick, 1);
     * dL.whenPressed(servoMove);
     */
    // JoystickButton collectButton = new JoystickButton(driverStick.joystick, 1);
    // collectButton.whileHeld(autonomousCommand);

    // when press button "1" on frc will run shooterAimer, follow shooterAimer for
    // more info
    // Joystick joystick = new Joystick(0);
    JoystickButton shooterButton = new JoystickButton(driverStick.joystick, 1);
    shooterButton.whenHeld(adjustShooter);

    Trigger hookTrigger = new Trigger(() -> climberStick.getSlider() > .5);
    hookTrigger.whileActiveContinuous(manualClimb);
    hookTrigger.whenActive(() -> manualDrive.cancel());

    Trigger driveTrigger = new Trigger(() -> climberStick.getSlider() <= .5);
    driveTrigger.whileActiveContinuous(manualDrive);
    driveTrigger.whenActive(() -> manualClimb.cancel());

    JoystickButton flywheelButton = new JoystickButton(driverStick.joystick, 5);
    flywheelButton.whenPressed(flywheelShooter);

    JoystickButton ingestButton = new JoystickButton(driverStick.joystick, 7);
    ingestButton.whenPressed(ingest);
    ingestButton.whenReleased(index);

    // JoystickButton spitBallButton = new JoystickButton(driverStick.joystick, 6);
    // spitBallButton.whenHeld();
    // spitBallButton.whenReleased();

    /**
     * Get the slider position of the HID.
     *
     * @return the z position
     */

    // dR.whenPressed(servoMove);
    // JoystickButton manualButton = new JoystickButton(driverStick.joystick, 2);
    // manualButton.whenHeld(autoClimb);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public void autonomousInit() {
    inTeleop = false;
    autonomousCommand.schedule();
    // while (!timer.hasElapsed(2)) {}
    // flywheelShooter.schedule();
  }

  public void teleopInit() {
    configureButtonBindings();
    arm.setDefaultCommand(controlArm);
    // ingester.setDefaultCommand(ingest);
    ingestPos.setDefaultCommand(unlockIngester);
    // climber.setDefaultCommand(manualClimb);
    // driveTrain.setDefaultCommand(manualDrive);
    indexer.setDefaultCommand(index);
  }
}
