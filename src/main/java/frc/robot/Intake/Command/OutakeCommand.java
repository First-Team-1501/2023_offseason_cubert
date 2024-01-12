package frc.robot.Intake.Command;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Intake.Intake;

public class OutakeCommand extends CommandBase
{

    private Intake INTAKE;

    public OutakeCommand(Intake intake)
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

            INTAKE.startOutake();
  
        
    }

    @Override
    public void end(boolean inturrupted)
    {
        INTAKE.stopIntake();

    }



}

