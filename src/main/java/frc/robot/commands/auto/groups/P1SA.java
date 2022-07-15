package frc.robot.commands.auto.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Pickup;
import frc.robot.subsystems.*;

public class P1SA extends SequentialCommandGroup {

    public P1SA(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter) {
        addCommands(new GrabAndShoot(base, intake, index, elevator, shooter, true, 0),
                new Pickup(base, intake, index, elevator, shooter, -0.1),
                new GrabAndShoot(base, intake, index, elevator, shooter, true, 0.15));
    }
}
