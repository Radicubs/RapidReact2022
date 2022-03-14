package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.Arrays;
import java.util.List;

public class Shooter extends SubsystemBase implements StartableSystem {

    private final WPI_TalonFX top;
    private final WPI_TalonFX bottom;
    private double motorSpeed;

    private final double kP = 0.117;
    private final double kI = 0;
    private final double kD = 0;

    public Shooter() {
        top = new WPI_TalonFX(Constants.SHOOTER_TOP);
        bottom = new WPI_TalonFX(Constants.SHOOTER_BOTTOM);
        top.configFactoryDefault();
        bottom.configFactoryDefault();

        bottom.setInverted(true);
        motorSpeed = 0;
    }

    @Override
    public void periodic() {
        top.set(TalonFXControlMode.PercentOutput, motorSpeed);
        bottom.set(TalonFXControlMode.PercentOutput, motorSpeed);
        System.out.println("TOP: " + ((double) top.getSelectedSensorVelocity() / 6380 / 2048 * 600) + " BOTTOM: " + ((double) bottom.getSelectedSensorVelocity() / 6380 / 2048 * 600));
    }

    @Override
    public void on() {motorSpeed = 0.5;}

    public void shooterSlowForward() {motorSpeed = 0.1;}

    @Override
    public void backwards() {motorSpeed = -0.1;}

    @Override
    public void off() {motorSpeed = 0;}

}
