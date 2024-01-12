package frc.robot.Intake;


import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase
{

    private CANSparkMax intakeMaster;
    private CANSparkMax intakeSlave;
;

    public Intake()
    {

        //Initialize master motor
        intakeMaster = new CANSparkMax(Constants.Intake.intakeMasterMoter, MotorType.kBrushless);
        intakeMaster.setIdleMode(IntakeConfig.idleMode);

        //Configure PID
        SparkMaxPIDController controller = intakeMaster.getPIDController();
        controller.setP(IntakeConfig.pValue, 0);
        controller.setI(IntakeConfig.iValue, 0);
        controller.setD(IntakeConfig.dValue, 0);
        controller.setFF(IntakeConfig.ffValue, 0);
        controller.setOutputRange(-IntakeConfig.maxPower, IntakeConfig.maxPower);

        //Initialize slave motor
        intakeSlave = new CANSparkMax(Constants.Intake.intakeSlaveMotor, MotorType.kBrushless);
        intakeSlave.follow(intakeMaster, true);

        CANSparkMax[] motors = {intakeMaster, intakeSlave};

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
        intakeMaster.setClosedLoopRampRate(IntakeConfig.rampRate);
        intakeMaster.setOpenLoopRampRate(IntakeConfig.rampRate);


        for(CANSparkMax motor : motors)
        {
            //Burn flash - *IMPORTANT*
            motor.burnFlash();
        }
        
    }

    public void startIntake()
    {
        intakeMaster.set(IntakeConfig.intakeSpeed);
    }


    public void stopIntake()
    {
        intakeMaster.set(0);
    }

    
    public void startOutake()
    {
        intakeMaster.set(IntakeConfig.outakeSpeed);
    }
}