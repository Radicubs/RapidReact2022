package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class Climber extends SubsystemBase {

    private final CANSparkMax leftClimb;
    private final CANSparkMax rightClimb;
    private int climbArms;

    public Climber() {
        leftClimb = new CANSparkMax(Constants.CLIMBER_LEFT, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightClimb = new CANSparkMax(Constants.CLIMBER_RIGHT, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftClimb.set(0);
        leftClimb.setInverted(true);
        rightClimb.set(0);
        climbArms = 0;
    }

    @Override
    public void periodic() {
        if(RobotContainer.controller.getPOV() == 0) climbArms = 0;
        else if(RobotContainer.controller.getPOV() == 90) climbArms = 1;
        else if(RobotContainer.controller.getPOV() == 270) climbArms = 2;


        double left = RobotContainer.controller.getRawAxis(Constants.LT_AXIS);
        double right = -RobotContainer.controller.getRawAxis(Constants.RT_AXIS);

        if(climbArms == 0 || climbArms == 2) leftClimb.set(left + right);
        else leftClimb.set(0);

        if(climbArms == 0 || climbArms == 1) rightClimb.set(left + right);
        else rightClimb.set(0);
    }
}
