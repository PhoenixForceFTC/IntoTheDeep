package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
@Autonomous(group="!CompOpModes")
public class SpecimenAutoRR extends AutoOpMode {

    //------------------------------------------------------------
    //--- Field Positions ---
    //------------------------------------------------------------

    //--- Scoring Side, align right side of the tile

    // robot is 15 inches by 11 and a half inches
    public static Position START = new Position(16.5,-67,90);
    public static Position INT1 = new Position(34,-60,90);
    public static Position INT2 = new Position(36,-12,90);
    public static Position BLOCK1 = new Position(46,-12,90);
    public static Position BLOCK1DEPOSIT = new Position(46,-60,90);
    public static Position BLOCK2 = new Position(57,-10,90);
    public static Position BLOCK2DEPOSIT = new Position(57,-60,90);
    public static Position BLOCK3 = new Position(62,-9,90);
    public static Position BLOCK3DEPOSIT = new Position(62 ,-60,90);



    
    @Override
    public void runOpMode() {
        setSpeed(Speed.FAST);
        setup(START);
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

