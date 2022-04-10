package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.RobotContainer;


public class SetShooterPowerManual extends CommandBase {
    /**
     * Creates a new AutonomousCommand.
     */
    private final Shooter shoot;

    private double maxRPM = 5000;
    private double minRPM = 0;
    private double shooterRPM = minRPM;
    
    public SetShooterPowerManual(Shooter shoot) {
      // Use addRequirements() here to declare subsystem dependencies.
      this.shoot = shoot;
      addRequirements(shoot);
      
    }
    
    public void setShooterRPM() {
      double sliderValue = RobotContainer.secondaryJoystick.joystick.getRawAxis(2); //axis channel for slider
      shooterRPM = minRPM + sliderValue * (maxRPM - minRPM);
    }


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      setShooterRPM();
      shoot.setRPMValue(shooterRPM); //pass RPM value to shooter subsystem
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
