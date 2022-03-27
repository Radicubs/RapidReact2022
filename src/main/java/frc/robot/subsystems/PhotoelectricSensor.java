package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PhotoelectricSensor extends SubsystemBase {

    private final DigitalInput input;

    public PhotoelectricSensor(int DID) {
        input = new DigitalInput(DID);
    }

    public boolean get_on() {
        return input.get();
    }


}
