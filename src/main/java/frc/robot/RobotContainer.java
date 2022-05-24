// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.datalog.DataLogReader;
import edu.wpi.first.util.datalog.DataLogRecord;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.TreeMap;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...

    private final Field2d field2d = new Field2d();
    private double currentTime = 0;
    private double endTime;
    private double lastLoopTime = 0;
    private boolean playing = true;

    private final TreeMap<Double, Pose2d> robotPoses = new TreeMap<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        SmartDashboard.putData("Field2d", field2d);
        SmartDashboard.putNumber("Time", 0);
        SmartDashboard.putData("\u25b6", new InstantCommand().andThen(this::play));
        SmartDashboard.putData("\u2358", new InstantCommand().andThen(this::pause));
        // Configure the button bindings
        configureButtonBindings();
        int poseEntryId = 0;
        try {
            DataLogReader reader = new DataLogReader("src/main/deploy/FRC_20220523_024716.wpilog");
            String[] poseParts;
            double time;
            double x, y, angle;

            for (DataLogRecord record : reader) {
                if (record.isStart()) {
                    if (record.getStartData().name.equals("/pose")) {
                        poseEntryId = record.getStartData().entry;
                    }
                } else if (record.isFinish()) {
                    if (record.getFinishEntry() == poseEntryId) {
                        endTime = record.getTimestamp() / 1000000.0;
                    }
                } else if (record.getEntry() == poseEntryId) {
                    poseParts = record.getString().split(",");
                    x = Double.parseDouble(poseParts[0]);
                    y = Double.parseDouble(poseParts[1]);
                    angle = Double.parseDouble(poseParts[2]);
                    time = record.getTimestamp() / 1000000.0;
                    robotPoses.put(time, new Pose2d(new Translation2d(x, y), new Rotation2d(angle)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SmartDashboard.putNumber("Max", endTime);
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return null;
    }

    public void simulationPeriodic() {
        currentTime = SmartDashboard.getNumber("Time", currentTime);
        try {
            field2d.setRobotPose(
                    robotPoses.floorEntry(
                            Timer.getFPGATimestamp()
                    ).getValue());
            System.out.println(robotPoses.floorEntry(currentTime).getValue());
        } catch (NullPointerException ignored) {
        }
        if (playing) {
            currentTime += Timer.getFPGATimestamp() - lastLoopTime;
            SmartDashboard.putNumber("Time", currentTime);
            lastLoopTime = Timer.getFPGATimestamp();
        }
    }

    public void play() {
        lastLoopTime = Timer.getFPGATimestamp();
        playing = true;
    }

    public void pause() {
        playing = false;
    }
}
