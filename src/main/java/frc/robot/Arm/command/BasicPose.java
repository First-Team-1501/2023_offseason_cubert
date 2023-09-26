package frc.robot.Arm.command;

public class BasicPose implements ArmPose
{
    private Double armDegrees;

   
    public BasicPose(Double armDeg)
    {
        armDegrees = armDeg;
    }
    
    public BasicPose(double armDeg)
    {
        this(Double.valueOf(armDeg));
    }

    public Double getPos()
    {
        return armDegrees;

    }

    public String toString()
    {
        return "ArmPose: { Angle: "+ getPos() + " }";
    }
    

}
