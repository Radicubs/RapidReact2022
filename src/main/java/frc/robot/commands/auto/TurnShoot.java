package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.MecanumDriveBase;
import frc.robot.subsystems.Shooter;

public class TurnShoot extends SequentialCommandGroup {

    public TurnShoot(MecanumDriveBase drive, Elevator elevator, Shooter shooter, Index index) {
        addCommands(new LimelightAlign(drive),
                new StartSystems(elevator, index, shooter).withTimeout(5));
    }
}
