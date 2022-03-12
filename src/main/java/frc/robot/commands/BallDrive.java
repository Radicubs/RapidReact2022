package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBase;

public class BallDrive extends CommandBase {

    private final DriveBase base;
    private final NetworkTableEntry cam;

    public BallDrive(DriveBase base, NetworkTableEntry cam) {
        this.base = base;
        this.cam = cam;
        addRequirements(base);
    }

    @Override
    public void execute() {
        System.out.println("Network value: " + cam.getString(""));
        if(cam.getString("").isEmpty()) {
            System.out.println("Not recieving values from Jetson Nano!!");
            return;
        }

        else if(cam.getString("").equals("nothing")) {
            MecanumDrive.WheelSpeeds speeds = MecanumDrive.driveCartesianIK(0, 0, .3333, RobotContainer.gyro.getAngle());
            base.setValues(speeds.rearRight, speeds.frontRight, speeds.rearLeft, speeds.frontLeft);
            return;
        }

        String[] entry = cam.getString("").split(" ");
        double x = Double.parseDouble(entry[0]);

        double error = (640.0 - x);
        MecanumDrive.WheelSpeeds speeds;
        // 540 to 740
        if (Math.abs(error) > 400) {
            speeds = MecanumDrive.driveCartesianIK(0, error / 2000, error / 2000, 0);
        } else {
            speeds = MecanumDrive.driveCartesianIK(-0.5, error / 500, error / 3000, 0);
        }



        //if(x < 630) speeds = MecanumDrive.driveCartesianIK(0, 0, 0.333, RobotContainer.gyro.getAngle());
        //else if(x > 730) speeds = MecanumDrive.driveCartesianIK(0, 0, -0.333, RobotContainer.gyro.getAngle());
        //else speeds = MecanumDrive.driveCartesianIK(0.333, 0, (x < 680 ? 0.333 : -0.333), RobotContainer.gyro.getAngle());

        base.setValues(speeds.rearRight, speeds.frontRight, speeds.rearLeft, speeds.frontLeft);
    }
}
