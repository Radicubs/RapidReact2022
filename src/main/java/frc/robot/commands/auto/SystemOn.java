package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.OnOffSystem;

public class SystemOn extends CommandBase {

    private final OnOffSystem system;

    public SystemOn(OnOffSystem system) {
        this.system = system;
    }

    @Override
    public void initialize() {system.on();}
}
