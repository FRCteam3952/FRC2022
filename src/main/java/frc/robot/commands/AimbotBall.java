// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.subsystems.DriveTrain;


public class AimbotBall extends CommandBase {
    private final DriveTrain drive_train;
    private NetworkTableInstance inst;
    private NetworkTable table;
    private NetworkTableEntry ball;
    private NetworkTableEntry xPos;
    
    public AimbotBall(DriveTrain subDriveTrain) {
        drive_train = subDriveTrain;
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("Vision");
        ball = table.getEntry("seeBall");
        xPos = table.getEntry("ball_x");

        addRequirements(drive_train);
    }

    @Override
    public void initialize() {
        System.out.println("doing aimbot ball thing");

    }

    @Override
    public void execute() {

        if(ball.getBoolean(false)) {
            double x = xPos.getNumber(100.0).doubleValue();
            double error = 200 - x;
            System.out.println(" Error: " + error);
            drive_train.drive(0, PIDCalculations(error), 0);
            
        } else {
            System.out.println("no ball?");
        }
        cancel();
    }

    public double PIDCalculations(double error){
        //PIDController pid = new PIDController(1, 0, 0);
        //double calculation = pid.calculate(error);
        //System.out.println(calculation);
        return error/160/4;
    }
}