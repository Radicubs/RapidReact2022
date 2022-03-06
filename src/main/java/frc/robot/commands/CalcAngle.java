package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.DifferentialDriveBase;

public class CalcAngle {

    private final DifferentialDriveBase base;
    private final Translation2d target;

    public double targetAngle = 0;

    public CalcAngle(Translation2d target, DifferentialDriveBase base) {
        this.target = target;
        this.base = base;
    }

    public double calcAngle() {
        System.out.println(base.getPose());
        if (targetAngle != 0) {
            return targetAngle;
        } else {
            targetAngle = Math.atan((base.getPose().getX() - target.getX()) / (base.getPose().getY() - target.getY())) * (180.0 / Math.PI) + base.getPose().getRotation().getDegrees();
            return targetAngle;
        }
    }
}
