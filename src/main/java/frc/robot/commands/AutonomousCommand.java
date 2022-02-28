package frc.robot.commands;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.commands.ShootBalls;


public class AutonomousCommand extends CommandBase {
    /**
     * Creates a new AutonomousCommand.
     */
    private final DriveTrain drive;
    private final Talon frontLeftWheel;
    private final Talon frontRightWheel;

    private final Shooter shooter = new Shooter();
    private final ShootBalls shootBalls = new ShootBalls(shooter);

    public static double distanceToShoot; // DEFINE LATER WHEN YOU KNOW HOW FAR
    public static double limelightAngleDeg;
    public static double limelightHeightInch = 30;
    public static double goalHeightInch;

    public AutonomousCommand(DriveTrain subsystem) {
      // Use addRequirements() here to declare subsystem dependencies.
      drive = subsystem;     
      frontLeftWheel = new Talon(0); 
      frontRightWheel = new Talon(1);
      addRequirements(drive);
      
    }
  
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
     
       NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
       NetworkTableEntry tx = table.getEntry("tx");
       NetworkTableEntry ty = table.getEntry("ty");
       NetworkTableEntry ta = table.getEntry("ta");
       table.getEntry("ledMode").setNumber(3);
       table.getEntry("camMode").setNumber(0);
       table.getEntry("pipeline").setNumber(0);


       //read values periodically
       double x = tx.getDouble(0.0);
       double y = ty.getDouble(0.0);
       double area = ta.getDouble(0.0);
       System.out.println("running");
       frontLeftWheel.set(x/100.0);
       frontRightWheel.set(x/100.0);

       System.out.println("a");

       if(isFarEnoughToShoot() ==  distanceToShoot) {
          shootBalls.schedule();
       }
    }
    public double isFarEnoughToShoot() {
      NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
      double targetOffsetAngleVert = table.getEntry("ty").getDouble(0.0);
      double angletoGoalDeg = limelightAngleDeg + targetOffsetAngleVert;
      double angletoGoalRad = angletoGoalDeg * (3.14159 / 180);
      return (goalHeightInch - limelightHeightInch)/Math.tan(angletoGoalRad);
    }
  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      frontLeftWheel.stopMotor();
      frontRightWheel.stopMotor();
    }
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  }