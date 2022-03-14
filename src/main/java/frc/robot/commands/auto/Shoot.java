package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Shooter;

public class Shoot extends CommandBase {

    private final Elevator elevator;
    private final Shooter shooter;
    private boolean hasLeftElevator;
    private int counter;

    public Shoot(Elevator elevator, Shooter shooter) {
        this.elevator = elevator;
        this.shooter = shooter;
        hasLeftElevator = false;
        counter = 0;
    }

    @Override
    public void initialize() {
        shooter.on();
        elevator.on();
    }

    @Override
    public void execute() {
        if(!RobotContainer.color.isBlue() && !RobotContainer.color.isRed()) hasLeftElevator = true;

        if(hasLeftElevator) {
            counter++;
        }
    }

    @Override
    public boolean isFinished() {return counter == 200;}

    @Override
    public void end(boolean interrupted) {shooter.off(); elevator.off();}
}
