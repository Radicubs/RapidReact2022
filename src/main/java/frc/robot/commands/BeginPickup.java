package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class BeginPickup extends CommandBase {

    private final Intake intake;
    private final Index index;
    private final Elevator elevator;
    private final Shooter shooter;
    private boolean isFinished;
    private int counter;
    private boolean seenRed;

    public BeginPickup(Intake intake, Index index, Elevator elevator, Shooter shooter) {
        this.index = index;
        this.intake = intake;
        this.elevator = elevator;
        this.shooter = shooter;
    }

    @Override
    public void initialize() {
        intake.on();
        index.on();
        elevator.on();
    }

    @Override
    public void execute() {
        //if(!RobotContainer.color.isBlue() && !RobotContainer.color.isRed()) {
        //    System.out.println("NONE");
        //    return;
        //}
        RobotContainer.color.getDiagnostics();
        if(RobotContainer.color.isRed()) {
            System.out.println("RED");
            shooter.shooterSlowForward();
            seenRed = true;
        }

        else if(RobotContainer.color.isBlue()) {
            elevator.off();
        }

        if (seenRed) {
            counter += 1;
        }
    }

    @Override
    public boolean isFinished() {return counter == 100;} // needs to be optimized (lowered)

    @Override
    public void end(boolean interrupted) {
        elevator.off();
        shooter.off();
        index.off();
        intake.off();
    }
}
