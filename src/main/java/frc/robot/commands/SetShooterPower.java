package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Tachometer;

import javax.lang.model.util.ElementScanner6;

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
    private final DriveTrain drive;
    
    //private final

    public static double shooterPower = 0.5; 
    public static double limelightAngleDeg = 35;
    public static double limelightHeightInch = 29;
    public static double goalHeightInch = 104;
    public static double tanSpeed = 0;
    

    private final double WHEEL_RADIUS = 0.006; //in meters
    private final double BALL_WEIGHT = 0.907185; //in kilograms
    private final double g = 9.80665; // in meters per second
    private final double angle = 75; //degrees, measure later
    
    

    public SetShooterPower(DriveTrain drive) {
      // Use addRequirements() here to declare subsystem dependencies.
      this.drive = drive;
      addRequirements(drive);
      
    }

    public double distanceToHoop() {
      NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
      double targetOffsetAngleVert = table.getEntry("ty").getDouble(0.0);
      double angletoGoalDeg = limelightAngleDeg + targetOffsetAngleVert;
      double angletoGoalRad = angletoGoalDeg * (Math.PI / 180);
      return (goalHeightInch - limelightHeightInch)/Math.tan(angletoGoalRad);
    }
  
    public double calculateAngularVelocity() {
      
      return 0;
    }

    public void setTangentialSpeed() {

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