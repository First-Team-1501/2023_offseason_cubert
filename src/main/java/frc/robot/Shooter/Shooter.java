package frc.robot.Shooter;


import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase
{

    private CANSparkMax shooterMaster;
    private CANSparkMax shooterSlave;
;

    public Shooter()
    {

        //Initialize master motor
        shooterMaster = new CANSparkMax(Constants.Shooter.shooterMasterMotor, MotorType.kBrushless);
        shooterMaster.setIdleMode(ShooterConfig.idleMode);

        //Configure PID
        SparkMaxPIDController controller = shooterMaster.getPIDController();
        controller.setP(ShooterConfig.pValue, 0);
        controller.setI(ShooterConfig.iValue, 0);
        controller.setD(ShooterConfig.dValue, 0);
        controller.setFF(ShooterConfig.ffValue, 0);
        controller.setOutputRange(-ShooterConfig.maxPower, ShooterConfig.maxPower);

        //Initialize slave motor
        shooterSlave = new CANSparkMax(Constants.Shooter.shooterSlaveMotor, MotorType.kBrushless);
        shooterSlave.follow(shooterMaster, true);

        CANSparkMax[] motors = {shooterMaster, shooterSlave};

        initializeMotors(motors);

    }

    @Override
    public void periodic()
    {
        
    }

    private void initializeMotors(CANSparkMax[] motors)
    {
        //Do this for each motor in motors[] array
        for(CANSparkMax motor: motors)
        {
            //Set current Limit
            motor.setSmartCurrentLimit(40,20 );

            //Enable soft limits
            motor.enableSoftLimit(SoftLimitDirection.kForward, true);
            motor.enableSoftLimit(SoftLimitDirection.kReverse, true);
            
            //Set motors to position mode
            motor.getEncoder().setPosition(0);
            
        }


        //Set ramp rates
        shooterMaster.setClosedLoopRampRate(ShooterConfig.rampRate);
        shooterMaster.setOpenLoopRampRate(ShooterConfig.rampRate);


        for(CANSparkMax motor : motors)
        {
            //Burn flash - *IMPORTANT*
            motor.burnFlash();
        }
        
    }

    public void startShooter()
    {
        shooterMaster.set(ShooterConfig.intakeSpeed);
    }


    public void stopShooter()
    {
        shooterMaster.set(0);
    }

    
}