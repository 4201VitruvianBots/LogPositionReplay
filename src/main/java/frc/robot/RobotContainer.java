// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

import java.io.FileReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.opencsv.CSVReader;

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

  private Field2d field2d = new Field2d();

  public static final class TimePose2d {
    private double m_time;
    private Pose2d m_pose;

    public TimePose2d(double time, Pose2d pose) {
      m_time = time;
      m_pose = pose;
    }

    public double getTime() {
      return m_time;
    }

    public Pose2d getPose() {
      return m_pose;
    }
  }

  private final ArrayList<TimePose2d> robotPoses = new ArrayList<>();
  private Pose2d currentPose = new Pose2d();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    SmartDashboard.putData("Field2d",field2d);
    // Configure the button bindings
    configureButtonBindings();
      try {
        // Create an object of filereader
        // class with CSV file as a parameter.
        FileReader filereader = new FileReader("deploy/FRC_20220515_232043.csv");

        // create csvReader object passing
        // file reader as a parameter
        CSVReader csvReader = new CSVReader(filereader);
        String[] nextRecord;

        // we are going to read data line by line
        String timeString, name, value;
        String[] poseParts;
        double time, x, y, angle;
        while ((nextRecord = csvReader.readNext()) != null) {
          timeString = nextRecord[0];
          name = nextRecord[1];
          value = nextRecord[2];
          if (name.equals("/pose")) {
            poseParts = value.split(",");
            x = Double.parseDouble(poseParts[0]);
            y = Double.parseDouble(poseParts[1]);
            angle = Double.parseDouble(poseParts[2]);
            time = Double.parseDouble(timeString);
            robotPoses.add(new TimePose2d(time,new Pose2d(new Translation2d(x,y),new Rotation2d(angle))));
          }

        }

        csvReader.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
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
    Optional<TimePose2d> c = robotPoses.stream().filter(timePose2d -> timePose2d.getTime() < Timer.getMatchTime()).findFirst();
    c.ifPresent(timePose2d -> currentPose = timePose2d.getPose());
    field2d.setRobotPose(currentPose);
  }
}
