package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DifferentialDriveBase;

public class Turn2Angle extends CommandBase {

    private final DifferentialDriveBase drive;
    private PIDController controller;
    private Translation2d target;
    public boolean firstRun = false;
    public double targetAngle;
    public Turn2Angle(Translation2d target, DifferentialDriveBase drive) {
        this.drive = drive;
        addRequirements(drive);

        this.target = target;

        controller = new PIDController(Constants.kTurnP, Constants.kTurnI, Constants.kTurnD);

        controller.enableContinuousInput(-180, 180);
        controller.setTolerance(Constants.kTurnToleranceDeg, Constants.kTurnRateToleranceDegPerS);
    }
    
    @Override
    public void initialize() {
        controller.reset();
        // targetAngle = Math.atan((drive.getPose().getX() - target.getX()) / (drive.getPose().getY() - target.getY())) * (180.0 / Math.PI) + drive.getPose().getRotation().getDegrees();
        // System.out.println("Calculated target angle to " + targetAngle);
    }

    @Override
    public void execute() {
        if (firstRun == false) {
            targetAngle = Math.atan((drive.getPose().getX() - target.getX()) / (drive.getPose().getY() - target.getY())) * (180.0 / Math.PI) + drive.getPose().getRotation().getDegrees();
        }
        firstRun = true;
        System.out.println(drive.getPose());
        drive.arcadeDrive(0, controller.calculate(drive.getHeading(), targetAngle));
    }

    @Override
    public boolean isFinished() {
        if (controller.atSetpoint()) {
            targetAngle = 0;
            firstRun = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        firstRun = false;
    }
}
