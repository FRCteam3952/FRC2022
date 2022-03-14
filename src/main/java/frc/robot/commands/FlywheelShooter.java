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
    private final double INDEX_SPEED = .25;
    private final double SECOND_INDEX_MULTIPLIER = 1.5;
    private final double PULL_TIME = 0.3;
    private final double HIGH_RPM = 9000;
    private final double LOW_RPM = 2000;
    private final double HIGH_POWER = .75;
    private final double LOW_POWER = 0.5;
    private double ShooterRPM = LOW_RPM;
    private double ShooterPower = LOW_POWER;
    private double delta = 1000;
    private double currentTime = 0;
    private boolean done = false;
    private boolean hasShot = false;

    public FlywheelShooter(Indexer indexer, Shooter shooter, IndexBalls indexBalls) {
        this.shooter = shooter;
        this.index = indexer;
        this.indexBalls = indexBalls;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(index, shooter);
    }

    public void calculateSpeed() {
        double slider = 1 - (RobotContainer.driverStick.joystick.getXRotate() + 1) / 2;
        System.out.println(slider);
        ShooterRPM = LOW_RPM + (slider * (HIGH_RPM - LOW_RPM));
        ShooterPower = LOW_POWER + (slider * (HIGH_POWER - LOW_POWER));
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
        System.out.println("sadsad");
        calculateSpeed();
        if (!timer.hasElapsed(1)) {
            index.setIndexSpeed(0);
            shooter.setShooterSpeed(-0.75);
        }
        else if (!timer.hasElapsed(1 + PULL_TIME)) {
            index.setIndexSpeed(INDEX_SPEED); //pull in balls
        } else if (index.getShooterRPM() < ShooterRPM && !hasShot) {
            index.setIndexSpeed(0);
            shooter.setShooterSpeed(ShooterPower); //rev up shooter
        } else if (index.getShooterRPM() > ShooterRPM && index.getShooterRPM() < ShooterRPM + delta && !hasShot) {
            index.setIndexSpeed(-SECOND_INDEX_MULTIPLIER * INDEX_SPEED); 
            hasShot = true; //index balls into shooter
            currentTime = timer.get();
        } else if (timer.get() > (currentTime + 2.69)) {
            index.setIndexSpeed(0);
            shooter.setShooterSpeed(0);
            timer.stop();
            if (RobotContainer.inTeleop) {
                indexBalls.schedule();
            }
            cancel();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        shooter.setShooterSpeed(0);
        timer.reset();
        hasShot = false;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return done;
    }
}