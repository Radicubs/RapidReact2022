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
        RMF = new VictorSPX(0);
        RMB = new VictorSPX(1);
        LMF = new VictorSPX(2);
        LMB = new VictorSPX(3);

        motors = Arrays.asList(RMF, RMB, LMF, LMB);

        for(VictorSPX motor : motors) {
            motor.configFactoryDefault();
            motor.setNeutralMode(NeutralMode.Brake);
        }

        LMF.setInverted(true);
        LMB.setInverted(true);

        setDefaultCommand(new TankDrive(this));
    }

    public void setValues(double m1, double m2, double m3, double m4) {
        m1 = m1 * 2000.0 * 2048.0 / 600.0;
        m2 = m2 * 2000.0 * 2048.0 / 600.0;
        m3 = m3 * 2000.0 * 2048.0 / 600.0;
        m4 = m4 * 2000.0 * 2048.0 / 600.0;

        RMB.set(VictorSPXControlMode.Velocity, m1);
        RMF.set(VictorSPXControlMode.Velocity, m2);
        LMB.set(VictorSPXControlMode.Velocity, m3);
        LMF.set(VictorSPXControlMode.Velocity, m4);
    }
}