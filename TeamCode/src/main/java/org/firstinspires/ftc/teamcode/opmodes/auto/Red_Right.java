package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.core.tools.Arm;

@Config
@Autonomous(group="!CompOpModes")
public class Red_Right extends AutoOpMode {

    //------------------------------------------------------------
    //--- Field Positions ---
    //------------------------------------------------------------

    //--- Scoring Side, align right side of the tile
    public static Position START = new Position(-66.25, -36, 0);
    public static Position SCORE = new Position(-36, -36, 0);

    public static Position INT = new Position(-36, -36, 0);
    public static Position INT2 = new Position(-36, 60, 0);
    public static Position ASCENT = new Position(12, 36, 0);



    
    @Override
    public void runOpMode() {
        setup(START);
        //claw.on();
        //claw.update();
        goTo(SCORE);
        arm.setTargetAngle(Arm.Position.DUMPING);
        arm.setExtensionPosition(Arm.Lift.Position.MAX);
        sleepTools(2000);
        //claw.off();
        //claw.update();
        sleepTools(500);
        arm.setExtensionPosition(Arm.Lift.Position.ZERO);
        sleepTools(1500);
        arm.setTargetAngle(Arm.Position.HOME);
        goTo(INT);
        goTo(INT2);
        goTo(ASCENT);
    }

}

