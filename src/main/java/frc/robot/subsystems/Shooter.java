package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveOdometry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

    private final TalonFX top;
    private final TalonFX bottom;
    private double motorSpeed;
    // private final MecanumDriveOdometry odometry;
    // private final MecanumDriveKinematics kinematics;

    public Shooter() {
        top = new TalonFX(Constants.SHOOTER_TOP);
        bottom = new TalonFX(Constants.SHOOTER_BOTTOM);

        top.configFactoryDefault();
        bottom.configFactoryDefault();

        bottom.setInverted(true);
        top.setSensorPhase(false);
        bottom.follow(top);
        top.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        bottom.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);

        top.configNeutralDeadband(0.001);
        bottom.configNeutralDeadband(0.001);

        top.configNominalOutputForward(0, Constants.kTimeoutMs);
        top.configNominalOutputReverse(0, Constants.kTimeoutMs);
        top.configPeakOutputForward(1, Constants.kTimeoutMs);
        top.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        bottom.configNominalOutputForward(0, Constants.kTimeoutMs);
        bottom.configNominalOutputReverse(0, Constants.kTimeoutMs);
        bottom.configPeakOutputForward(1, Constants.kTimeoutMs);
        bottom.configPeakOutputReverse(-1, Constants.kTimeoutMs);
        double val = 0.28;
        top.config_kP(Constants.kPIDLoopIdx, val, Constants.kTimeoutMs);
        top.config_kF(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

        top.config_kD(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs); // start with small kp, then double until
                                                                       // oscillations, then increase d
        top.config_kI(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

        bottom.config_kP(Constants.kPIDLoopIdx, val, Constants.kTimeoutMs);
        bottom.config_kF(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

        bottom.config_kD(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);
        top.config_kI(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

        top.setNeutralMode(NeutralMode.Brake);
        bottom.setNeutralMode(NeutralMode.Brake);

    }

    @Override
    public void periodic() {
        double speed = motorSpeed * 2000.0 * 2048.0 / 600.0;
        top.set(TalonFXControlMode.Velocity, speed);
        bottom.set(TalonFXControlMode.Velocity, speed);

        System.out.println(top.getSelectedSensorVelocity());
        System.out.println(bottom.getSelectedSensorVelocity());
    }

    public void on() {
        motorSpeed = 0.8;
    }

    public void off() {
        motorSpeed = 0;
    }

}
