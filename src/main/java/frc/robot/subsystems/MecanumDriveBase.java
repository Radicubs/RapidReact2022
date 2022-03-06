package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;

public class MecanumDriveBase extends DriveBase {

    public void setValues(double m1, double m2, double m3, double m4) {
        m1 = m1 * 2000.0 * 2048.0 / 600.0;
        m2 = m2 * 2000.0 * 2048.0 / 600.0;
        m3 = m3 * 2000.0 * 2048.0 / 600.0;
        m4 = m4 * 2000.0 * 2048.0 / 600.0;

        rightBack.set(TalonFXControlMode.Velocity, m1);
        rightFront.set(TalonFXControlMode.Velocity, m2);
        leftBack.set(TalonFXControlMode.Velocity, m3);
        leftFront.set(TalonFXControlMode.Velocity, m4);
    }
}
