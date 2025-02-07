package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.teamcode.core.tools.Arm;
import org.firstinspires.ftc.teamcode.core.tools.MultiAxisClawAssembly;
import org.firstinspires.ftc.teamcode.core.tools.Tooling;

@Config
@Autonomous(group="!CompOpModes")
public class SpecimenAutoRR extends AutoOpMode3deadwheel {

    //------------------------------------------------------------
    //--- Field Positions ---
    //------------------------------------------------------------

    //--- Scoring Side, align right side of the tile

    // robot is 15 inches by 11 and a half inches
    public static Position START = new Position(7.5,-67,90);
    public static Position START2 = new Position(7.5,-63,90);
    public static Position SCORE1 = new Position(0, -63,90);
    public static Position SCORE2 = new Position(0, -26,90);
    public static Position INT1 = new Position(34,-60,90);
    public static Position INT2 = new Position(36,-12,90);
    public static Position BLOCK1 = new Position(46,-12,90);
    public static Position BLOCK1DEPOSIT = new Position(46,-60,90);
    public static Position BLOCK2 = new Position(57,-10,90);
    public static Position BLOCK2DEPOSIT = new Position(57,-60,90);
    public static Position BLOCK3 = new Position(61,-9,90);
    public static Position BLOCK3DEPOSIT = new Position(61 ,-60,90);



    
    @Override
    public void runOpMode() {

        setSpeed(Speed.FAST);

        Arm.autoRan=false;
        setup(START);
        Arm.customAngle = 0;
        Arm.extensionPosition = 0;
        goTo(START2);
        goTo(SCORE1);
        Arm.customAngle = 0;
        Arm.extensionPosition = 0;
        Arm.customAngle = Arm.Position.DUMPING.angle;
        Arm.extensionPosition = 1010;
        arm.update();
        claw.setPosition(MultiAxisClawAssembly.Position.SUBMERSIBLE_PICKUP_HORIZONTAL);
        Arm.customAngle = 67;
        arm.update();
        goTo(SCORE2);
        Arm.extensionPosition = 0;
        sleepTools(2000);
        goTo(SCORE1);
        claw.off();
        goTo(INT1);
        goTo(INT2);
        goTo(BLOCK1);
        goTo(BLOCK1DEPOSIT);
        goTo(BLOCK1);
        goTo(BLOCK2);
        goTo(BLOCK2DEPOSIT);
        goTo(BLOCK2);
        setSpeed(Speed.SLOW);
        goTo(BLOCK3);
        setSpeed(Speed.FAST);
        goTo(BLOCK3DEPOSIT);
    }

}

