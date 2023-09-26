package frc.robot.Arm;


import frc.robot.Constants;
import frc.robot.Arm.command.ArmPose;
import frc.robot.Arm.command.BasicPose;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase
{

    private CANSparkMax armMaster;
    private CANSparkMax armSlave;

    private ArmPose currentPose;
    private PoseList poses;

    public Arm(NamedPose pose)
    {

        poses = new PoseList();
        currentPose = poses.getArmPose(pose);

        //Initialize master motor
        armMaster = new CANSparkMax(Constants.Arm.armMasterMoter, MotorType.kBrushless);
        armMaster.getEncoder().setPositionConversionFactor(360.0/(ArmConfig.armMotorConfig.gearRatio));
        armMaster.setIdleMode(ArmConfig.armMotorConfig.idleMode);

        //Set soft limits
        armMaster.setSoftLimit(SoftLimitDirection.kReverse, ArmConfig.armMotorConfig.lowerLimit);
        armMaster.setSoftLimit(SoftLimitDirection.kForward, ArmConfig.armMotorConfig.upperLimit);

        //Configure PID
        SparkMaxPIDController controller = armMaster.getPIDController();
        controller.setP(ArmConfig.armMotorConfig.pValue, 0);
        controller.setI(ArmConfig.armMotorConfig.iValue, 0);
        controller.setD(ArmConfig.armMotorConfig.dValue, 0);
        controller.setFF(ArmConfig.armMotorConfig.ffValue, 0);
        controller.setOutputRange(-ArmConfig.armMotorConfig.maxPower, ArmConfig.armMotorConfig.maxPower);

        //Initialize slave motor
        armSlave = new CANSparkMax(Constants.Arm.armSlaveMotor, MotorType.kBrushless);
        armSlave.follow(armMaster, true);

        CANSparkMax[] motors = {armMaster, armSlave};

        initializeMotors(motors);

    }

    public PoseList getPoseList()
    {
        return poses;
    }

    public ArmPose getCurrentPose()
    {
        return new BasicPose(armMaster.getEncoder().getPosition());
    }

    public ArmPose getSetPose()
    {
        return currentPose;
    }

    @Override
    public void periodic()
    {
        // despite the lack of roundness, this is very sensual. Yes more!;
        SmartDashboard.putNumber("Arm", armMaster.getEncoder().getPosition());
        SmartDashboard.putNumber("Arm Current", armMaster.getOutputCurrent());
        SmartDashboard.putNumber("Arm error", armMaster.getEncoder().getPosition()- currentPose.getPos());

        SmartDashboard.putBoolean( "At Pose?", isAtPose(currentPose));

        adoptPose(currentPose);
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
        armMaster.setClosedLoopRampRate(ArmConfig.armMotorConfig.rampRate);
        armMaster.setOpenLoopRampRate(ArmConfig.armMotorConfig.rampRate);

        armSlave.enableSoftLimit(SoftLimitDirection.kForward, false);
        armSlave.enableSoftLimit(SoftLimitDirection.kReverse, false);

        for(CANSparkMax motor : motors)
        {
            //Burn flash - *IMPORTANT*
            motor.burnFlash();
        }
        
    }

    private void setArmPosition(Double armRef)
    {
        if(armRef == null)
        {
            armRef = armMaster.getEncoder().getPosition();
        }

        armMaster.getPIDController().setReference(armRef, ControlType.kPosition);
         
    }

    public void adoptPose(ArmPose pose)
    {
        if(currentPose == null )
        {
            throw new NullPointerException("Arm pose may not be null. You ****ed up");
        }

        
        currentPose = pose;
        
        setArmPosition(pose.getPos());     
        
    }

    public boolean isAtPose(ArmPose pose)
    {
        double tolerance = 2;
        return isWithin(pose.getPos(), armMaster.getEncoder().getPosition(), tolerance);
    }

    private boolean isWithin(double a, double b, double within)
    {
        return Math.abs(a-b)<within;
    }

}