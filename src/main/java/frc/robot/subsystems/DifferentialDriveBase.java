package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.MecanumDriveCommand;
import frc.robot.commands.TankDrive;

import java.util.Arrays;
import java.util.List;

public class DifferentialDriveBase extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */

    private WPI_TalonFX rightFront;
    private WPI_TalonFX rightBack;
    private WPI_TalonFX leftFront;
    private WPI_TalonFX leftBack;
    SpeedControllerGroup leftMotors, rightMotors;
    
    private DifferentialDrive differentialDrive;

    private final DifferentialDriveOdometry differentialDriveOdometry;
    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.ksVolts, Constants.kvVoltSecondsPerMeter, Constants.kaVoltSecondsSquaredPerMeter);
    private double leftEncoderOffset;
    private double rightEncoderOffset;
    private PIDController left;
    private PIDController right;

    public DifferentialDriveBase() {
        rightFront = new WPI_TalonFX(Constants.RIGHT_FALCON_FRONT);
        rightBack = new WPI_TalonFX(Constants.RIGHT_FALCON_BACK);
        leftFront = new WPI_TalonFX(Constants.LEFT_FALCON_FRONT);
        leftBack = new WPI_TalonFX(Constants.LEFT_FALCON_BACK);

        List<WPI_TalonFX> motors = Arrays.asList(rightBack, rightFront, leftBack, leftFront);

        for(WPI_TalonFX motor : motors) {
            motor.configFactoryDefault();

            motor.configNeutralDeadband(0.001);

            /* Config sensor used for Primary PID [Velocity] */
            motor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, Constants.kPIDLoopIdx,
                    Constants.kTimeoutMs);

            /* Config the peak and nominal outputs */
            motor.configNominalOutputForward(0, Constants.kTimeoutMs);
            motor.configNominalOutputReverse(0, Constants.kTimeoutMs);
            motor.configPeakOutputForward(1, Constants.kTimeoutMs);
            motor.configPeakOutputReverse(-1, Constants.kTimeoutMs);

            motor.setNeutralMode(NeutralMode.Brake);
        }

        leftMotors = new SpeedControllerGroup(leftFront, leftBack);
        rightMotors = new SpeedControllerGroup(rightFront, rightBack);

        differentialDrive = new DifferentialDrive(leftMotors, rightMotors);
        leftFront.setInverted(true);
        leftBack.setInverted(true);

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
        // System.out.println("Right distance travelled: " + getRightEncoderDistance());
        // System.out.println("Left distance travelled: " + getLeftEncoderDistance());
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

    public double getAverageEncoderDistance() {
        return (getLeftEncoderDistance() + getRightEncoderDistance()) / 2.0;
    }

    public void setMaxOutput(double maxOutput) {
        differentialDrive.setMaxOutput(maxOutput);
    }

    public double getHeading() {
        return RobotContainer.gyro.getRotation2d().getDegrees();
    }

    public void setValues(double m1, double m2, double m3, double m4) {
        differentialDrive.tankDrive(m1, m3);
        differentialDrive.feed();
    }

    public void arcadeDrive(double y, double turn) {
        differentialDrive.arcadeDrive(y, turn);
    }
}