package frc.robot.Arm;
import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase
{
   
    //motors
    private CANSparkMax intakeMotor;

    public Intake()
    {
        //Initialize Intake Motor
        intakeMotor = new CANSparkMax(Constants.Shooter.shooterMotorID, MotorType.kBrushless);
        intakeMotor.setIdleMode(IntakeConfig.idleMode);
        intakeMotor.setSmartCurrentLimit(20,10);

        //Burn Flash - *IMPORTANT*
        intakeMotor.burnFlash();
    }

    public void startIntake()
    {
        intakeMotor.set(IntakeConfig.intakeSpeed);
    }


    public void stopIntake()
    {
        intakeMotor.set(0);
    }

    
    public void shoot()
    {
        intakeMotor.set(IntakeConfig.shootSpeed);
    }
}
