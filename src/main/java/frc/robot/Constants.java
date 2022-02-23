// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final int kSlotIdx = 0;

    public static final int kPIDLoopIdx = 0;

    public static final int kTimeoutMs = 30;

    public static final double kP = 0.1;
    public static final double kI = 0.001;
    public static final double kD = 5;
    public static final double kF = 1023.0 / 20660.0;

    public static final double kGearRatio = 10.71;
    public static final double kWheelDiameterMeters = 0.1524;
    public static final double kEncoderCPR = 2048;
    public static final double kEncoderDistancePerPulse = (kWheelDiameterMeters * Math.PI) / ((double) kEncoderCPR * kGearRatio);

    public static final double ksVolts = 0.55329;
    public static final double kvVoltSecondsPerMeter = 1.1386;
    
    public static final double kaVoltSecondsSquaredPerMeter = 0.074261;
    public static final double kTrackWidthMeters = 0.58;
    public static final double kMaxSpeedMetersPerSecond = 1;
    public static final double kMaxAccelerationMetersPerSecondSquared = 0.5;

    public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidthMeters);

    public static final double kPDriveVel = 1.2109;

    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;

    public static final int rightMotorFront = 0;
    public static final int rightMotorBack = 1;
    public static final int leftMotorFront = 2;
    public static final int leftMotorBack = 3;

    public static final int JOYSTICK = 0;
    public static final int LEFT_X_AXIS = 0;
    public static final int LEFT_Y_AXIS = 1;
    public static final int RIGHT_X_AXIS = 4;
    public static final int RIGHT_Y_AXIS = 5;
    public static final int A_BUTTON = 1;
    public static final int L_BUMP = 5;

    public static final int RIGHT_FALCON_FRONT = 1;
    public static final int RIGHT_FALCON_BACK = 2;
    public static final int LEFT_FALCON_FRONT = 3;
    public static final int LEFT_FALCON_BACK = 0;

    public static final int LEFT_TRIGGER = 2;

    public static double kTurnP = 0.027;
    public static double kTurnI = 0.0000;
    public static double kTurnD = 0.003;
    public static double kTurnToleranceDeg = 1;
    public static double kTurnRateToleranceDegPerS = 1;
}
