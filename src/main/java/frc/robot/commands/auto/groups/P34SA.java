package frc.robot.commands.auto.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.*;

public class P34SA extends SequentialCommandGroup {

    public P34SA(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter) {
        addCommands(new DualBall(base, intake, index, elevator, shooter, -0.333),
                new GrabAndShoot(base, intake, index, elevator, shooter, false, -0.333));
    }

}
