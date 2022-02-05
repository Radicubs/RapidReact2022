package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.TankDrive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DriveBase extends SubsystemBase {

    private VictorSPX RMF; // Right Motor Front
    private VictorSPX LMF; // Left Motor Front
    private VictorSPX RMB; // Right Motor Back
    private VictorSPX LMB; // Left Motor Back
    private List<VictorSPX> motors;

    public DriveBase() {
        RMF = new VictorSPX(Constants.rightMotorFront);
        RMB = new VictorSPX(Constants.rightMotorBack);
        LMF = new VictorSPX(Constants.leftMotorFront);
        LMB = new VictorSPX(Constants.leftMotorBack);

        motors = Arrays.asList(RMF, RMB, LMF, LMB);

        for (VictorSPX motor : motors) {
            motor.configFactoryDefault();
            motor.setNeutralMode(NeutralMode.Brake);
        }

        RMF.setInverted(true);
        RMB.setInverted(true);

        setDefaultCommand(new TankDrive(this));
    }

    public void setValues(double m1, double m2, double m3, double m4) {
        RMB.set(VictorSPXControlMode.PercentOutput, m1);
        RMF.set(VictorSPXControlMode.PercentOutput, m2);
        LMB.set(VictorSPXControlMode.PercentOutput, m3);
        LMF.set(VictorSPXControlMode.PercentOutput, m4);
    }
}
// I wanted to be a contributor lol -tanmay