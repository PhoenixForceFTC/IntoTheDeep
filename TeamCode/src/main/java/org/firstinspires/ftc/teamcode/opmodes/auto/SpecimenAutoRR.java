package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.core.tools.Arm;
import org.firstinspires.ftc.teamcode.core.tools.MultiAxisClawAssembly;
import org.firstinspires.ftc.teamcode.core.tools.Tooling;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoOpMode3deadwheel;

import java.util.concurrent.TimeUnit;

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
    public static Position SCORE2 = new Position(0, -32,90);
    public static Position SCORE3 = new Position(0, -48,90);
    public static Position INT1 = new Position(38,-48,90);
    public static Position INT2 = new Position(38,-12,90);
    public static Position BLOCK1 = new Position(46,-12,90);
    public static Position BLOCK1DEPOSIT = new Position(46,-60,90);
    public static Position BLOCK2 = new Position(51,-10,90);
    public static Position BLOCK2DEPOSIT = new Position(51,-60,90);
    public static Position GRAB1 = new Position( 46,-36,270);
    public static Position GRAB2 = new Position(36,-40,270);
    public static Position SCORE4 = new Position(0, -34,270);


    private void sleepTools2(int ms) {
        Timing.Timer timer = new Timing.Timer(ms, TimeUnit.MILLISECONDS);
        timer.start();
        while (!timer.done() && !isStopRequested()) {
            arm.update();
            claw.update();
        }
    }

    @Override
    public void runOpMode() {

        setSpeed(Speed.FAST);

        Arm.autoRan=false;
        setup(START);
        goTo(START2);
        goTo(SCORE1);
        sleepTools2(1000);
        Arm.customAngle = Arm.Position.DUMPING.angle;
        claw.setPosition(MultiAxisClawAssembly.Position.SUBMERSIBLE_PICKUP_HORIZONTAL);
        sleepTools2(700);


        Arm.extensionPosition = 1010;
        sleepTools2(2000);

        goTo(SCORE2);
        sleepTools2(100);
        Arm.customAngle = 90;
        Arm.extensionPosition = 0;
        sleepTools2(700);
        Arm.customAngle = 67;
        sleepTools2(700);
        claw.off();
        sleepTools2(900);
        goTo(SCORE3);
        claw.off();
        goTo(INT1);
        goTo(INT2);
        claw.setPosition(MultiAxisClawAssembly.Position.WALL_SPECIMEN_PICKUP);
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

        claw.setPosition(MultiAxisClawAssembly.Position.WALL_SPECIMEN_PICKUP);
        arm.setTargetAngle(Arm.Position.HOME);
        Arm.extensionPosition = 0;
        sleepTools2(500);
        goTo(SCORE4);
        arm.setTargetAngle(Arm.Position.DUMPING);
        Arm.extensionPosition = Tooling.TARGET_SAMPLE_HEIGHT;
        multiAxisClawAssembly.setPosition(MultiAxisClawAssembly.Position.DUMP_AND_WALL_REMOVAL_AND_HOME);


    }

}

