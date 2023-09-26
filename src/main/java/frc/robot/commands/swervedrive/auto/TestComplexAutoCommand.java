package frc.robot.commands.swervedrive.auto;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.Auton;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Arm.Intake;
import frc.robot.Arm.command.ShootCommand;
import frc.robot.Constants.Auton;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.util.HashMap;
import java.util.List;

public class TestComplexAutoCommand extends SequentialCommandGroup {
    public TestComplexAutoCommand(SwerveSubsystem swerve, Intake intake) {
        // add the subsystems to the requirements of the command
        // the scheduler will prevent two commands that require the same subsystem from being scheduled simultaneously
        addRequirements(swerve, intake);

        // move 4 meters, then spin 180 degrees while moving another 0.8 meters.
        PathPlannerTrajectory pathTrajectory = PathPlanner.generatePath(
            new PathConstraints(2, 2),
            new PathPoint(new Translation2d(0, 0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(0)),
            new PathPoint(new Translation2d(3.0, 0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(0)),
            new PathPoint(new Translation2d(4.5, 0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(157))
        );

        // reset odometry
        swerve.resetOdometry(pathTrajectory.getInitialHolonomicPose());

        // add the commands, in order
        addCommands(
            // shoot cube, then wait 1.5 seconds
            new ShootCommand(intake).raceWith(new WaitCommand(1.5)),

            // command to follow the trajectory
            new PPSwerveControllerCommand(
                pathTrajectory,
                swerve::getPose,
                Auton.xAutoPID.createPIDController(),
                Auton.yAutoPID.createPIDController(),
                Auton.angleAutoPID.createPIDController(),
                swerve::setChassisSpeeds,
                swerve
            ),

            new WaitCommand(3),

            // zero the gyro to correct orientation
            new InstantCommand(swerve::zeroGyro)
        );
    }
}
