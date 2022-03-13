package frc.robot.commands.auto;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MecanumDriveBase;

public class LimelightAlign extends CommandBase {

    private final MecanumDriveBase drive;
    private final NetworkTable table;
    private boolean isDone;

    public LimelightAlign(MecanumDriveBase drive) {
        this.drive = drive;
        addRequirements(drive);
        table = NetworkTableInstance.getDefault().getTable("limelight");
        isDone = false;
    }

    @Override
    public void execute() {
        double KpAim = -0.1f;
        double KpDistance = -0.1f;
        double min_aim_command = 0.05f;
        double tx = table.getEntry("tx").getDouble(0);
        double ty = table.getEntry("ty").getDouble(0);

        double heading_error = -tx;
        double distance_error = -ty;
        double steering_adjust = 0.0f;

        if (tx > 1.0)
        {
            steering_adjust = KpAim*heading_error - min_aim_command;
        }
        else if (tx < -1.0)
        {
            steering_adjust = KpAim*heading_error + min_aim_command;
        }

        double distance_adjust = KpDistance * distance_error;

        drive.setValues(-(steering_adjust + distance_adjust), -(steering_adjust + distance_adjust), steering_adjust + distance_adjust, steering_adjust + distance_adjust);

        isDone = Math.abs(tx) < 8 && Math.abs(ty) < 8;
    }

    @Override
    public boolean isFinished() {return isDone;}

    @Override
    public void end(boolean interrupted) {
        drive.setValues(0, 0, 0, 0);
    }



}
