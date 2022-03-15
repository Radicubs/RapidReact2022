package frc.robot.commands.auto.groups;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.BallDrive;
import frc.robot.commands.Pickup;
import frc.robot.commands.auto.Rotate;
import frc.robot.subsystems.*;

public class P1SA extends SequentialCommandGroup {

    public P1SA(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter) {
        addCommands(new GrabAndShoot(base, intake, index, elevator, shooter, true),
                // new Rotate(base, 120),
                new BallDrive(base, NetworkTableInstance.getDefault().getTable("data").getEntry("0")),
                new Pickup(base, intake, index, elevator, shooter, true));
    
                //new Rotate(base, 60),
                // new GrabAndShoot(base, intake, index, elevator, shooter, true));
    }
}
