package frc.robot.commands;

import org.w3c.dom.css.ElementCSSInlineStyle;

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
    private boolean seenColor;
    private final MecanumDriveBase base;
    private final boolean ballPreloaded;
    private final ColorSensor colorSensor;
    private boolean isDone = false;

    public Pickup(MecanumDriveBase base, Intake intake, Index index, Elevator elevator, Shooter shooter, boolean ballPreloaded) {
        
        addRequirements(base, intake, index, elevator, shooter);

        this.index = index;
        this.intake = intake;
        this.elevator = elevator;
        this.shooter = shooter;
        interrupt = false;
        this.base = base;
        this.ballPreloaded = ballPreloaded;

        if (ballPreloaded) {
            colorSensor = RobotContainer.indexColor;
        } else {
            colorSensor = RobotContainer.elevatorColor;
        }
    }

    @Override
    public void initialize() {
        intake.on();
        index.on();
        elevator.on();
        base.setPercent(-0.25, -0.25, -0.25, -0.25);
    }


    @Override
    public void execute() {
        //if(!RobotContainer.color.isBlue() && !RobotContainer.color.isRed()) {
        //    System.out.println("NONE");
        //    return;
        //}

        if (colorSensor.isRed() || colorSensor.isBlue()) {
            shooter.shooterSlowForward();
            colorSensor.getDiagnostics();
            seenColor = true;
            isDone = true;
        }

        /*
        if (seenColor) {
            counter += 1;
        }
        if(colorSensor.isRed()) {
            System.out.println("RED");
            shooter.shooterSlowForward();
            seenRed = true;
        }

        else if(colorSensor.isBlue()) {
            elevator.off();
            interrupt = true;
        }

        if (seenRed) {
            counter += 1;
        }
        */
    }

    @Override
    public boolean isFinished() {
        // return counter == 100 || interrupt;
        return isDone;
    } // needs to be optimized (lowered)

    @Override
    public void end(boolean interrupted) {
        elevator.off();
        shooter.off();
        index.off();
        intake.off();
    }
}
