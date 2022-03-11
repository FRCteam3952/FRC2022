package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class FlywheelShooter extends CommandBase {
    private final Indexer index;
    private final Timer timer = new Timer();
    private final Shooter shooter;
    private final IndexBalls indexBalls;
    private final double INDEX_SPEED = .5;
    private final double SECOND_INDEX_MULTIPLIER = .75;
    private final double SHOOTER_SPEED = .5;
    private final double PULL_TIME = 0.125;
    private final double SHOOTER_RPM = 1500;
    private double curTime = 0;
    private boolean hasShot = false;

    public FlywheelShooter(Indexer indexer, Shooter shooter, IndexBalls indexBalls) {
        this.shooter = shooter;
        this.index = indexer;
        this.indexBalls = indexBalls;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(index, shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        timer.start();
        index.setIndexSpeed(0);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (timer.get() <= PULL_TIME) {
            index.setIndexSpeed(INDEX_SPEED);
        } else if (index.getShooterRPM() < SHOOTER_RPM && !hasShot) {
            index.setIndexSpeed(0);
            shooter.setShooterSpeed(SHOOTER_SPEED);
        } else if (index.getShooterRPM() > SHOOTER_RPM && !hasShot) {
            index.setIndexSpeed(-SECOND_INDEX_MULTIPLIER * INDEX_SPEED);
            hasShot = true;
            curTime = timer.get();
        } else if (timer.get() > (curTime + 1)) {
            index.setIndexSpeed(0);
            shooter.setShooterSpeed(0);
            hasShot = false;
            if (RobotContainer.inTeleop) {
                indexBalls.schedule();
            } else {
                // driver command here
            }
            timer.stop();
            cancel();
 
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        shooter.setShooterSpeed(0);
        timer.reset();
        hasShot = false;
        curTime = 0;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}