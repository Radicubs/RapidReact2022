package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.Gyro;

public class FieldOrientedDrive extends CommandBase {

    private final Gyro gyro;
    private final DriveBase base;

    public FieldOrientedDrive(DriveBase base, Gyro gyro) {
        this.gyro = gyro;
        this.base = base;
        addRequirements(gyro);
        addRequirements(base);
    }

    @Override
    public void execute() {

    }
}
