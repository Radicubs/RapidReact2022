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
    // private int numBalls;

    public Shoot(Elevator elevator, Shooter shooter) {
        this.elevator = elevator;
        this.shooter = shooter;
        hasLeftElevator = false;
        counter = 0;
        // this.numBalls = numBalls;
    }

    @Override
    public void initialize() {
        shooter.on();
        // elevator.on();
    }

    @Override
    public void execute() {

        if (shooter.top.getSelectedSensorVelocity() > 1800 && shooter.bottom.getSelectedSensorVelocity() > 1800) {
            elevator.on();
        }

        /*
        if (numBalls == 1) {
            if(!RobotContainer.elevatorColor.isBlue() && !RobotContainer.elevatorColor.isRed()) hasLeftElevator = true;
        } else if (numBalls == 2) {

        }

        if(hasLeftElevator) {
            counter++;
        }
        */
    }

    @Override
    public boolean isFinished() {
        // return counter == 200;
        return false;
    }

    @Override
    public void end(boolean interrupted) {shooter.off(); elevator.off();}
}
