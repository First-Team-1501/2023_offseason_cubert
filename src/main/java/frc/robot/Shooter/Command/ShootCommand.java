package frc.robot.Shooter.Command;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Shooter.Shooter;

public class ShootCommand extends CommandBase
{

    private Shooter SHOOTER;

    public ShootCommand(Shooter shooter)
    {
        SHOOTER = shooter;
    }

    @Override
    public void initialize() 
    {
        
    }
    @Override
    public void execute()
    {

            SHOOTER.startShooter();
  
        
    }

    @Override
    public void end(boolean inturrupted)
    {
        SHOOTER.stopShooter();

    }



}

