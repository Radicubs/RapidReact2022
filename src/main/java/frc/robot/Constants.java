// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final int kSlotIdx = 0;

    public static final int kPIDLoopIdx = 0;

    public static final int kTimeoutMs = 30;

    public static final double kP = 0.1;
    public static final double kI = 0.001;
    public static final double kD = 5;
    public static final double kF = 1023.0/20660.0;

    public static final int rightMotorFront = 0;
    public static final int rightMotorBack = 1;
    public static final int leftMotorFront = 2;
    public static final int leftMotorBack = 3;

    public static final int JOYSTICK = 0;
    public static final int LEFT_X_AXIS = 1;
    public static final int LEFT_Y_AXIS = 1;
    public static final int RIGHT_X_AXIS = 4;
    public static final int RIGHT_Y_AXIS = 5;
}
