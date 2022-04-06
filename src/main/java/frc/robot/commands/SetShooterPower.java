package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.DriveTrain;



public class SetShooterPower extends CommandBase {
    /**
     * Creates a new AutonomousCommand.
     */
    private final Shooter shoot;
    private final DriveTrain drive;
    
    //private final

    public static double limelightAngleDeg = 35;
    public static double limelightHeightInch = 29;
    public static double goalHeightInch = 104;
    
    public double launchSpeed = 0;
    public double shooterRPM = 0;
    
    private final double HOOP_HEIGHT = 2.6416; //in meters
    private final double HOOP_RADIUS = 0.6096; //in meters
    private final double WHEEL_RADIUS = 0.0619125; //in meters
    private final double BALL_MASS = 0.26932047; //in kilograms
    private final double WHEEL_MASS = 0.144582568; //in kilograms, find out later
    private final double GRAVITY = 9.80665; // in meters per second squared
    private final double ANGLE = 75; //degrees, measure later
    private final double MIN_DISTANCE = 2.6919; //in meters
    private final double delta = 3 - MIN_DISTANCE;



    public SetShooterPower(Shooter shoot, DriveTrain drive) {
      // Use addRequirements() here to declare subsystem dependencies.
      this.shoot = shoot;
      this.drive = drive;
      addRequirements(shoot, drive);
      
    }

    public double distanceToHoop() {
      NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
      double targetOffsetAngleVert = table.getEntry("ty").getDouble(0.0);
      double angletoGoalDeg = limelightAngleDeg + targetOffsetAngleVert;
      double angletoGoalRad = angletoGoalDeg * (Math.PI / 180);
      return (goalHeightInch - limelightHeightInch)/Math.tan(angletoGoalRad) * 0.0254;
    }

    public void setLaunchSpeed() {
      double x = distanceToHoop() + HOOP_RADIUS;
      double y = HOOP_HEIGHT;
      double a = Math.toRadians(ANGLE);
      double g = GRAVITY;
      double velocity = Math.sqrt((-(g/2) * Math.pow(x, 2)) / ((y - x * Math.tan(a)) * Math.pow(Math.cos(a), 2)));
      launchSpeed = velocity;
    }

    public void setShooterRPM() {
      double wheelTanSpeed = 2 * launchSpeed * ((WHEEL_MASS + ((7/5) * BALL_MASS)) / WHEEL_MASS); 
      double angularVelocity = wheelTanSpeed / WHEEL_RADIUS;
      shooterRPM = (angularVelocity * 60) / (2 * Math.PI);
    }


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      if (distanceToHoop() + HOOP_RADIUS < MIN_DISTANCE + delta) {
        System.out.println("Robot too close to hub to shoot, backing up");
        launchSpeed = 0;
        drive.drive(-0.5, 0, 0, 0);
      } else {
        setLaunchSpeed();
        setShooterRPM();
        shoot.setAutoShootRPM(shooterRPM);
        cancel();
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
