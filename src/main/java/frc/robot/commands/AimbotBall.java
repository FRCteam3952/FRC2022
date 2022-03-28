// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


public class AimbotBall extends CommandBase {
    private DriveTrain drive_train;
    private NetworkTableInstance inst;
    private NetworkTable table;
    private NetworkTableEntry ball;
    private NetworkTableEntry xPos;
    private NetworkTableEntry xRes;
    
    public AimbotBall() {
        System.out.println("Set up Network Table.");
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("Vision");
        ball = table.getEntry("seeBall");
        xPos = table.getEntry("ball_x");
        xRes = table.getEntry("xRes");

    
    }

    @Override
    public void initialize() {
        System.out.println("doing aimbot ball thing");

    }

    @Override
    public void execute() {

        if(ball.getBoolean(false)) {
            double x = xPos.getNumber(100.0).doubleValue();
            System.out.println(x);
            double error = xRes.getNumber(100.0).doubleValue()/2 - x;
            
        } else {
            System.out.println("no ball");
        }
    }
}