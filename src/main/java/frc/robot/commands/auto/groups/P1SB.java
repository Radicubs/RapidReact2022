package frc.robot.commands.auto.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auto.Drive;
import frc.robot.commands.auto.Rotate;
import frc.robot.subsystems.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class P1SB extends SequentialCommandGroup {

    public P1SB(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter) {
        addCommands(new GrabAndShoot(base, intake, index, elevator, shooter),
                new Rotate(base, 90),
                new Drive(base, new AtomicBoolean(false), 0.2).withTimeout(1),
                new GrabAndShoot(base, intake, index, elevator, shooter));
    }
}
