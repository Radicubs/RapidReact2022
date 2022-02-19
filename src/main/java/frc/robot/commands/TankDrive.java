package frc.robot.commands;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DifferentialDriveBase;
import frc.robot.subsystems.DriveBase;

public class TankDrive extends CommandBase {

    private final DifferentialDriveBase driveBase;

    public TankDrive(DifferentialDriveBase driveBase) {
        this.driveBase = driveBase;
        addRequirements(driveBase);
    }

    public void initialize() {}

    public void execute() {
        double left = RobotContainer.controller.getRawAxis(Constants.LEFT_Y_AXIS);
        double right = RobotContainer.controller.getRawAxis(Constants.RIGHT_Y_AXIS);

        left = 0.2;
        right = 0.2;

        if(Math.abs(left) < 0.01) left = 0;
        if(Math.abs(right) < 0.01) right = 0;

        driveBase.setValues(right, right, left, left);
    }


    public void end(boolean interrupted) {}


    public boolean isFinished() {
        return false;
    }



}