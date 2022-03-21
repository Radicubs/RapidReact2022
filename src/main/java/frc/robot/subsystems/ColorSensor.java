package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensor extends SubsystemBase {

    private final int redThreshold = 10000;
    private final int blueThreshold = 10000;
    private final ColorSensorV3 color;

    public ColorSensor(I2C.Port port) {
        color = new ColorSensorV3(port);
    }

    public Color getColor() {
        return color.getColor();
    }

    @Override
    public void periodic() {
        // System.out.println("COLOR: " + color.getRed() + " " + color.getGreen() + " " + color.getBlue());
    }

    public boolean isRed() {return (((double) color.getRed()) / color.getBlue() > 1.5);}

    public boolean isBlue() {return (((double) color.getBlue()) / color.getRed() > 1.5);}

    public void getDiagnostics() {
        //System.out.println((double) color.getRed() / color.getBlue());
    }
}
