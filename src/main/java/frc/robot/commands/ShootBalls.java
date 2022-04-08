package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.TopIndexer;
import frc.robot.Constants;

public class ShootBalls extends CommandBase {
    /**
     * Creates a new AutonomousCommand.
     */
    private final Shooter shoot;
    private final BottomIndexer bottomIndex;
    private final TopIndexer topIndex;

    private double bottomIndexSpeed = 0.4;
    private double topIndexSpeed = 0.35;

    private ShootingStates state = ShootingStates.INDEX_FIRST_BALL;
    
    public ShootBalls(Shooter shoot, BottomIndexer bottomIndex, TopIndexer topIndex) {
      // Use addRequirements() here to declare subsystem dependencies.
      this.shoot = shoot;
      this.bottomIndex = bottomIndex;
      this.topIndex = topIndex;
      addRequirements(shoot, bottomIndex, topIndex); 
    }

    private enum ShootingStates {
      INDEX_FIRST_BALL,
      INDEX_SECOND_BALL,
      PREPARE_TO_SHOOT,
      ACCELERATE_FLYWHEEL,
      SHOOT_FIRST_BALL,
      SHOOT_SECOND_BALL
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      switch (state) {
        case INDEX_FIRST_BALL:
          bottomIndex.setIndexSpeed(bottomIndexSpeed);
          topIndex.setIndexSpeed(topIndexSpeed);
          if (shoot.getTopShooterLimPressed()) {
            state = ShootingStates.INDEX_SECOND_BALL;
            topIndex.setIndexSpeed(0);
          }
          break;

        case INDEX_SECOND_BALL:
          bottomIndex.setIndexSpeed(bottomIndexSpeed);
          if (shoot.getBottomShooterLimPressed()) {
            state = ShootingStates.PREPARE_TO_SHOOT;
          }
          break;

        case PREPARE_TO_SHOOT:
          
          break;

        case ACCELERATE_FLYWHEEL:
          
          break;

        case SHOOT_FIRST_BALL:
          
          break;

        case SHOOT_SECOND_BALL:
          
          break;

        default:
          System.err.println("No state is true");
          break;
      }
    }
    
  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

    }
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  }
