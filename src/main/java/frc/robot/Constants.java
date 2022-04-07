// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    //PWM ports declaration  
    public static final int ingesterCollectingRollerBottomPort = 4;
    public static final int ingesterCollectingRollerTopPort= 8;
    public static final int indexerWheelsPort = 5;
    public static final int shooterRollersPort = 6;
    public static final int releaseIngesterPort = 7;


    //DIO port declaration
    public static final int bottomLimitSwitchClimberPort = 0;
    public static final int topOrBottomLimitClimberPort = 1;
    public static final int shooterShootingLimitPort = 2;
    public static final int shooterBottomLimitPort = 3;
    public static final int shooterTachometerPort = 4;

    
    // CAN Bus, uses phoenix 
    
    public static final int frontLeftMotorPort = 4;
    public static final int frontRightMotorPort = 1;
    public static final int rearLeftMotorPort = 3;
    public static final int rearRighttMotorPort = 2;
    public static final int armAnglePort = 6;
    public static final int hookPort = 10;
    public static final int flywheelPort1 = 7;
    public static final int flywheelPort2 = 8;


    //encoder channels, find values later
    public static final int frontLeftEncoderChannelA = 1;
    public static final int frontLeftEncoderChannelB = 2;
    public static final int frontRightEncoderChannelA = 3;
    public static final int frontRightEncoderChannelB = 4;
    public static final int rearLeftEncoderChannelA = 5;
    public static final int rearLeftEncoderChannelB = 6;
    public static final int rearRightEncoderChannelA = 7;
    public static final int rearRightEncoderChannelB = 8;

    //button numbers for primary joystick
    public static final int aimbotButtonNumber = 1;

    //button numbers for secondary joystick
    public static final int shootBallsButtonNumber = 1;
    public static final int adjustAimButtonNumber = 2;
    public static final int setShooterButtonNumber = 3;
}