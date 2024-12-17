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

    // robot is 15 inches by 11 and a half inches
    public static Position START = new Position(-66.25, 36, 90);
    public static Position INT = new Position(-60, 50, 90);
    public static Position SCORE = new Position(-60, 50, 135);
    public static Position INT2 = new Position(-57, 50, 270);

    public static Position INT3 = new Position(-57, 60, 270);


    public static Position INT5 = new Position(-12, 50, 135);
    public static Position ASCENT = new Position(-12, 28, 135);



    
    @Override
    public void runOpMode() {
        setup(START);
        claw.on();
        claw.update();
        sleepTools(500);
        goTo(INT);
        turn(45);
        sleepTools(500);

        arm.setTargetAngle(Arm.Position.DUMPING);
        arm.setExtensionPosition(Arm.Lift.Position.MAX);
        sleepTools(2000);
        claw.off();
        claw.update();
        sleepTools(500);
        claw.on();
        claw.update();
        sleepTools(100);
        arm.setTargetAngle(Arm.Position.HOME);
        sleepTools(500);
        arm.setExtensionPosition(Arm.Lift.Position.ZERO);
        sleepTools(1500);

        turn(135);
        goTo(INT2);
        turn(-180);
        goTo(INT);
        turn(45);
        sleepTools(500);

        arm.setTargetAngle(Arm.Position.DUMPING);
        arm.setExtensionPosition(Arm.Lift.Position.MAX);
        sleepTools(2000);
        claw.off();
        claw.update();
        sleepTools(500);
        claw.on();
        claw.update();
        sleepTools(100);
        arm.setTargetAngle(Arm.Position.HOME);
        sleepTools(500);
        arm.setExtensionPosition(Arm.Lift.Position.ZERO);
        sleepTools(1500);

        turn(135);
        goTo(INT3);
        turn(-180);
        goTo(INT);
        turn(45);
        sleepTools(500);

        arm.setTargetAngle(Arm.Position.DUMPING);
        arm.setExtensionPosition(Arm.Lift.Position.MAX);
        sleepTools(2000);
        claw.off();
        claw.update();
        sleepTools(500);
        claw.on();
        claw.update();
        sleepTools(100);
        arm.setTargetAngle(Arm.Position.HOME);
        sleepTools(500);
        arm.setExtensionPosition(Arm.Lift.Position.ZERO);
        sleepTools(1500);





        goTo(INT5);
        goTo(ASCENT);
        Arm.customAngle = 140;
        arm.setTargetAngle(Arm.Position.CUSTOM);
        sleepTools(3000);
        Arm.lastAutoAngle = arm.getCurrentAngle();
    }

}

