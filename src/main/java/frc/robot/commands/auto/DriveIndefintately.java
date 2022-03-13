package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MecanumDriveBase;

public class DriveIndefintately extends CommandBase {//For testing purposes

    private final MecanumDriveBase base;

    public DriveIndefintately(MecanumDriveBase base) {
        this.base = base;
    }

    @Override
    public void initialize() {base.setPercent(0.1, 0.1, 0.1, 0.1);}

    @Override
    public boolean isFinished() {return false;}
}
