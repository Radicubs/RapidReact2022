package frc.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class Gyro extends SubsystemBase {

    private final ADXRS450_Gyro gyro;

    public Gyro() {
        gyro = new ADXRS450_Gyro();
        gyro.calibrate();
        System.out.println("Gyro callibrated!");
    }

    public double getRate() {return gyro.getRate();}

    public double getAngle() {return gyro.getAngle();}

    @Override
    public void periodic() {
        if(RobotContainer.controller.getRawButton(Constants.LEFT_TRIGGER)) gyro.calibrate();
    }

}
