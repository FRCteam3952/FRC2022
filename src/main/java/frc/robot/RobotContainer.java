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
import frc.robot.commands.ControlHooks;
import frc.robot.commands.SetShooterPower;
import frc.robot.commands.ShooterAimer;
import frc.robot.commands.UnlockIngester;
import frc.robot.commands.IngestBalls;
import frc.robot.commands.IndexBalls;
import frc.robot.commands.ShootBalls;
import frc.robot.commands.AdjustShooter;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.Ingester;
import frc.robot.subsystems.IngesterPositioner;
import frc.robot.subsystems.Tachometer;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import frc.robot.controllers.*;
import edu.wpi.first.wpilibj.Joystick;

public class RobotContainer {
  public static boolean inTeleop = true;
  public final static DriveTrain driveTrain = new DriveTrain();

  public final static Ingester ingester = new Ingester();
  public final static IngesterPositioner ingestPos = new IngesterPositioner();
  public final static UnlockIngester unlockIngester = new UnlockIngester(ingestPos);

  public final static Shooter shooter = new Shooter();

  public final static Tachometer tacheo = new Tachometer();
  public final static Indexer indexer = new Indexer();

  public final static ShootBalls shootBalls = new ShootBalls(shooter);

  public static Tango2Controller climberDriverController = new Tango2Controller(new Tango2Joystick(0));
  public static FlightJoystickController shooterStick = new FlightJoystickController(new Joystick(1));

  public final static ClimberHooks hooks = new ClimberHooks();
  public final static ClimberArm arm = new ClimberArm();
  public final static ControlArm controlArm = new ControlArm(arm);
  public final static ControlHooks controlHooks = new ControlHooks(hooks);

  // declare new shooter airmer to be ran, for driveTrain
  public final static AdjustShooter adjustShooter = new AdjustShooter(driveTrain, shooter);
  public final static IndexBalls index = new IndexBalls(indexer, shooter);
  public final static IngestBalls ingest = new IngestBalls(ingester, indexer);
  public final static FlywheelShooter flywheelShooter = new FlywheelShooter(tacheo, shooter, indexer, index);
  public final static AutonomousSetup autonomousCommand = new AutonomousSetup(driveTrain, hooks, arm, ingestPos, shooter, tacheo);
  //
  // public final static SequentialCommandGroup a = new
  // SequentialCommandGroup(flywheelShooter, autonomousCommand);

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
  public void configureButtonBindings() {
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
    JoystickButton shooterButton = new JoystickButton(shooterStick.joystick, 1);
    shooterButton.whileActiveContinuous(adjustShooter);
    shooterButton.whenReleased(adjustShooter.cancel());

    Trigger hookTrigger = new Trigger(() -> climberDriverController.getSlider() > .5);
    hookTrigger.whileActiveContinuous(controlHooks);
    hookTrigger.whenActive(() -> controlHooks.cancel());

    Trigger driveTrigger = new Trigger(() -> climberDriverController.getSlider() <= .5);
    driveTrigger.whileActiveContinuous(controlHooks);
    driveTrigger.whenActive(() -> controlHooks.cancel());

    JoystickButton flywheelButton = new JoystickButton(shooterStick.joystick, 5);
    flywheelButton.whenPressed(flywheelShooter);

    JoystickButton ingestButton = new JoystickButton(shooterStick.joystick, 7);
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

   /*
    inTeleop = false;
    Timer t = new Timer();
    t.start();
    indexer.setIndexSpeed(.3);
    ingester.setIngestRollerSpeed(.5);

    // autonomousCommand.schedule();
    // while (!timer.hasElapsed(2)) {}
    // shooter.setShooterSpeed(.36); // do change lol .3 old
    while (t.get() <3) {
      // ingestPos.changeIngestAngle(-0.5);
      driveTrain.drive(.6 * (t.get() / 2.5), 0);

    }
    driveTrain.drive(0, 0);
    indexer.setIndexSpeed(0);
    ingester.setIngestRollerSpeed(0);
    // ingestPos.changeIngestAngle(0);
*/
  public void autonomousInit() {
    inTeleop = false;
    Timer t = new Timer();
    t.start();
    indexer.setIndexSpeed(0);

    // autonomousCommand.schedule();
    // while (!timer.hasElapsed(2)) {}
    shooter.setShooterSpeed(.4); // do change lol .3 old
    while (t.get() < 3) {
    }
    indexer.setIndexSpeed(-.3);
    while (t.get() < 4) {
    }
    shooter.setShooterSpeed(0);
    indexer.setIndexSpeed(0);
    while (t.get() < 7.5) {
      if (t.get() < 6) {
        driveTrain.drive(.4 * (t.get() / 3.5), 0);

      } else {
        driveTrain.drive(0, 0);

      }
    }

  }

  public void teleopInit() {
    inTeleop = true;
    configureButtonBindings();
    arm.setDefaultCommand(controlArm);
    // ingester.setDefaultCommand(ingest);
    ingestPos.setDefaultCommand(unlockIngester);
    // hooks.setDefaultCommand(manualClimb);
    // driveTrain.setDefaultCommand(manualDrive);
    indexer.setDefaultCommand(index);
  }
}
