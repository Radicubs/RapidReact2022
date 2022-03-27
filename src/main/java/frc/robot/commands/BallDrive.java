package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.MecanumDriveBase;

public class BallDrive extends CommandBase {

    private final MecanumDriveBase base;
    private final NetworkTableEntry cam;
    private boolean isFinished;
    private final double lowestBallPos = 295; //TODO
    private final double rotDir;

    public BallDrive(MecanumDriveBase base, NetworkTableEntry cam, double rotDir) {
        this.base = base;
        this.cam = cam;
        addRequirements(base);
        isFinished = false;
        this.rotDir = rotDir;
    }

    @Override
    public void execute() {
        String data = cam.getString("");
        System.out.println("Network value: " + data);
        if(cam.getString("").isEmpty()) {
            System.out.println("Not recieving values from Jetson Nano!!");
            return;
        }

        else if(data.equals("nothing")) {
            MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(0, 0, rotDir, RobotContainer.gyro.getAngle());
            base.setValues(speeds.rearRight, speeds.frontRight, speeds.rearLeft, speeds.frontLeft);
            return;
        }

        String[] ananthsDUmb = data.split("  ");
        String[] entry = null;

        for(String e : ananthsDUmb) {
            if(e.split(" ")[3].equals(RobotContainer.isBlue.getSelected() ? "blue" : "red")) {
                entry = e.split(" ");
                break;
            }
        }

        if(entry == null) return;

        double x = Double.parseDouble(entry[0]);

        double error = (360 - x);
        MecanumDrive.WheelSpeeds speeds;
        // 540 to 740
        if (Math.abs(error) > 400) {
            speeds = MecanumDrive.driveCartesianIK(0, error / 1700, error / 2000, 0);
        } else {
            speeds = MecanumDrive.driveCartesianIK(-0.65, error / 400, error / 3000, 0);
        }



        //if(x < 630) speeds = MecanumDrive.driveCartesianIK(0, 0, 0.333, RobotContainer.gyro.getAngle());
        //else if(x > 730) speeds = MecanumDrive.driveCartesianIK(0, 0, -0.333, RobotContainer.gyro.getAngle());
        //else speeds = MecanumDrive.driveCartesianIK(0.333, 0, (x < 680 ? 0.333 : -0.333), RobotContainer.gyro.getAngle());

        base.setValues(speeds.rearRight, speeds.frontRight, speeds.rearLeft, speeds.frontLeft);

        isFinished = Double.parseDouble(entry[1]) > lowestBallPos;
    }

    @Override
    public boolean isFinished() {return isFinished;}

    @Override
    public void end(boolean interrupted) {base.setValues(0, 0, 0, 0);}
}
