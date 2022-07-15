package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class Winch extends SubsystemBase {

    private final CANSparkMax motor;

    public Winch() {
        motor = new CANSparkMax(Constants.WINCH_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless);
    }

    @Override
    public void periodic() {
        if(RobotContainer.controller.getRawButton(Constants.BACK_BUTTON)) motor.set(0.25);
        else if(RobotContainer.controller.getRawButton(Constants.START_BUTTON)) motor.set(-0.25);
        else motor.set(0);
    }

}
