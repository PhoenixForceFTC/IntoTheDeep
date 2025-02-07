package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.teamcode.core.tools.Arm;
import org.firstinspires.ftc.teamcode.core.tools.MultiAxisClawAssembly;
import org.firstinspires.ftc.teamcode.core.tools.Tooling;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode3deadwheel;

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
    public static Position SCORE2 = new Position(0, -40,90);
    public static Position SCORE3 = new Position(0, -48,90);
    public static Position INT1 = new Position(34,-48,90);
    public static Position INT2 = new Position(36,-12,90);
    public static Position BLOCK1 = new Position(46,-12,90);
    public static Position BLOCK1DEPOSIT = new Position(46,-60,90);
    public static Position BLOCK2 = new Position(57,-10,90);
    public static Position BLOCK2DEPOSIT = new Position(57,-60,90);
    public static Position GRAB1 = new Position( 46,-36,270);
    public static Position GRAB2 = new Position(36,-48,270);
    public static Position SCORE4 = new Position(0, -34,270);




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
        Arm.extensionPosition = 1300;
        sleepTools(1000);
        Arm.customAngle = 70;
        sleepTools(1000);
        claw.setPosition(MultiAxisClawAssembly.Position.SUBMERSIBLE_PICKUP_HORIZONTAL);
        Arm.customAngle = 67;
        sleepTools(1000);
        goTo(SCORE2);
        Arm.extensionPosition = 0;
        sleepTools(1000);
        claw.off();
        sleepTools(1000);
        goTo(SCORE3);
        claw.off();
        goTo(INT1);
        goTo(INT2);
        claw.setPosition(MultiAxisClawAssembly.Position.WALL_SPECIMEN_PICKUP);
        arm.update();
        goTo(BLOCK1);
        goTo(BLOCK1DEPOSIT);
        Action traj2 = drive.actionBuilder(drive.localizer.getPose())

                .splineToConstantHeading(BLOCK1.toVector2d(), BLOCK1.toRotation2d(), new TranslationalVelConstraint(500)/*, accelConstraint*/)//new splines wont be implemented through autoOpMode
                .splineToConstantHeading(BLOCK2.toVector2d(), BLOCK2.toRotation2d(), new TranslationalVelConstraint(500)/*, accelConstraint*/)
                .build();

        Actions.runBlocking(traj2);
        goTo(BLOCK2DEPOSIT);
        Action traj3 = drive.actionBuilder(drive.localizer.getPose())

                .splineTo(GRAB1.toVector2d(), GRAB1.toRotation2d(), new TranslationalVelConstraint(500)/*, accelConstraint*/)//new splines wont be implemented through autoOpMode
                .splineToConstantHeading(GRAB2.toVector2d(), GRAB2.toRotation2d(), new TranslationalVelConstraint(500)/*, accelConstraint*/)
                .build();

        Actions.runBlocking(traj3);
        arm.setTargetAngle(Arm.Position.HOME);
        claw.setPosition(MultiAxisClawAssembly.Position.WALL_SPECIMEN_PICKUP);
        goTo(SCORE4);


    }

}

