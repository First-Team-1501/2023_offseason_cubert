package frc.robot.Shooter;

import com.revrobotics.CANSparkMax.IdleMode;

public final class ShooterConfig 
{
 

        public static final double rampRate = .05;


        public static final IdleMode idleMode = IdleMode.kBrake;

        //PID
        public static final double pValue = 0.02;
        public static final double iValue = 0;
        public static final double dValue = 0.0;
        public static final double ffValue = 0;
        
        public static final double maxPower = 1;
    
        public static final double intakeSpeed = 1;
        public static final double outakeSpeed = -1;

}
