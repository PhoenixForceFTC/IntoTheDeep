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
    public static Position INT = new Position(36,-36,90);
    public static Position BLOCK1 = new Position(48,-12,0);
    public static Position BLOCK1DEPOSIT = new Position(48,-60,0);
    public static Position BLOCK2 = new Position(57,-10,90);
    public static Position BLOCK2DEPOSIT = new Position(57,-60,90);
    public static Position BLOCK3 = new Position(61,-9,0);
    public static Position BLOCK3DEPOSIT = new Position(61,-60,0);



    
    @Override
    public void runOpMode() {
        setup(START);
        goTo(INT);
        goTo(BLOCK1);
        goTo(BLOCK1DEPOSIT);
        goTo(BLOCK2);
        goTo(BLOCK2DEPOSIT);
        goTo(BLOCK3);
        goTo(BLOCK3DEPOSIT);


    }

}

