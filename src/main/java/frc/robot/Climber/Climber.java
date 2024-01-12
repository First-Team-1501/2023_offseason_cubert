package frc.robot.Climber;
import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase
{
   
    //motors
    private CANSparkMax climbMotor;

    public Climber()
    {
        //Initialize Intake Motor
        climbMotor = new CANSparkMax(Constants.Climber.climberMotor, MotorType.kBrushless);
        climbMotor.setIdleMode(ClimberConfig.idleMode);
        climbMotor.setSmartCurrentLimit(20,10);

        //Burn Flash - *IMPORTANT*
        climbMotor.burnFlash();
    }

    public void climbUp()
    {
        climbMotor.set(ClimberConfig.climbSpeed);
    }


    public void stopClimb()
    {
        climbMotor.set(0);
    }

    
    public void climbDown()
    {
        climbMotor.set(ClimberConfig.repelSpeed);
    }
}
