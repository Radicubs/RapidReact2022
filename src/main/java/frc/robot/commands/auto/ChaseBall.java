package frc.robot.commands.auto;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.BallDrive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.MecanumDriveBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChaseBall extends SequentialCommandGroup {

    public ChaseBall(MecanumDriveBase base, Intake intake) {
        addRequirements(base, intake);

        addCommands(new BallDrive(base, NetworkTableInstance.getDefault().getTable("data").getEntry("0")),
                new SystemOn(intake),
                new Drive(base, new AtomicBoolean(false), 0.1));
    }
}
