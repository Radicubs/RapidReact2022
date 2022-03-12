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
        leftClimb.setInverted(true);
        rightClimb.set(0);
    }

    @Override
    public void periodic() {
        double left = RobotContainer.controller.getRawAxis(Constants.LT_AXIS);
        double right = -RobotContainer.controller.getRawAxis(Constants.RT_AXIS);

        leftClimb.set(left + right);
        rightClimb.set(left + right);
    }
}
