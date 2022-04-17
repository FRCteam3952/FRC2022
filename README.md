# Troy frc 2021-22 season robot code

## Here's the important future info that we're gonna remove from the default code comments. Make sure to check this

### Subsystems:
periodic() -- Is run once per scheduler run. Basically execute() but for subsystems

simulationPeriodic() -- periodic() but during simulation

### Commands:
Within the constructor: use addRequirements(subsystem1, subsystem2) to add subsystem requirements that you pass to constructor. You can put as many as you want into addRequirements(), 2 is just for the example. You MUST do this if you use a subsystem

initialize() -- Is run when the command is scheduled.

execute() -- Is run once per scheduler run. Do NOT use a while loop in here, instead use an if statement.

end() -- Is run when the command ends OR is interrupted.

isFinished() -- Make this return true when the command should end.

## Other code we use
[Ball finding](https://github.com/SeanSon2005/TroyFRC2022NeuralNet)
