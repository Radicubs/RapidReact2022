package frc.robot.commands.auto.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auto.Drive;
import frc.robot.subsystems.*;

public class P3SA extends SequentialCommandGroup {

    public P3SA(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter) {
        addCommands(new GrabAndShoot(base, intake, index, elevator, shooter, true, 0),
                new Drive(base, 0.25).withTimeout(1),
                new GrabAndShoot(base, intake, index, elevator, shooter, false, -0.15, false));
    }

}
