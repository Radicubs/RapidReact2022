package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gyro extends SubsystemBase {

    private final ADXRS450_Gyro gyro;

    public Gyro() {
        gyro = new ADXRS450_Gyro();
        gyro.calibrate();
        System.out.println("Gyro calibrated!");
    }

    public double getRate() {return gyro.getRate();}

    public double getAngle() {return gyro.getAngle();}

    public void recal() {
        gyro.calibrate();
        System.out.println("Gyro recalibrated to " + getAngle());
    }

    public Rotation2d getRotation2d() {
        return gyro.getRotation2d();
    }

}
