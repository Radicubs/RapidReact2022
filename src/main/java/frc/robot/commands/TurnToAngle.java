package frc.robot.commands;

import javax.swing.TransferHandler.TransferSupport;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.DifferentialDriveBase;

public class TurnToAngle extends PIDCommand {
    public TurnToAngle(double targetAngleDegrees, DifferentialDriveBase drive) {
        super(
            new PIDController(Constants.kTurnP, Constants.kTurnI, Constants.kTurnD),
            // Close loop on heading
            drive::getHeading,
            // Set reference to target
            targetAngleDegrees,
            // Pipe output to turn robot
            output -> drive.arcadeDrive(0, output),
            // Require the drive
            drive);
    
    
        // Set the controller to be continuous (because it is an angle controller)
    
        getController().enableContinuousInput(-180, 180);
    
        // Set the controller tolerance - the delta tolerance ensures the robot is stationary at the
        // setpoint before it is considered as having reached the reference
        getController().setTolerance(Constants.kTurnToleranceDeg, Constants.kTurnRateToleranceDegPerS);
      }

    public TurnToAngle(Translation2d target, Pose2d currentPose2d, DifferentialDriveBase drive) {
        this(Math.atan2(currentPose2d.getY() - target.getY(), currentPose2d.getX() - target.getX()) * (180.0 / Math.PI) + currentPose2d.getRotation().getDegrees(), drive);
        System.out.println(Math.atan2(currentPose2d.getY() - target.getY(), currentPose2d.getX() - target.getX()) * (180.0 / Math.PI) + currentPose2d.getRotation().getDegrees());
        System.out.println(target);
        System.out.println(currentPose2d);
    }
    
    
      @Override
    
      public boolean isFinished() {
        // End when the controller is at the reference.
    
        // return getController().atSetpoint();
        return false;
      }
    
}
