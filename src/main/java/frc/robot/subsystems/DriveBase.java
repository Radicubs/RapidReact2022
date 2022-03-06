package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.Arrays;
import java.util.List;

public abstract class DriveBase extends SubsystemBase {

    protected WPI_TalonFX rightFront;
    protected WPI_TalonFX rightBack;
    protected WPI_TalonFX leftFront;
    protected WPI_TalonFX leftBack;

    public DriveBase() {
        rightFront = new WPI_TalonFX(Constants.RIGHT_FALCON_FRONT);
        rightBack = new WPI_TalonFX(Constants.RIGHT_FALCON_BACK);
        leftFront = new WPI_TalonFX(Constants.LEFT_FALCON_FRONT);
        leftBack = new WPI_TalonFX(Constants.LEFT_FALCON_BACK);

        List<WPI_TalonFX> motors = Arrays.asList(rightBack, rightFront, leftBack, leftFront);

        for(WPI_TalonFX motor : motors) {
            motor.configFactoryDefault();

            motor.configNeutralDeadband(0.001);

            /* Config sensor used for Primary PID [Velocity] */

            motor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, Constants.kPIDLoopIdx,
                    Constants.kTimeoutMs);

            /* Config the peak and nominal outputs */
            motor.configNominalOutputForward(0, Constants.kTimeoutMs);
            motor.configNominalOutputReverse(0, Constants.kTimeoutMs);
            motor.configPeakOutputForward(1, Constants.kTimeoutMs);
            motor.configPeakOutputReverse(-1, Constants.kTimeoutMs);

            /* Config the Velocity closed loop gains in slot0 */
            motor.config_kF(Constants.kPIDLoopIdx, Constants.kF, Constants.kTimeoutMs);
            motor.config_kP(Constants.kPIDLoopIdx, Constants.kP, Constants.kTimeoutMs);
            motor.config_kI(Constants.kPIDLoopIdx, Constants.kI, Constants.kTimeoutMs);
            motor.config_kD(Constants.kPIDLoopIdx, Constants.kD, Constants.kTimeoutMs);

            // Might interfere with PID

            motor.setNeutralMode(NeutralMode.Brake);
        }

        leftFront.setInverted(true);
        leftBack.setInverted(true);
    }
}