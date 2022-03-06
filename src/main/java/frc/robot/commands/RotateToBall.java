package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBase;

import java.util.Arrays;
import java.util.InputMismatchException;

import static java.lang.Double.parseDouble;

public class RotateToBall extends CommandBase {

    private final DriveBase drive;
    private final NetworkTableEntry entry;
    private boolean isFinished = false;
    private double previous = 0;

    public RotateToBall(DriveBase drive) {
        this.drive = drive;
        entry = RobotContainer.networkTable.getTable("opencv").getEntry("latest");

        addRequirements(drive);
    }



    @Override
    public void execute() {

        String[] entry = this.entry.getString("null null null null").split(" ");
        System.out.println(Arrays.toString(entry));


        if(entry.length != 4) {
            System.out.println("ERROR: " + Arrays.toString(entry) + " Length = " + entry.length);
            return;
        }

        double cam = -1; // 0 front, 1 left, 2 back, 3 right
        double x = -1; // X value of the location of the ball
        double y = -1; // Y value of the location ball
        double radius = -1; // radius of the ball, indicates size

        try {
            cam = Double.parseDouble(entry[0]);
            x = Double.parseDouble(entry[1]);
            y = Double.parseDouble(entry[2]);
            radius = Double.parseDouble(entry[3]);
        } catch(NumberFormatException e) {
            return;
        }

        if (x == -1 || x == 0) {
            previous /= 2;
            drive.setValues(previous, previous, previous, previous);
            return;
        }

        /*if(cam == 0 && (x > (Constants.MaxCV / 2) - 50 && x < (Constants.MaxCV / 2) + 50)) {
            isFinished = true;
            return;
        }

        boolean isLeft = false;

        //For two cameras only, follow the comment instructions

        if(cam == 2) { //change this to 1
            if(x < Constants.MaxCV / 2) {
                isLeft = true;
            }
        }

        else if(cam == 1 || (cam == 0 && x < Constants.MaxCV / 2)) isLeft = true;//take out the first part of the or*/

        double turn = ((x - Constants.MaxCV / 2) / 750);
        previous = turn;
        drive.setValues(-turn, -turn, turn, turn);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void end(boolean isFinished) {
    }




}
