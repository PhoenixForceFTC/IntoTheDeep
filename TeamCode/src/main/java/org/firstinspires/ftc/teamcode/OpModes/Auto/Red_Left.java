package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.core.tools.Arm;
import org.firstinspires.ftc.teamcode.core.tools.MultipleMotorLift;

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
    public static Position ASCENT = new Position(-28, -12, 225);

    
    @Override
    public void runOpMode() {
        while (!isStopRequested() && !opModeIsActive()) {

        }
        setup(START);
        claw.on();
        claw.update();
        sleepTools(500);
        goTo(INT);
        splineTo(SCORE);
        sleepTools(500);
        lift.setTargetPosition(MultipleMotorLift.Position.TOP_POSITION_CONTROL);
        arm.setTargetPosition(Arm.Position.DUMPING);
        sleepTools(2000);
        claw.off();
        claw.update();
        sleepTools(500);
        claw.on();
        claw.update();
        sleepTools(500);
        lift.setTargetPosition(MultipleMotorLift.Position.BOTTOM_POSITION_CONTROL);
        lift.update();
        sleepTools(1500);
        arm.setTargetPosition(Arm.Position.HOME);
        goTo(ASCENT);
        Arm.customAngle = 140;
        arm.setTargetPosition(Arm.Position.CUSTOM);
        sleepTools(3000);
        Arm.lastAutoAngle = arm.getCurrentPosition();
    }

}

