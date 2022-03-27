package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.Arrays;
import java.util.List;

public class Shooter extends SubsystemBase implements StartableSystem {

    public final WPI_TalonFX top;
    public final WPI_TalonFX bottom;
    private double motorSpeed;
    // private final MecanumDriveOdometry odometry;
    // private final MecanumDriveKinematics kinematics;

    private final double kP = 0.23;
    private final double kI = 0.004;
    private final double kD = 0.8;
    private SendableChooser<Double> speed;

    public Shooter() {
        speed = new SendableChooser<>();
        speed.setDefaultOption("38", 0.38);
        speed.addOption("39", 0.39);
        speed.addOption("40", 0.40);
        speed.addOption("41", 0.41);
        speed.addOption("42", 0.42);
        speed.addOption("37", 0.37);
        speed.addOption("36", 0.36);
        speed.addOption("35", 0.35);
        SmartDashboard.putData(speed);
        SmartDashboard.updateValues();


        top = new WPI_TalonFX(Constants.SHOOTER_TOP);
        bottom = new WPI_TalonFX(Constants.SHOOTER_BOTTOM);
        top.configFactoryDefault();
        bottom.configFactoryDefault();

        bottom.setInverted(true);
        top.setSensorPhase(false);

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
                
        top.config_kP(Constants.kPIDLoopIdx, kP, Constants.kTimeoutMs);
        top.config_kF(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

        top.config_kD(Constants.kPIDLoopIdx, kD, Constants.kTimeoutMs); // start with small kp, then double until
                                                                       // oscillations, then increase d
        top.config_kI(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

        bottom.config_kP(Constants.kPIDLoopIdx, kP, Constants.kTimeoutMs);
        bottom.config_kF(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

        bottom.config_kD(Constants.kPIDLoopIdx, kD, Constants.kTimeoutMs);
        top.config_kI(Constants.kPIDLoopIdx, 0, Constants.kTimeoutMs);

        top.setNeutralMode(NeutralMode.Coast);
        bottom.setNeutralMode(NeutralMode.Coast);

    }

    @Override
    public void periodic() {
        double speed = motorSpeed * 6380 * 2048.0 / 600.0;
        top.set(TalonFXControlMode.Velocity, speed);
        bottom.set(TalonFXControlMode.Velocity, speed);

        //System.out.println(top.getSelectedSensorVelocity());
        //System.out.println(speed);

        NetworkTableInstance.getDefault().getTable("subs").getEntry("Shooter").setBoolean(motorSpeed > 0);
    }

    @Override
    public void on() {motorSpeed = speed.getSelected();}

    public void shooterSlowForward() {motorSpeed = 0.1;}

    @Override
    public void backwards() {motorSpeed = -0.1;}

    @Override
    public void off() {motorSpeed = 0;}

}
