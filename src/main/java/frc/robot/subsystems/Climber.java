package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class Climber extends SubsystemBase {

    private final CANSparkMax leftClimb;
    private final CANSparkMax rightClimb;

    public Climber() {
        leftClimb = new CANSparkMax(Constants.CLIMBER_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightClimb = new CANSparkMax(Constants.CLIMBER_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftClimb.set(0);
        rightClimb.set(0);
    }

    @Override
    public void periodic() {
        double amount = Math.max(Math.abs(RobotContainer.controller.getRawAxis(Constants.RT_AXIS)), Math.abs(RobotContainer.controller.getRawAxis(Constants.LT_AXIS)));
        if(Math.abs(amount) < 0.1) return;
        leftClimb.set(amount);
        rightClimb.set(amount);
    }
}
