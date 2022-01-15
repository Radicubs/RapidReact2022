package frc.robot.commands;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBase;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static edu.wpi.first.wpilibj.drive.MecanumDrive.driveCartesianIK;

public class MecanumDriveCommand extends CommandBase {

    private final DriveBase driveBase;

    public MecanumDriveCommand(DriveBase driveBase) {
        this.driveBase = driveBase;
        addRequirements(driveBase);
    }

    public void initialize() {}



    @Override
    public void execute() {

        double left = RobotContainer.controller.getRawAxis(Constants.LEFT_Y_AXIS) / 3;
        double right = RobotContainer.controller.getRawAxis(Constants.LEFT_X_AXIS) / 3;
        double zRot = RobotContainer.controller.getRawAxis(Constants.RIGHT_X_AXIS) / 3;

        MecanumDrive.WheelSpeeds speeds = driveCartesianIK(applyDeadband(left, 0.02), applyDeadband(right, 0.02), zRot, 0);

        driveBase.setValues(speeds.rearRight, speeds.frontRight, speeds.rearLeft, speeds.frontLeft);
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