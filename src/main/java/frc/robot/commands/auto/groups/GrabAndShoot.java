package frc.robot.commands.auto.groups;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.BallDown;
import frc.robot.commands.BallDrive;
import frc.robot.commands.Pickup;
import frc.robot.commands.auto.LimelightAlign;
import frc.robot.commands.auto.Shoot;
import frc.robot.subsystems.*;

public class GrabAndShoot extends SequentialCommandGroup {
    public GrabAndShoot(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter, boolean ballPreloaded, double rotDir) {
        if(!ballPreloaded) addCommands(new BallDrive(base, NetworkTableInstance.getDefault().getTable("data").getEntry("0"), rotDir),
                new Pickup(base, intake, index, elevator, shooter, ballPreloaded));

        addCommands(new LimelightAlign(base, true).
                        alongWith(new BallDown(elevator, shooter, index, intake).withTimeout(0.25)),
                new Shoot(index, elevator, shooter).withTimeout(2));
    }
}
