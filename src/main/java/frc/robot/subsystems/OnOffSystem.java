package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class OnOffSystem extends SubsystemBase implements StartableSystem {

    private final CANSparkMax motor;
    protected double motorSpeed;
    private final double highSpeed;
    private final String subName;

    public OnOffSystem(final int id, final double highSpeed, final String subName) {
        motor = new CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless);
        motorSpeed = 0;
        this.highSpeed = highSpeed;
        this.subName = subName;
    }

    @Override
    public void periodic() {
        motor.set(motorSpeed);
        NetworkTableInstance.getDefault().getTable("subs").getEntry(subName).setBoolean(Math.abs(motorSpeed) > 0);
    }

    public void on() {motorSpeed = highSpeed;}

    public void off() {motorSpeed = 0;}

    public void backwards() {motorSpeed = -highSpeed;}
}
