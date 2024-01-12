package frc.robot.Climber.Command;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Climber.Climber;

public class ClimbUpCommand extends CommandBase
{

    private Climber CLIMBER;

    public ClimbUpCommand(Climber climber)
    {
        CLIMBER = climber;
    }

    @Override
    public void initialize() 
    {
        
    }
    @Override
    public void execute()
    {

            CLIMBER.climbUp();
  
        
    }

    @Override
    public void end(boolean inturrupted)
    {
        CLIMBER.stopClimb();

    }



}

