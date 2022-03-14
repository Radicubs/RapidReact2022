package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MecanumDriveBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class Drive extends CommandBase {//For testing purposes

    private final MecanumDriveBase base;
    private final AtomicBoolean shouldEnd;
    private final double percent;

    public Drive(MecanumDriveBase base, AtomicBoolean shouldEnd, double percent) {
        this.shouldEnd = shouldEnd;
        this.base = base;
        this.percent = percent;
    }

    @Override
    public void execute() {base.setPercent(percent, percent, -percent, -percent);}

    @Override
    public boolean isFinished() {return shouldEnd.get();}

    @Override
    public void end(boolean e) {
        base.setPercent(0, 0, 0, 0);
    }
}
