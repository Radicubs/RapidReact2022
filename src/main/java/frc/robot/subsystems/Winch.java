package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
// import edu.wpi.first.wpilibj2.command.;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class Winch extends SubsystemBase {

    private final VictorSPX motor;
    private boolean direction;
    public Winch()  {
        motor = new VictorSPX(Constants.WINCH_MOTOR);
        motor.setNeutralMode(NeutralMode.Brake);
        motor.configFactoryDefault();
    }

    @Override
    public void periodic() {
        if(RobotContainer.controller.getRawButton(Constants.BACK_BUTTON)) motor.set(VictorSPXControlMode.PercentOutput, 0.1);
        else if(RobotContainer.controller.getRawButton(Constants.START_BUTTON)) motor.set(VictorSPXControlMode.PercentOutput, -0.1);
        else motor.set(VictorSPXControlMode.PercentOutput, 0);
    }

}
