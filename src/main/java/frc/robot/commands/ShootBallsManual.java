package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.TopIndexer;

import edu.wpi.first.wpilibj.Timer;



public class ShootBallsManual extends CommandBase {
    /**
     * Creates a new AutonomousCommand.
     */
    private final Shooter shoot;
    private final Timer timer;
    private final BottomIndexer bottomIndex;
    private final TopIndexer topIndex;
    
    
    //private final
    
    private final double speed = 0.85;
    private final double indexSpeed = 0.4;


    public ShootBallsManual(Shooter shoot, BottomIndexer bottomIndex, TopIndexer topIndex) {
      // Use addRequirements() here to declare subsystem dependencies.
      this.timer = new Timer();
      this.shoot = shoot;
      this.bottomIndex = bottomIndex;
      this.topIndex = topIndex;
      addRequirements(shoot, bottomIndex, topIndex); 
    }


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      timer.start();
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      //shoot.setShooterPower(speed);
      if(timer.hasElapsed(2)){
        bottomIndex.setIndexSpeed(indexSpeed);
        topIndex.setIndexSpeed(indexSpeed);
      }
      if(timer.hasElapsed(4)){
        bottomIndex.setIndexSpeed(0);
        topIndex.setIndexSpeed(0.15);
        //shoot.setShooterPower(0.2);
      }
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
