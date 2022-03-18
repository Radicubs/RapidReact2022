package frc.robot.commands.auto;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MecanumDriveBase;
import static edu.wpi.first.wpilibj.drive.MecanumDrive.driveCartesianIK;


public class LimelightAlign extends CommandBase {

    private final MecanumDriveBase drive;
    private final NetworkTable table;
    private boolean isDone;
    private boolean isAuto;
    private final int limelightThreshold = 2;

    public LimelightAlign(MecanumDriveBase drive) {
        this(drive, true);
    }

    public LimelightAlign(MecanumDriveBase drive, boolean isAuto) {
        this.drive = drive;
        addRequirements(drive);
        table = NetworkTableInstance.getDefault().getTable("limelight");
        isDone = false;
        this.isAuto = isAuto;
    }

    @Override
    public void execute() {
        /*
        double KpAim = -0.1f;
        double KpDistance = -0.1f;
        double min_aim_command = 0.05f;
        */

        // System.out.println(table.getEntry("tv").getDouble(0));
        double tv = table.getEntry("tv").getDouble(0);
        System.out.println(tv);
        if(tv < 0.5) {
            if(isAuto) {drive.setPercent(-0.2, -0.2, -0.2, -0.2);}
            else {drive.setPercent(0.12, -0.12, -0.12, 0.12);}
            return;
        }

        double tx = table.getEntry("tx").getDouble(0);
        double ty = table.getEntry("ty").getDouble(0);

        isDone = (Math.abs(tx) < limelightThreshold && Math.abs(ty) < limelightThreshold); // limelight threshold

        double zRot = -tx / 30;
        double forward = -ty / 13;
        double sideways = -tx / 100;

        MecanumDrive.WheelSpeeds speeds = driveCartesianIK(forward, sideways, zRot, 0);

        drive.setValues(speeds.rearRight, speeds.frontRight, speeds.rearLeft, speeds.frontLeft);

        /*
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
        */
    }

    @Override
    public boolean isFinished() {return isDone;}

    @Override
    public void end(boolean interrupted) {
        drive.setValues(0, 0, 0, 0);
    }

    public static double applyDeadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }


}
