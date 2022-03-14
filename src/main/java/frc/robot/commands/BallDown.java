package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class BallDown extends CommandBase {

    private final Elevator elevator;
    private final Shooter shooter;
    private final Intake intake;
    private final Index index;

    public BallDown(Elevator elevator, Shooter shooter, Index index, Intake intake) {
        this.elevator = elevator;
        this.shooter = shooter;
        this.intake = intake;
        this.index = index;
        addRequirements(shooter, elevator, index, intake);
    }

    @Override
    public void initialize() {
        elevator.set(0.35);
        shooter.backwards();
        index.backwards();
        intake.backwards();
    }

    @Override
    public void end(boolean inturrupted) {
        elevator.off();
        shooter.off();
        intake.off();
        index.off();
    }

}
