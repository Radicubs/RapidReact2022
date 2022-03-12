package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

    private final TalonFX top;
    private final TalonFX bottom;
    private double motorSpeed;

    public Shooter() {
        top = new TalonFX(Constants.SHOOTER_TOP);
        bottom = new TalonFX(Constants.SHOOTER_BOTTOM);
        top.configFactoryDefault();
        bottom.configFactoryDefault();
        bottom.setInverted(true);
        motorSpeed = 0;
    }

    @Override
    public void periodic() {
        top.set(TalonFXControlMode.PercentOutput, motorSpeed);
        bottom.set(TalonFXControlMode.PercentOutput, motorSpeed);
    }

    public void on() {motorSpeed = 0.8;}

    public void off() {motorSpeed = 0;}
}
