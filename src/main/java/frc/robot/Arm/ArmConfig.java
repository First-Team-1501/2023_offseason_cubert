package frc.robot.Arm;

import com.revrobotics.CANSparkMax.IdleMode;

public final class ArmConfig 
{
 

    public static final class armMotorConfig
    {

        public static final double rampRate = .05;

        public static final int gearRatio = 188;

        public static final IdleMode idleMode = IdleMode.kBrake;

        public static final float upperLimit = 0;
        public static final float lowerLimit = -95;

        //PID
        public static final double pValue = 0.02;
        public static final double iValue = 0;
        public static final double dValue = 0.0;
        public static final double ffValue = 0;
        
        public static final double maxPower = 0.9;
    }

}
