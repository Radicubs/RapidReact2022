package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.DifferentialDriveBase;

public class TurnToAngle extends PIDCommand {

    private final DifferentialDriveBase drive;
    private final Translation2d target;

    public TurnToAngle(Translation2d target, Pose2d currentPose2d, DifferentialDriveBase drive) {
        super(
                new PIDController(Constants.kTurnP, Constants.kTurnI, Constants.kTurnD),
                // Close loop on heading
                drive::getHeading,
                // Set reference to targe
                //TurnToAngle::calcAngle,
                new CalcAngle(target, drive)::calcAngle,
                // Pipe output to turn robot
                output -> drive.arcadeDrive(0, output),
                // Require the drive
                drive);


        // Set the controller to be continuous (because it is an angle controller)

        addRequirements(drive);
        this.drive = drive;
        this.target = target;

        getController().enableContinuousInput(-180, 180);

        // Set the controller tolerance - the delta tolerance ensures the robot is stationary at the
        // setpoint before it is considered as having reached the reference
        getController().setTolerance(Constants.kTurnToleranceDeg, Constants.kTurnRateToleranceDegPerS);
    }

    public static double calcAngle(Translation2d target, DifferentialDriveBase base) {
        return Math.atan((base.getPose().getX() - target.getX()) / (base.getPose().getY() - target.getY())) * (180.0 / Math.PI) + base.getPose().getRotation().getDegrees();
    }

    @Override
    public boolean isFinished() {
        if (getController().atSetpoint()) {
            return true;
        }
        return false;
    }
}