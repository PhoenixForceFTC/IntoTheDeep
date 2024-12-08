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
    public static Position START = new Position(-63, 36, 180);
    public static Position INT = new Position(-36, 36, 180);
    public static Position INT2 = new Position(-36, -60, 180);
    public static Position SCORE = new Position(-36, -60, 180);
    public static Position ASCENT = new Position(12, -36, 180);



    
    @Override
    public void runOpMode() {
        setup(START);
        claw.on();
        claw.update();
        goTo(INT);
        goTo(INT2);
        goTo(SCORE);
        arm.setExtensionPosition(Arm.Lift.Position.MAX);
        arm.setTargetAngle(Arm.Position.DUMPING);
        sleepTools(2000);
        claw.update();
        sleepTools(500);
        claw.update();
        sleepTools(500);
        arm.setExtensionPosition(Arm.Lift.Position.ZERO);
        sleepTools(1500);
        arm.setTargetAngle(Arm.Position.HOME);
        goTo(ASCENT);
    }

}

