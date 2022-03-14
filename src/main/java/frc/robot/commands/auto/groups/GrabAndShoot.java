package frc.robot.commands.auto.groups;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.BallDrive;
import frc.robot.commands.Pickup;
import frc.robot.commands.auto.LimelightAlign;
import frc.robot.commands.auto.Shoot;
import frc.robot.subsystems.*;

public class GrabAndShoot extends SequentialCommandGroup {

    public GrabAndShoot(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter) {
        addCommands(new BallDrive(base, NetworkTableInstance.getDefault().getTable("data").getEntry("0")),
                new Pickup(base, intake, index, elevator, shooter),
                new LimelightAlign(base),
                new Shoot(elevator, shooter));
    }
}
