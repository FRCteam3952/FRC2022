package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.DriveTrain;



public class ShootBallsManual extends CommandBase {
    /**
     * Creates a new AutonomousCommand.
     */
    private final Shooter shoot;
    
    //private final
    
    private final double speed = 1;



    public ShootBallsManual(Shooter shoot) {
      // Use addRequirements() here to declare subsystem dependencies.
      this.shoot = shoot;
      addRequirements(shoot);
      
    }


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      shoot.setShooterPower(speed);
      // System.out.println("set speed");
    }
    
  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        shoot.setShooterPower(0);
    }
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  }
