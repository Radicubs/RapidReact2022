package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;

public class Pickup extends CommandBase {

    private final Intake intake;
    private final Index index;
    private final Elevator elevator;
    private final Shooter shooter;
    private boolean interrupt;
    private int counter;
    private boolean seenRed;
    private final MecanumDriveBase base;

    public Pickup(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter) {
        this.index = index;
        this.intake = intake;
        this.elevator = elevator;
        this.shooter = shooter;
        interrupt = false;
        this.base = base;
    }

    @Override
    public void initialize() {
        intake.on();
        index.on();
        elevator.on();
        base.setPercent(0.1, 0.1, -0.1, -0.1);
    }

    @Override
    public void execute() {
        //if(!RobotContainer.color.isBlue() && !RobotContainer.color.isRed()) {
        //    System.out.println("NONE");
        //    return;
        //}

        if(RobotContainer.color.isRed()) {
            System.out.println("RED");
            shooter.shooterSlowForward();
            seenRed = true;
        }

        else if(RobotContainer.color.isBlue()) {
            elevator.off();
            interrupt = true;
        }

        if (seenRed) {
            counter += 1;
        }
    }

    @Override
    public boolean isFinished() {return counter == 100 || interrupt;} // needs to be optimized (lowered)

    @Override
    public void end(boolean interrupted) {
        elevator.off();
        shooter.off();
        index.off();
        intake.off();
    }
}
