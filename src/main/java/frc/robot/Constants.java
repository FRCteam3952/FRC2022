// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    // team COLOR
    public static final int findTeamSwitch = 0; // we need to set this up

    // PWM ports declaration
    public static final int ingesterServoPort = 0;

    // DIO ports declaration
    public static final int bottomLimitSwitchClimberPort = 0;
    public static final int shooterBottomLimitPort = 9;
    public static final int shooterTopLimitPort = 8;
    public static final int shooterTachometerPort = 3;
    public static final int angleLimitSwitchClimberPort = 4;

    // CAN Bus ID declaration
    public static final int frontRightMotorPort = 1;
    public static final int rearRighttMotorPort = 2;
    public static final int rearLeftMotorPort = 3;
    public static final int frontLeftMotorPort = 4;
    public static final int bottomIndexerPort = 5;
    public static final int armAnglePort = 6;
    public static final int flywheelPort1 = 7;
    public static final int flywheelPort2 = 8;
    public static final int topIndexerPort = 9;
    public static final int hookPort = 10;

    // Joystick Port Numbers
    public static final int primaryJoystickPort = 0;
    public static final int secondaryJoystickPort = 1;
    public static final int tertiaryJoystickPort = 2;

    // button numbers for primary joystick (1-9)
    public static final int rollIngesterButtonNumber = 1;
    public static final int aimbotToBallButtonNumber = 2;
    public static final int spitBothBallButtonNumber = 3;
    public static final int spitBottomBallButtonNumber = 4;
    public static final int resetGyroButtonNumber = 8;

    // button numbers for secondary joystick (1-11)
    public static final int shootBallsButtonNumber = 1;
    public static final int adjustAimButtonNumber = 2;
    public static final int setShooterPowerButtonNumber = 3;
    public static final int setShooterManualButtonNumber = 4;
    public static final int resetIndexerAndIngesterButtonNumber = 5;
    public static final int setToTarmacRPMButtonNumber = 7;

    // button numbers for tertiary joystick (1-11)
    public static final int autoClimbButtonNumber = 1;
    public static final int moveArmAngleToRobotButtonNumber = 2;
    public static final int moveArmAngleAwayFromRobotButtonNumber = 3;
    public static final int adjustShooterPowerManualButtonNumber = 4;
    public static final int resetAutoClimbButtonNumber = 7;
    public static final int startingConfigButtonNumber = 8;
    public static final int resetArmEncoderButtonNumber = 9;
    public static final int setArmAngleto90ButtonNumber = 10;
    public static final int resetClimberEncoderButtonNumber = 11;

}
