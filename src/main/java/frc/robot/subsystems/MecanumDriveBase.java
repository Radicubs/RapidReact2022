package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveOdometry;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.MecanumDriveCommand;
import frc.robot.commands.TankDrive;

import java.util.Arrays;
import java.util.List;

public class MecanumDriveBase extends SubsystemBase {

    private final WPI_TalonFX rightFront;
    private final WPI_TalonFX rightBack;
    private final WPI_TalonFX leftFront;
    private final WPI_TalonFX leftBack;
    private final MecanumDriveKinematics kinematics;
    private final MecanumDriveOdometry odometry;


    public MecanumDriveBase() {
        kinematics = new MecanumDriveKinematics(Constants.frontLeft, Constants.frontRight, Constants.backLeft, Constants.backRight);
        odometry = new MecanumDriveOdometry(kinematics, Rotation2d.fromDegrees(0), new Pose2d());//TODO set to actual tarmac vals

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

            /* Config the Velocity closed loop gains in slot0 */
            motor.config_kF(Constants.kPIDLoopIdx, Constants.kF, Constants.kTimeoutMs);
            motor.config_kP(Constants.kPIDLoopIdx, Constants.kP, Constants.kTimeoutMs);
            motor.config_kI(Constants.kPIDLoopIdx, Constants.kI, Constants.kTimeoutMs);
            motor.config_kD(Constants.kPIDLoopIdx, Constants.kD, Constants.kTimeoutMs);

            // Might interfere with PID

            motor.setNeutralMode(NeutralMode.Brake);
        }

        leftFront.setInverted(true);
        leftBack.setInverted(true);

        setDefaultCommand(new MecanumDriveCommand(this));
    }

    public void setValues(double m1, double m2, double m3, double m4) {
        m1 = m1 * 2000.0 * 2048.0 / 600.0;
        m2 = m2 * 2000.0 * 2048.0 / 600.0;
        m3 = m3 * 2000.0 * 2048.0 / 600.0;
        m4 = m4 * 2000.0 * 2048.0 / 600.0;
        //2000 = motor free speed (rpm)
        //2048 = encoder ticks per rev
        //600 = 60 * 10 = encoder ticks are measured in 100ms * 10 = seconds * 60 = per minute

        rightBack.set(TalonFXControlMode.Velocity, m1);
        rightFront.set(TalonFXControlMode.Velocity, m2);
        leftBack.set(TalonFXControlMode.Velocity, m3);
        leftFront.set(TalonFXControlMode.Velocity, m4);
    }

    @Override
    public void periodic() {
        MecanumDriveWheelSpeeds speeds = new MecanumDriveWheelSpeeds(
                leftFront.getSelectedSensorVelocity() * 10 / 2048 * 6380,
                rightFront.getSelectedSensorVelocity() * 10 / 2048 * 6380,
                leftBack.getSelectedSensorVelocity() * 10 / 2048 * 6380,
                rightBack.getSelectedSensorVelocity() * 10 / 2048 * 6380);

        odometry.update(Rotation2d.fromDegrees(RobotContainer.gyro.getAngle()), speeds);
    }
}