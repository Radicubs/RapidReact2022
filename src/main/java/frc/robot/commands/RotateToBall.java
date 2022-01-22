package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBase;

import java.util.InputMismatchException;

public class RotateToBall extends CommandBase {

    private final DriveBase drive;
    private final NetworkTableEntry entry;
    private boolean isFinished = false;

    public RotateToBall(DriveBase drive) {
        this.drive = drive;
        entry = RobotContainer.networkTable.getTable("opencv").getEntry("latest");

        addRequirements(drive);
    }



    @Override
    public void execute() {

        String[] entry = this.entry.getString("").split("null null null null");

        if(entry.length != 4) throw new InputMismatchException("OpenCV Networking is not responding with correct values!");

        int cam = 0; // 0 front, 1 left, 2 back, 3 right
        int x = 0; // X value of the location of the ball
        int y = 0; // Y value of the location ball
        int radius = 0; // radius of the ball, indicates size

        try {
            cam = Integer.parseInt(entry[0]);
            x = Integer.parseInt(entry[1]);
            y = Integer.parseInt(entry[2]);
            radius = Integer.parseInt(entry[3]);
        } catch(NumberFormatException e) {
            return;
        }


        if(cam == 0 && (x > (Constants.MaxCVY / 2) - 50 && x < (Constants.MaxCVY / 2) + 50)) {
            isFinished = true;
            return;
        }

        boolean isLeft = false;

        //For two cameras only, follow the comment instructions

        if(cam == 2) { //change this to 1
            if(x < Constants.MaxCVX / 2) {
                isLeft = true;
            }
        }

        else if(cam == 1 || (cam == 0 && x < Constants.MaxCVY / 2)) isLeft = true;//take out the first part of the or

        int turnPercent = isLeft ? 10 : -10;
        drive.setValues(turnPercent, turnPercent, turnPercent, turnPercent);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void end(boolean isFinished) {
    }




}
