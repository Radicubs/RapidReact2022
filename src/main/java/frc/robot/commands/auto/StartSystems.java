package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.OnOffSystem;
import frc.robot.subsystems.StartableSystem;

public class StartSystems extends ParallelCommandGroup {

    public StartSystems(StartableSystem... systems) {
        for(StartableSystem system : systems) addCommands(new SystemOn(system));
    }
}
