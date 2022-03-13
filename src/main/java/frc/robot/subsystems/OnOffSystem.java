package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class OnOffSystem extends SubsystemBase {

    private final CANSparkMax motor;
    protected double motorSpeed;
    private final double highSpeed;

    public OnOffSystem(final int id, final double highSpeed) {
        motor = new CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless);
        motorSpeed = 0;
        this.highSpeed = highSpeed;
    }

    @Override
    public void periodic() {motor.set(motorSpeed);}

    public void on() {motorSpeed = highSpeed;}

    public void off() {motorSpeed = 0;}
}
