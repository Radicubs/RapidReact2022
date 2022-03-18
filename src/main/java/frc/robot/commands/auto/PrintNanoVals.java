package frc.robot.commands.auto;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class PrintNanoVals extends CommandBase {

    private final NetworkTableEntry entry;

    public PrintNanoVals() {entry = NetworkTableInstance.getDefault().getTable("data").getEntry("0");
    }

    @Override
    public void execute() {
        System.out.println(entry.getString(""));
    }
}
