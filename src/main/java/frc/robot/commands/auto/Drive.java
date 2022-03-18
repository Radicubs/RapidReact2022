package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MecanumDriveBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class Drive extends CommandBase {//For testing purposes

    private final MecanumDriveBase base;
    private final double percent;

    public Drive(MecanumDriveBase base, double percent) {
        this.base = base;
        this.percent = percent;
    }

    @Override
    public void execute() {base.setPercent(percent, percent, -percent, -percent);}

    @Override
    public void end(boolean e) {
        base.setPercent(0, 0, 0, 0);
    }
}
