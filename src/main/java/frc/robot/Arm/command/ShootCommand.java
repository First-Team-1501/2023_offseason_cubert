package frc.robot.Arm.command;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Arm.Intake;

public class ShootCommand extends CommandBase
{

    private Intake INTAKE;

    public  ShootCommand(Intake intake)
    {
        INTAKE = intake;
    }

    @Override
    public void initialize() 
    {
        
    }
    @Override
    public void execute()
    {

            INTAKE.shoot();
  
        
    }

    @Override
    public void end(boolean inturrupted)
    {
        INTAKE.stopIntake();

    }



}