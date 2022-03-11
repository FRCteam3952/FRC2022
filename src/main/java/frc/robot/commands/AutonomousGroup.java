package frc.robot.commands;

import java.util.Arrays;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutonomousGroup extends SequentialCommandGroup {
    public AutonomousGroup(Command a, Command b) {
        addCommands(a, b);
    }

}
 