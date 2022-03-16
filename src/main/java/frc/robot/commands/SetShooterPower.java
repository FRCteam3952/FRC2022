package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Tachometer;

import javax.lang.model.util.ElementScanner6;

import com.fasterxml.jackson.databind.type.PlaceholderForType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.IngesterPositioner;



public class SetShooterPower extends CommandBase {
    /**
     * Creates a new AutonomousCommand.
     */
    private final Shooter shoot;
    
    //private final

    public static double limelightAngleDeg = 35;
    public static double limelightHeightInch = 29;
    public static double goalHeightInch = 104;
    
    public double launchSpeed = 0;
    public double shooterRPM = 0;
    
    private final double HOOP_HEIGHT = 2.6416; //in meters
    private final double WHEEL_RADIUS = 0.006; //in meters
    private final double BALL_MASS = 0.907185; //in kilograms
    private final double WHEEL_MASS = 0.0175767; //in kilograms, find out later
    private final double GRAVITY = 9.80665; // in meters per second
    private final double ANGLE = 75; //degrees, measure later


//     private final double t = 10; //place hold for time will remove
//     private final double fallingSpeed = -(0.5)*GRAVITY*t*t; //probably won't use 
//     private final double RPM= 10; //place hold for speed of ball that will come from tacheo
//     private final double ballSpeed= 1/2*RPM*WHEEL_RADIUS* Math.PI; //about the speed the ball travels at
//     private final double xBallSpeed= Math.cos(ANGLE)*ballSpeed; //prob won't use
//     private final double yBallSpeed= Math.sin(ANGLE)*ballSpeed; //prob won't use
//     private final double maxHeight= ballSpeed*ballSpeed * Math.sin(ANGLE)*Math.sin(ANGLE)/(2*GRAVITY); //max height
//     private final double arrivingDistance= (ballSpeed*ballSpeed * Math.sin(2*ANGLE)/(GRAVITY))/2; //need to implement delta, a.k.a range of possible goals

    public SetShooterPower(Shooter shoot) {
      // Use addRequirements() here to declare subsystem dependencies.
      this.shoot = shoot;
      addRequirements(shoot);
      
    }

    public double distanceToHoop() {
      NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
      double targetOffsetAngleVert = table.getEntry("ty").getDouble(0.0);
      double angletoGoalDeg = limelightAngleDeg + targetOffsetAngleVert;
      double angletoGoalRad = angletoGoalDeg * (Math.PI / 180);
      return (goalHeightInch - limelightHeightInch)/Math.tan(angletoGoalRad) * 0.0254;
    }
  
//     public double calculateAngularVelocity() {
//       if ( (maxHeight == (2.64+(0.0095))) && ((arrivingDistance-distanceToHoop())== 0))  //implement delta possibilities to replace 0 and test some more
//       {
//           //return shootstuff
//       }
//       return 0;
//     }

    public void setLaunchSpeed() {
      double x = distanceToHoop();
      double y = HOOP_HEIGHT;
      double a = Math.toRadians(ANGLE);
      double g = GRAVITY;
      double velocity = Math.sqrt((g * Math.pow(x, 2)) / ((2 * Math.pow(Math.cos(a), 2)) * (y - x * Math.tan(a))));
      launchSpeed = velocity;
    }

    public void setShooterRPM() {
      double wheelTanSpeed = 2 * launchSpeed * ((WHEEL_MASS + (7/5) * BALL_MASS) / WHEEL_MASS); 
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
      //  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
      //  NetworkTableEntry tx = table.getEntry("tx");
      //  // NetworkTableEntry ty = table.getEntry("ty");
      //  // NetworkTableEntry ta = table.getEntry("ta");
      //  table.getEntry("ledMode").setNumber(3);
      //  table.getEntry("camMode").setNumber(0);
      //  table.getEntry("pipeline").setNumber(0);

      //  //read values periodically
      //  // double y = ty.getDouble(0.0);
      //  // double area = ta.getDouble(0.0);
      //  System.out.println("running");



      // /*
      //  frontLeftWheel.set(x/100.0);
      //  frontRightWheel.set(x/100.0);
      //  */

       
      // double delta = 1; //allowed deviation
      // if(distanceToHoop() + delta < distanceToShoot)
      //   drive.drive(0.3, 0);
      // else if (distanceToHoop() - delta > distanceToShoot) 
      //   drive.drive(-0.3, 0);
      // else
      //   drive.setShooterDistanceFinished();
       
      setLaunchSpeed();
      setShooterRPM();
      shoot.setAutoShootRPM(shooterRPM);
      cancel();
    }
    
  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      /*
      frontLeftWheel.stopMotor();
      frontRightWheel.stopMotor();
      */
    }
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  }
