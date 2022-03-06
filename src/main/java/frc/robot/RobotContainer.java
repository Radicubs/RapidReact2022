// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.RotateToBall;
import frc.robot.commands.TurnToAngle;
import frc.robot.subsystems.DifferentialDriveBase;
import frc.robot.subsystems.Gyro;

import java.util.List;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  //private final DriveBase driveBase = new DriveBase();
  private final DifferentialDriveBase driveBase = new DifferentialDriveBase();

  public static final Gyro gyro = new Gyro();
  public final NetworkTable table;
  private final JoystickButton ballButton = new JoystickButton(controller, Constants.A_BUTTON);
  private final JoystickButton gyroCallibrate = new JoystickButton(controller, Constants.L_BUMP);

  public static Joystick controller = new Joystick(Constants.JOYSTICK);
  private static final JoystickButton button = new JoystickButton(controller, Constants.A_BUTTON);
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    table = NetworkTableInstance.getDefault().getTable("data");
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    button.toggleWhenPressed(new RotateToBall(driveBase, table.getEntry("opencv")));
    ballButton.whenHeld(new TurnToAngle(new Translation2d(1,1), driveBase.getPose(), driveBase));
    gyroCallibrate.toggleWhenPressed(new StartEndCommand(gyro::recal, () -> System.out.print(""), gyro));//lamda for requireNonNull
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {

    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(1, 1);
    config.setKinematics(Constants.kDriveKinematics);

    Trajectory t = TrajectoryGenerator.generateTrajectory(new Pose2d(), List.of(new Translation2d(1, 0), new Translation2d(1, 1)),
            new Pose2d(2, 0, new Rotation2d(0)), config);

    RamseteCommand ramseteCommand = new RamseteCommand(t,
            driveBase::getPose,
            new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
            driveBase.getFeedforward(),
            Constants.kDriveKinematics,
            driveBase::getWheelSpeeds,
            driveBase.getLeftPID(),
            driveBase.getRightPID(),
            driveBase::tankDriveVolts,
            driveBase);

    driveBase.resetOdometry(t.getInitialPose());
    return ramseteCommand.andThen(() -> driveBase.tankDriveVolts(0, 0));
  }
}
