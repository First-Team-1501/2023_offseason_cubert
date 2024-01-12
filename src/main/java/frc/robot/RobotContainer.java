// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Intake.Intake;
import frc.robot.Intake.Command.*;
import frc.robot.Shooter.Shooter;
import frc.robot.Shooter.Command.*;
import frc.robot.Climber.Climber;
import frc.robot.Climber.Command.*;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.swervedrive.auto.Autos;
import frc.robot.commands.swervedrive.auto.TestComplexAutoCommand;
import frc.robot.commands.swervedrive.drivebase.AbsoluteDrive;
import frc.robot.commands.swervedrive.drivebase.AbsoluteFieldDrive;
import frc.robot.commands.swervedrive.drivebase.TeleopDrive;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{
  private final Intake s_Intake;
  private final Shooter s_Shooter;
  private final Climber s_Climber;
  private PowerDistribution powerBoard;

  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                         "swerve/neo"));
  // CommandJoystick rotationController = new CommandJoystick(1);
  // Replace with CommandPS4Controller or CommandJoystick if needed
  //CommandJoystick driverController = new CommandJoystick(1);

  // CommandJoystick driverController   = new CommandJoystick(3);//(OperatorConstants.DRIVER_CONTROLLER_PORT);
  XboxController driverXbox = new XboxController(0);
  XboxController operatorXbox = new XboxController(1);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer(PowerDistribution pd)
  {

    s_Intake = new Intake();
    s_Climber = new Climber();
    s_Shooter = new Shooter();
    powerBoard = pd;

    // Configure the trigger bindings
    configureBindings();

    AbsoluteDrive closedAbsoluteDrive = new AbsoluteDrive(drivebase,
                                                    () -> MathUtil.applyDeadband(driverXbox.getLeftY(),OperatorConstants.LEFT_Y_DEADBAND),
                                                    () -> MathUtil.applyDeadband(driverXbox.getLeftX(),OperatorConstants.LEFT_X_DEADBAND),
                                                    () -> -driverXbox.getRawAxis(2),
                                                    () -> -driverXbox.getRawAxis(3), false);
    AbsoluteFieldDrive closedFieldAbsoluteDrive = new AbsoluteFieldDrive(drivebase,
                                                    () -> MathUtil.applyDeadband(driverXbox.getLeftY(),OperatorConstants.LEFT_Y_DEADBAND),
                                                    () -> MathUtil.applyDeadband(driverXbox.getLeftX(),OperatorConstants.LEFT_X_DEADBAND),
                                                    () -> driverXbox.getRawAxis(2), false);
    TeleopDrive simClosedFieldRel = new TeleopDrive(drivebase,
                                                    () -> MathUtil.applyDeadband(driverXbox.getLeftY(),OperatorConstants.LEFT_Y_DEADBAND),
                                                    () -> MathUtil.applyDeadband(driverXbox.getLeftX(),OperatorConstants.LEFT_X_DEADBAND),
                                                    () -> driverXbox.getRawAxis(2), () -> true, false, true);
    TeleopDrive closedFieldRel = new TeleopDrive(drivebase,
                                                    () -> MathUtil.applyDeadband(driverXbox.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
                                                    () -> MathUtil.applyDeadband(driverXbox.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
                                                    () -> driverXbox.getRawAxis(2), () -> true, false, true);

    //(This was the default) drivebase.setDefaultCommand(!RobotBase.isSimulation() ? closedAbsoluteDrive : closedFieldAbsoluteDrive);
    drivebase.setDefaultCommand(closedAbsoluteDrive);
    //(Don't use this one) drivebase.setDefaultCommand(closedFieldAbsoluteDrive);
    //(Don't use this one) drivebase.setDefaultCommand(simClosedFieldRel)
    ///(Can't get this one to work, do not try) drivebase.setDefaultCommand(closedFieldRel);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings()
  {

    new JoystickButton(driverXbox, 1).onTrue((new InstantCommand(drivebase::zeroGyro)));

    //Xbox controlls for arm and intake


    Trigger intakeButton = new Trigger(()-> operatorXbox.getRawButton(6));
        intakeButton.whileTrue(new IntakeCommand(s_Intake));

    Trigger outakeButton = new Trigger(()->operatorXbox.getRawButton(5));
        outakeButton.whileTrue(new OutakeCommand(s_Intake));

    Trigger climbButton = new Trigger(()-> operatorXbox.getRawButton(4));
        climbButton.whileTrue(new ClimbUpCommand(s_Climber));

    Trigger rapelButton = new Trigger(()->operatorXbox.getRawButton(1));
        rapelButton.whileTrue(new ClimbDownCommand(s_Climber));

    Trigger shootButton = new Trigger(()->operatorXbox.getRawButton(5));
        shootButton.whileTrue(new ShootCommand(s_Shooter));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand()
  {
    // An example command will be run in autonomous
    return Autos.exampleAuto(drivebase);
 
  }

  public void setDriveMode()
  {
    //drivebase.setDefaultCommand();
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }
}
