package frc.robot.commands.auto.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auto.Drive;
import frc.robot.subsystems.*;

public class DualBall extends SequentialCommandGroup {

    public DualBall(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter, double rotDir) {
        addCommands(new GrabAndShoot(base, intake, index, elevator, shooter, true, rotDir),
                new Drive(base, 0.2).withTimeout(1),
                new GrabAndShoot(base, intake, index, elevator, shooter, false, rotDir));
    }
}
