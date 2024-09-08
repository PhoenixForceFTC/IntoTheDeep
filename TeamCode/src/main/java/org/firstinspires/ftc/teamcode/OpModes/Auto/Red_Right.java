package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.core.tools.Arm;
import org.firstinspires.ftc.teamcode.core.tools.ToggleablePositionServo;

@Config
@Autonomous(group="!CompOpModes")
public class Red_Right extends org.firstinspires.ftc.teamcode.OpModes.Auto.AutoOpMode {

    //------------------------------------------------------------
    //--- Field Positions ---
    //------------------------------------------------------------

    //--- Scoring Side, align right side of the tile
    public static Position START = new Position(12, -60, 90);
    public static Position INT = new Position(60, -36, 270);
    public static Position SCORE = new Position(60, -36, 270);


    
    @Override
    public void runOpMode() {
        while (!isStopRequested() && !opModeIsActive()) {

        }
        setup(START);
        goTo(INT);
        splineTo(SCORE);
        arm.setTargetPosition(Arm.Position.DUMPING);
        sleep(2000);
        claw.update();
        sleep(500);
        claw.update();
        sleep(500);
        arm.setTargetPosition(Arm.Position.HOME);
    }

}

