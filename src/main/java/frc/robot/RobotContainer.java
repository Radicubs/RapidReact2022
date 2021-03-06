// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.BallDrive;
import frc.robot.commands.MecanumDriveCommand;
import frc.robot.commands.auto.groups.*;
import frc.robot.commands.BallDown;

import frc.robot.commands.auto.LimelightAlign;
import frc.robot.commands.auto.groups.GrabAndShoot;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.I2C;

public class RobotContainer {
  public static final Gyro gyro = new Gyro();
  public static final ColorSensor elevatorColor = new ColorSensor(I2C.Port.kOnboard);
  public static final ColorSensor indexColor = new ColorSensor(I2C.Port.kMXP);

  public final NetworkTable table;
  private final JoystickButton gyroCallibrate = new JoystickButton(controller, Constants.L_BUMP);

  public final Elevator elevator = new Elevator();
  public final Shooter shooter = new Shooter();
  public final Index index = new Index();
  public final Intake intake = new Intake();
  public final Climber climber = new Climber();
  public final MecanumDriveBase driveBase = new MecanumDriveBase();
  public final Winch winch = new Winch();

  public static final UltrasonicSensor ultrasonic = new UltrasonicSensor();
  public static final PhotoelectricSensor photoelectric = new PhotoelectricSensor(0);

  private SendableChooser<Command> chooser = new SendableChooser<>();
  public static SendableChooser<Boolean> isBlue = new SendableChooser<>();

  static {
    isBlue.setDefaultOption("Blue", true);
    isBlue.addOption("Red", false);
    SmartDashboard.putData(isBlue);
    SmartDashboard.updateValues();
  }

  public static Joystick controller = new Joystick(Constants.JOYSTICK);
  public static final Joystick buttonBoard = new Joystick(Constants.BUTTON_BOARD);
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    table = NetworkTableInstance.getDefault().getTable("data");
    configureButtonBindings();
    chooser.setDefaultOption("Grab and Shoot", new GrabAndShoot(driveBase, intake, index, elevator, shooter, true, 0.333));
    //chooser.addOption("P1SA", new P1SA(driveBase, intake, index, elevator, shooter));
    chooser.addOption("P2SA", new P2SA(driveBase, intake, index, elevator, shooter));
    chooser.addOption("P3SA", new P3SA(driveBase, intake, index, elevator, shooter));
    chooser.addOption("P4SA", new P4SA(driveBase, intake, index, elevator, shooter));
    //chooser.addOption("P1/2 Dual", new DualBall(driveBase, intake, index, elevator, shooter, -0.333));
    //chooser.addOption("P3/4 Dual", new DualBall(driveBase, intake, index, elevator, shooter, 0.333));
    //chooser.addOption("P1SB", new DualBall(driveBase, intake, index, elevator, shooter));
    SmartDashboard.putData(chooser);
    SmartDashboard.updateValues();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(controller, Constants.A_BUTTON).toggleWhenPressed(new StartEndCommand(() -> {intake.on(); index.on();},
            () -> {intake.off(); index.off();}, intake, index));
    new JoystickButton(controller, Constants.X_BUTTON).toggleWhenPressed(new StartEndCommand(elevator::on, elevator::off, elevator));
    new JoystickButton(controller, Constants.Y_BUTTON).toggleWhenPressed(new StartEndCommand(shooter::on, shooter::off, shooter));
    new JoystickButton(controller, Constants.R_BUMP).toggleWhenPressed(new BallDown(elevator, shooter, index, intake));

    new JoystickButton(buttonBoard, Constants.TOP_BUTTON_ONE).whenHeld(new LimelightAlign(driveBase, false));
    new JoystickButton(buttonBoard, Constants.TOP_BUTTON_TWO).whenHeld(new BallDrive(driveBase,
            NetworkTableInstance.getDefault().getTable("data").getEntry("0"), 0.333));
    new JoystickButton(buttonBoard, Constants.TOP_BUTTON_THREE);//.whenPressed(new MecanumDriveCommand(driveBase));
    new JoystickButton(buttonBoard, Constants.TOP_BUTTON_FOUR);
    new JoystickButton(buttonBoard, Constants.BOTTOM_BUTTON_ONE);
    new JoystickButton(buttonBoard, Constants.BOTTOM_BUTTON_TWO);
    new JoystickButton(buttonBoard, Constants.BOTTOM_BUTTON_THREE);
    new JoystickButton(buttonBoard, Constants.BOTTOM_BUTTON_FOUR);


    gyroCallibrate.whileActiveOnce(new StartEndCommand(gyro::recal, () -> {}, gyro));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // return new LimelightAlign(driveBase);
     // return new Pickup(driveBase, intake, index, elevator, shooter);
     return chooser.getSelected();
  }
}
