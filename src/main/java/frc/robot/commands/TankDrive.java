package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBase;

public class TankDrive extends CommandBase {

    private final DriveBase driveBase;

    public TankDrive(DriveBase driveBase) {
        this.driveBase = driveBase;
        addRequirements(driveBase);
    }

    public void initialize() {}

    public void execute() {
        double left = RobotContainer.controller.getRawAxis(Constants.LEFT_Y_AXIS);
        double right = RobotContainer.controller.getRawAxis(Constants.RIGHT_Y_AXIS);

        if(Math.abs(left) < 0.01) left = 0;
        if(Math.abs(right) < 0.01) right = 0;

        driveBase.setValues(right, right, left, left);
    }


    public void end(boolean interrupted) {}


    public boolean isFinished() {
        return false;
    }



}