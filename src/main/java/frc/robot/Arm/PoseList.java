
package frc.robot.Arm;

import frc.robot.Arm.command.ArmPose;
import frc.robot.Arm.command.BasicPose;

public class PoseList
{
    

    
    private ArmPose[] poseList;

    public PoseList()
    {
        poseList = new ArmPose[NamedPose.values().length];
       

        // General
        poseList[NamedPose.Home.ordinal()] = 
            new BasicPose(0);

        poseList[NamedPose.FloorPick.ordinal()] = 
            new BasicPose(-95);
            
        poseList[NamedPose.Shoot.ordinal()] = 
            new BasicPose(-45);
          
        
    }


    public ArmPose getArmPose(NamedPose p)
    {
        return poseList[p.ordinal()];
    }
   

}
