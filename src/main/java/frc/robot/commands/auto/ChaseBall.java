package frc.robot.commands.auto;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.BallDrive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.MecanumDriveBase;

public class ChaseBall extends SequentialCommandGroup {

    public ChaseBall(MecanumDriveBase base, Intake intake) {
        addRequirements(base, intake);

        addCommands(new BallDrive(base, NetworkTableInstance.getDefault().getTable("data").getEntry("0")),
                new SystemOn(intake),
                new DriveIndefintately(base));
    }
}
