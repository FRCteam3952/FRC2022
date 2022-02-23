// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.JoystickSim;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.CollectBalls;
import frc.robot.commands.ManualDrive;
import frc.robot.commands.ServoMove;
import frc.robot.commands.Climb;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Ingester;
import frc.robot.subsystems.Servos;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveTrain driveTrain = new DriveTrain();

  private final ManualDrive driveCommand = new ManualDrive(driveTrain);
  private final Servos servos = new Servos();

  private final Ingester ingester = new Ingester();

  private final CollectBalls collect = new CollectBalls(ingester);

  public static Controller driverStick = new Controller(new Joystick(0));

  private final AutonomousCommand autonomousCommand = new AutonomousCommand(driveTrain);

  private final ServoMove servoMove = new ServoMove(servos);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    servos.setDefaultCommand(servoMove);
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
    JoystickButton a = new JoystickButton(driverStick.joystick, 6);
    a.whenPressed(servoMove);
    JoystickButton collectButton = new JoystickButton(driverStick.joystick, 1);
    collectButton.whileHeld(autonomousCommand);

    JoystickButton d = new JoystickButton(driverStick.joystick, 6);
    d.whenPressed(servoMove);
    JoystickButton manualButton = new JoystickButton(driverStick.joystick, 2);
    collectButton.whileHeld(autonomousCommand);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return driveCommand;
  }
}
