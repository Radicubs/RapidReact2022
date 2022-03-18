package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.MecanumDriveBase;

public class Rotate extends CommandBase {

    private final MecanumDriveBase base;
    private final int wantDegrees;

    public Rotate(MecanumDriveBase base, int wantDegrees) {
        this.base = base;
        this.wantDegrees = wantDegrees;
    }

    @Override
    public void execute() {
        double error = wantDegrees - RobotContainer.gyro.getAngle();

        if(error < 0) base.setPercent(0.1, 0.1, -0.1, -0.1);
        else if(error > 0) base.setPercent(-0.1, -0.1, 0.1, 0.1);
    }

    @Override
    public boolean isFinished() {return Math.abs(wantDegrees - RobotContainer.gyro.getAngle()) < 10;}

    @Override
    public void end(boolean interrupted) {
        base.setPercent(0, 0, 0, 0);
    }

}
