package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.TopIndexer;
import frc.robot.subsystems.Tachometer;
import frc.robot.RobotContainer;
import frc.robot.Constants;


public class BallHandling extends CommandBase {
    /**
     * Creates a new AutonomousCommand.
     */
    private final Shooter shoot;
    private final BottomIndexer bottomIndex;
    private final TopIndexer topIndex;
    private final Timer timer = new Timer();

    private double ingestSpeed = 0.4;
    private double indexSpeed = 0.15;
    private double shootIndexSpeed = 0.25;
    private double delta1 = 50; //allowed variance of RPM lower threshold (in case pid controll not 100% accurate)
    private double delta2 = 500; //allowed variance of RPM upper threshold (in case of tachometer rpm spikes)
    public double currentRPM;
    public double desiredRPM = 1000;
    public boolean previousLimitState;


    private ShootingStates state = ShootingStates.INDEX_FIRST_BALL;
    
    public BallHandling(Shooter shoot, BottomIndexer bottomIndex, TopIndexer topIndex) {
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
      ACCELERATE_FLYWHEEL_2,
      SHOOT_SECOND_BALL
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      System.out.println("tachometer: " + Tachometer.getShooterRPM());
      
      switch (state) {
        case INDEX_FIRST_BALL:
          bottomIndex.setIndexSpeed(ingestSpeed);
          topIndex.setIndexSpeed(indexSpeed);
          if (shoot.getTopShooterLim()) {
            topIndex.setIndexSpeed(0);
            state = ShootingStates.INDEX_SECOND_BALL;
          }
          break;

        case INDEX_SECOND_BALL:
          bottomIndex.setIndexSpeed(ingestSpeed);
          if (shoot.getBottomShooterLim()) {
            bottomIndex.setIndexSpeed(0);
            state = ShootingStates.PREPARE_TO_SHOOT;
          }
          break;

        case PREPARE_TO_SHOOT:
          System.out.println("Time to adjust aim and shooting power");
          if (RobotContainer.secondaryJoystick.joystick.getRawButton(Constants.shootBallsButtonNumber)) {
            System.out.println("AAAAA PREP TO SHOOT");
            state = ShootingStates.ACCELERATE_FLYWHEEL;
          }
          break;

        case ACCELERATE_FLYWHEEL:
          System.out.println("ACC FLYWHEEL");
          currentRPM = Tachometer.getShooterRPM();
          System.out.println(currentRPM);
          desiredRPM = shoot.getRPMValue();
          shoot.setShooterToRPM();
          // shoot.setShooterPower(1);
          if (currentRPM >= desiredRPM - delta1 && currentRPM <= desiredRPM + delta2) {
          // if(currentRPM >= desiredRPM) {
            
            state = ShootingStates.SHOOT_FIRST_BALL;
          }
          break;

        case SHOOT_FIRST_BALL:
          System.out.println("SHOOT BALL 1");
          previousLimitState = shoot.getTopShooterLim();
          shoot.setShooterToRPM();
          //shoot.setShooterPower(1);
          topIndex.setIndexSpeed(shootIndexSpeed);
          bottomIndex.setIndexSpeed(shootIndexSpeed);
          if (shoot.getTopShooterLim() && !previousLimitState) {
            state = ShootingStates.ACCELERATE_FLYWHEEL_2;
          }
          break;

        case ACCELERATE_FLYWHEEL_2:
          currentRPM = Tachometer.getShooterRPM();
          desiredRPM = shoot.getRPMValue();
          shoot.setShooterToRPM();            
          topIndex.setIndexSpeed(shootIndexSpeed);
          bottomIndex.setIndexSpeed(shootIndexSpeed);
          if (currentRPM >= desiredRPM - delta1 && currentRPM <= desiredRPM + delta2) {
          // if(currentRPM >= desiredRPM){
            timer.reset();
            state = ShootingStates.SHOOT_SECOND_BALL;
          }
          break;

        case SHOOT_SECOND_BALL:
          shoot.setShooterToRPM();
          topIndex.setIndexSpeed(shootIndexSpeed);
          bottomIndex.setIndexSpeed(shootIndexSpeed);
          if (timer.hasElapsed(3)) {
            shoot.setRPMValue(0);
            // shoot.setShooterPower(0);
            state = ShootingStates.INDEX_FIRST_BALL;
          }
          break;

        default:
          System.err.println("No state is true");
          break;
      }

    }
    
    public void resetSwitchState() {
      state = ShootingStates.INDEX_FIRST_BALL;
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
