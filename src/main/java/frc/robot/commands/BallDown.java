package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Shooter;

public class BallDown extends CommandBase {

    private final Elevator elevator;
    private final Shooter shooter;

    public BallDown(Elevator elevator, Shooter shooter) {
        this.elevator = elevator;
        this.shooter = shooter;
        addRequirements(shooter, elevator);
    }

    @Override
    public void initialize() {
        elevator.set(0.25);
        shooter.shooterSlowBackward();
    }

    @Override
    public void end(boolean inturrupted) {
        elevator.off();
        shooter.off();
    }

}
