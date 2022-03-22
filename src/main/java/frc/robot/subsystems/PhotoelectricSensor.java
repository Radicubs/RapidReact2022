package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PhotoelectricSensor extends SubsystemBase {

    private boolean is_on = false;
    private final DigitalInput input;

    public PhotoelectricSensor(int DID) {
        input = new DigitalInput(DID);
    }

    public boolean get_on() {
        return is_on;
    }

    @Override
    public void periodic() {
        is_on = input.get();
        // System.out.println(is_on);
    }

}
