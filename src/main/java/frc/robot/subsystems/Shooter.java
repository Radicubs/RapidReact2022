package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
    private TalonFX shooterMotorOne;
    private TalonFX shooterMotorTwo;

    private double speed = 0;
    // private double speed = 0.1;

    public Shooter() {

        // constructor
        shooterMotorOne = new TalonFX(Constants.SHOOTER_LEFT);
        shooterMotorTwo = new TalonFX(Constants.SHOOTER_RIGHT);

        shooterMotorOne.configFactoryDefault();
        shooterMotorTwo.configFactoryDefault();

        shooterMotorOne.setNeutralMode(NeutralMode.Coast);
        shooterMotorTwo.setNeutralMode(NeutralMode.Coast);

        shooterMotorTwo.setInverted(true);
        shooterMotorOne.setSensorPhase(false);
        shooterMotorTwo.follow(shooterMotorOne);
        shooterMotorOne.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);

        // shooterMotorOne.config_kP(0, .05);
        // shooterMotorOne.config_kF(0, 0.0499999523);
        // shooterMotorOne.config_kD(0, .9);
    }

    @Override
    public void periodic() {
        // shooterMotorOne.set(ControlMode.Velocity, speed);
        // shooterMotorTwo.set(ControlMode.Velocity, speed);
        shooterMotorOne.set(ControlMode.PercentOutput, speed);
        shooterMotorTwo.set(ControlMode.PercentOutput, speed);
    }

    public void shooterOn() {

        speed = 0.2;
    }

    public void shooterOff() {

        speed = 0;
    }
}
