package frc.robot.commands;

import frc.robot.RobotContainer;
import frc.robot.subsystems.BottomIndexer;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberHooks;
import frc.robot.subsystems.DriveTrain;
// import frc.robot.subsystems.Tachometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Tachometer;
import frc.robot.subsystems.TopIndexer;

public class Autonomous extends CommandBase {
  /**
   * Creates a new AutonomousCommand.
   */
  private final DriveTrain drive;
  private final ClimberHooks climber;
  private final ClimberArm arm;
  private final Shooter shooter;
  private final BottomIndexer bottomIndexer;
  private final TopIndexer topIndexer;
  // private final Tachometer tacheo;
  private final Timer timer = new Timer();

  private final double SHOOTER_SPEED = 0.2;

  private double desiredRPM = 1000;
  private double indexSpeed = 0.15;
  private double shootIndexSpeed = 0.25;

  private double xSpeed= 0;
  private double ySpeed= 0; 
  private double zRotation=0;
  private final double MAX_POSITION = 50; //measured in motor rotations, measure later
  private AutonStages stage = AutonStages.CLIMBER_ARM_30_AND_INGEST;


  public Autonomous(DriveTrain drive, ClimberHooks climber, ClimberArm arm, Shooter shooter, BottomIndexer bottomIndexer, TopIndexer topIndexer) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.drive = drive;
    this.climber = climber;
    this.arm = arm;
    this.shooter = shooter;
    this.bottomIndexer = bottomIndexer;
    this.topIndexer = topIndexer;
    // this.tacheo = tacheo;
    addRequirements(drive, climber, arm, shooter, bottomIndexer, topIndexer);
  }

  private enum AutonStages {
      CLIMBER_ARM_30_AND_INGEST,
      MOVE_TO_POS,
      ACCELERATE_FLYWHEEL,
      SHOOT_FIRST_BALL,
      ACCELERATE_FLYWHEEL_2,
      SHOOT_SECOND_BALL,
      FINISH
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ySpeed = 0.5;
    xSpeed = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      if(!RobotContainer.inTeleop) {
          cancel();
      } else {
      switch(stage) {
          case CLIMBER_ARM_30_AND_INGEST:
            bottomIndexer.releaseServo();
            if(!arm.angleLimitPressed()) {
                arm.changeArmAngle(-0.5);
            } else {
                arm.changeArmAngle(0);
                stage = AutonStages.FINISH;
            }
            break;
          case MOVE_TO_POS:
            if (drive.getPosition() <= MAX_POSITION) {
                xSpeed += drive.getAdjustment()[0];
                ySpeed += drive.getAdjustment()[1];
                drive.drive(ySpeed, xSpeed, 0);
            } else {
                stage = AutonStages.SHOOT_FIRST_BALL;
            }
            break;
        case ACCELERATE_FLYWHEEL:
            System.out.println("ACC FLYWHEEL");
            double currentRPM = Tachometer.getShooterRPM();
            System.out.println(currentRPM);
            desiredRPM = shooter.getRPMValue();
            shooter.setShooterToRPM();
            // shoot.setShooterPower(1);
            if (currentRPM >= desiredRPM) {
            // if(currentRPM >= desiredRPM) {
              
              stage = AutonStages.SHOOT_FIRST_BALL;
            }
            break;
  
          case SHOOT_FIRST_BALL:
            System.out.println("SHOOT BALL 1");
            shooter.setShooterToRPM();
            //shooter.setShooterPower(1);
            topIndexer.setIndexSpeed(shootIndexSpeed);
            bottomIndexer.setIndexSpeed(shootIndexSpeed);
            if (shooter.getTopShooterLim()) {
              stage = AutonStages.ACCELERATE_FLYWHEEL_2;
            }
            break;
  
          case ACCELERATE_FLYWHEEL_2:
            currentRPM = Tachometer.getShooterRPM();
            desiredRPM = shooter.getRPMValue();
            shooter.setShooterToRPM();            
            topIndexer.setIndexSpeed(shootIndexSpeed);
            bottomIndexer.setIndexSpeed(shootIndexSpeed);
            if (currentRPM >= desiredRPM) {
            // if(currentRPM >= desiredRPM){
              timer.reset();
              stage = AutonStages.SHOOT_SECOND_BALL;
            }
            break;
  
          case SHOOT_SECOND_BALL:
            shooter.setShooterToRPM();
            topIndexer.setIndexSpeed(shootIndexSpeed);
            bottomIndexer.setIndexSpeed(shootIndexSpeed);
            if (timer.hasElapsed(3)) {
              shooter.setRPMValue(0);
              // shoot.setShooterPower(0);
              stage = AutonStages.FINISH;
            }
            break;
          case FINISH:
            cancel();
          default:
            break;

      }
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