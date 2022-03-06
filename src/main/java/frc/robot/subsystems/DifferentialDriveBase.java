package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.TankDrive;

public class DifferentialDriveBase extends DriveBase {
    /** Creates a new ExampleSubsystem. */

    SpeedControllerGroup leftMotors, rightMotors;
    
    private DifferentialDrive differentialDrive;

    private final DifferentialDriveOdometry differentialDriveOdometry;
    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.ksVolts, Constants.kvVoltSecondsPerMeter, Constants.kaVoltSecondsSquaredPerMeter);
    private double leftEncoderOffset;
    private double rightEncoderOffset;
    private PIDController left;
    private PIDController right;

    public DifferentialDriveBase() {
       super();

        differentialDriveOdometry = new DifferentialDriveOdometry(RobotContainer.gyro.getRotation2d());

        leftEncoderOffset = leftFront.getSelectedSensorPosition(0);
        rightEncoderOffset = rightFront.getSelectedSensorPosition(0);

        left = new PIDController(Constants.kPDriveVel, 0, 0);
        right = new PIDController(Constants.kPDriveVel, 0, 0);

        setDefaultCommand(new TankDrive(this));
    }

    public PIDController getLeftPID() {
        return left;
    }

    public PIDController getRightPID() {
        return right;
    }

    public void periodic() {
        differentialDriveOdometry.update(RobotContainer.gyro.getRotation2d(), getLeftEncoderDistance(), getRightEncoderDistance());
    }

    public SimpleMotorFeedforward getFeedforward() {
        return feedforward;
    }
    public double getLeftEncoderPosition() {
        return -(leftFront.getSelectedSensorPosition(0) - leftEncoderOffset);
    }
    public double getRightEncoderPosition() {
        return -(rightFront.getSelectedSensorPosition(0) - rightEncoderOffset);
    }

    public double getLeftEncoderDistance() {
        return getLeftEncoderPosition() * Constants.kEncoderDistancePerPulse;
    }

    public double getRightEncoderDistance() {
        return getRightEncoderPosition() * Constants.kEncoderDistancePerPulse;
    }

    public double getLeftEncoderRate() {
        return leftFront.getSelectedSensorVelocity(0) * 10 * 60 / (2048 * 6380.0);
    }

    public double getRightEncoderRate() {
        return rightFront.getSelectedSensorVelocity(0) * 10 * 60 / (2048 * 6380.0);
    }

    public Pose2d getPose() {
        return differentialDriveOdometry.getPoseMeters();
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(getLeftEncoderRate(), getRightEncoderRate());
    }

    public void resetEncoders() {
        // idk how to do this, DO NOT call resetOdometry()
        leftEncoderOffset = leftFront.getSelectedSensorPosition(0);
        rightEncoderOffset = rightFront.getSelectedSensorPosition(0);
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        differentialDriveOdometry.resetPosition(pose, RobotContainer.gyro.getRotation2d());
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftMotors.setVoltage(leftVolts / 12);
        rightMotors.setVoltage(rightVolts / 12);
        differentialDrive.feed();
    }

    public double getHeading() {
        return RobotContainer.gyro.getRotation2d().getDegrees();
    }

    public void setValues(double m1, double m2) {
        differentialDrive.tankDrive(m1, m2);
        differentialDrive.feed();
    }

    public void arcadeDrive(double y, double turn) {
        differentialDrive.arcadeDrive(y, turn);
    }
}