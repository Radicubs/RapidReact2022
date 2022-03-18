package frc.robot.subsystems;

import frc.robot.Constants;

public class Elevator extends OnOffSystem {

    public Elevator() {super(Constants.ELEVATOR_MOTOR, -0.45);}

    public void set(double speed) {motorSpeed = speed;}
}
