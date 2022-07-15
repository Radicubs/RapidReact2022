package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Constants;

public class Elevator extends OnOffSystem {

    public Elevator() {super(Constants.ELEVATOR_MOTOR, -0.5, "Elevator");}

    public void set(double speed) {motorSpeed = speed;}
}
