package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.StartableSystem;

public class SystemOn extends CommandBase {

    private final StartableSystem system;

    public SystemOn(StartableSystem system) {
        this.system = system;
    }

    @Override
    public void initialize() {system.on();}
}
