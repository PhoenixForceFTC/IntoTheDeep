package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.core.tools.Arm;

@Config
@Autonomous(group="!CompOpModes")
public class Red_Left extends AutoOpMode {

    //------------------------------------------------------------
    //--- Field Positions ---
    //------------------------------------------------------------

    //--- Scoring Side, align right side of the tile
    public static Position START = new Position(-36, -63, 180);
    public static Position INT = new Position(-50, -60, 180);
    public static Position SCORE = new Position(-50, -60, 225);
    public static Position INT2 = new Position(-50, -12, 225);
    public static Position ASCENT = new Position(-28, -12, 225);

    
    @Override
    public void runOpMode() {
        setup(START);
        claw.on();
        claw.update();
        sleepTools(500);
        goTo(INT);
        turn(45);
        sleepTools(500);
        arm.setExtensionPosition(Arm.Lift.Position.MAX);
        arm.setTargetPosition(Arm.Position.DUMPING);
        sleepTools(2000);
        claw.off();
        claw.update();
        sleepTools(500);
        claw.on();
        claw.update();
        sleepTools(100);
        arm.setTargetPosition(Arm.Position.HOME);
        sleepTools(500);
        arm.setExtensionPosition(Arm.Lift.Position.ZERO);
        sleepTools(1500);
        goTo(INT2);
        goTo(ASCENT);
        Arm.customAngle = 140;
        arm.setTargetPosition(Arm.Position.CUSTOM);
        sleepTools(3000);
        Arm.lastAutoAngle = arm.getCurrentPosition();
    }

}

