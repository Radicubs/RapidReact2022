package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class UltrasonicSensor extends SubsystemBase {

    private final AnalogInput ultrasonic;
    // possibly try ultrasonic = new AnalogPotentiometer(0, 1024, 0);
    public UltrasonicSensor() {ultrasonic = new AnalogInput(0);}

    // possibly try return ultrasonic.get() * 5;
    public double getRange() {return ultrasonic.getValue();}

    //@Override
    //public void periodic() {
        //System.out.println("Ultrasonic: " + getRange());
    //}

}
