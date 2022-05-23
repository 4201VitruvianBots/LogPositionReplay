// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.datalog.DataLogEntry;
import edu.wpi.first.util.datalog.DataLogIterator;
import edu.wpi.first.util.datalog.DataLogReader;
import edu.wpi.first.util.datalog.DataLogRecord;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.opencsv.CSVReader;
import org.apache.commons.lang3.ObjectUtils;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  private final Field2d field2d = new Field2d();
  private Pose2d currentPose = new Pose2d();
  private double currentTime = 0;
  private boolean playing = true;

  private final TreeMap<Double,Pose2d> robotPoses = new TreeMap<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    SmartDashboard.putData("Field2d",field2d);
    SmartDashboard.putNumber("Time", 0);
    // Configure the button bindings
    configureButtonBindings();
      try {
        DataLogReader reader = new DataLogReader("src/main/deploy/FRC_20220523_024716.csv");

        String timeString, name, value;
        String[] poseParts;
        double time, x, y, angle;

        for (DataLogRecord record : reader) {
//          if (record.name.equals("/pose")) {
//            poseParts = value.split(",");
//            x = Double.parseDouble(poseParts[0]);
//            y = Double.parseDouble(poseParts[1]);
//            angle = Double.parseDouble(poseParts[2]);
//            time = Double.parseDouble(timeString);
//            robotPoses.put(time, new Pose2d(new Translation2d(x, y), new Rotation2d(angle)));
//          }
        }

//        csvReader.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

      PrintLog.printLog(new String[] {"src/main/deploy/FRC_20220523_024716.csv"});
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }

  public void simulationPeriodic() {
//    try {
//      field2d.setRobotPose(
//              robotPoses.floorEntry(
//                      Timer.getFPGATimestamp()
//              ).getValue());
//    } catch(NullPointerException ignored) {}
//
//    if (playing) {
////      currentTime += Timer.
//    }
  }
}
