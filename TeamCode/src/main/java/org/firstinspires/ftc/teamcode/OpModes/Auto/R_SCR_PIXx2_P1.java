package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Subsystems.Vision;

@Config
@Autonomous(group="!CompOpModes")
public class R_SCR_PIXx2_P1 extends AutoOpMode {
    private Vision vision;

    //--- adb connect 192.168.43.1:5555

    private Vision.IDENTIFIED_SPIKE_MARK_LOCATION spikeLocation;

    //------------------------------------------------------------
    //--- Field Positions ---
    //------------------------------------------------------------

    //--- Scoring Side, align right side of the tile
    public static Position START = new Position(14, -63, 90);

    //--- Deliver
    public static Position LEFT_SPIKE = new Position(7, -35, 180);
    public static Position MIDDLE_SPIKE = new Position(22, -28, 180);
    public static Position RIGHT_SPIKE = new Position(27, -36, 180);
    //--- Parking
    public static Position PARK_ARENA_CENTER = new Position(44, -15, 180);
    public static Position PARK_ARENA_CENTER_PUSH = new Position(60, -15, 180);
    public static Position PARK_BACKDROP_CENTER = new Position(44, -37, 180);
    public static Position PARK_BACKDROP_CENTER_PUSH = new Position(60, -37, 180);
    public static Position PARK_ARENA_WALL = new Position(44, -63, 180);
    public static Position PARK_ARENA_WALL_PUSH = new Position(60, -63, 180);
    
    //--- Drop positions (LEFT)
    public static Position LEFT_BACKDROP = new Position(47, -30, 180);
    public static Position LEFT_BACKDROP_CLOSE = new Position(50, -30, 180);
    
    //--- Drop positions (MIDDLE)
    public static Position MIDDLE_BACKDROP = new Position(47, -37, 180);
    public static Position MIDDLE_BACKDROP_CLOSE = new Position(50, -37, 180);
    
    //--- Drop positions (RIGHT)
    public static Position RIGHT_BACKDROP = new Position(47, -43, 180);
    public static Position RIGHT_BACKDROP_CLOSE = new Position(50, -43, 180);


    
    @Override
    public void runOpMode() {

        //--- Initialize vision
        vision = new Vision(this, Vision.START_POSITION.RED_SCORING);
        vision.initTfod();

        //--- Wait for Start
        while (!isStopRequested() && !opModeIsActive()) {
            //--- Run Vuforia Tensor Flow and keep watching for the identifier in the Signal Cone
            vision.runTfodTensorFlow();
            telemetry.addData("Vision identified Parking Location", vision.getPixelLocation());
            telemetry.update();
        }

        //--- Read the final position of the spike
        //spikeLocation = Vision.IDENTIFIED_SPIKE_MARK_LOCATION.RIGHT; //--- Set default value to left
        spikeLocation = vision.getPixelLocation();
        //vision.Stop();

        //--- Initialize
        setup(START);

        //--- Initialize
        swinch.setClawClosed(true); //--- close the claw

        //--- Drive based on spike position
        switch (spikeLocation) {
            case LEFT:
                //LeftSpike();
                SpikeMovementPaths(LEFT_SPIKE, LEFT_BACKDROP, LEFT_BACKDROP_CLOSE
                );
                break;
            case MIDDLE:
                SpikeMovementPaths(MIDDLE_SPIKE, MIDDLE_BACKDROP, MIDDLE_BACKDROP_CLOSE
                );
                //CenterSpike();
                break;
            case RIGHT:
                SpikeMovementPaths(RIGHT_SPIKE, RIGHT_BACKDROP, RIGHT_BACKDROP_CLOSE
                );
                //RightSpike();
                break;
        }

        //--- Finalize
        swinch.setClawClosed(true); //--- close the claw

        //--- Park
     //   goTo(PARK_ARENA_CENTER);
        goTo(PARK_ARENA_WALL);
        goTo(PARK_ARENA_WALL_PUSH);
        //goTo(PARK_BACKDROP_CENTER);

        sleep(5000);
    }

    private void SpikeMovementPaths(Position SpikePos,
                           Position BackdropPos, Position BackdropClosePos)
    {
        //--- Drive to spike and eject pixel
        goTo(SpikePos);
        sleep(300);
        intake.returnPixel();
        sleep(200);
        intake.stop();
        sleep(300);

        //--- Drive to the backdrop
        drop.goToPosition(3); //--- Up
        goTo(BackdropPos);
        setSpeed(Speed.VERY_SLOW); //--- Slow down before moving back a little
        goTo(BackdropClosePos);

        //--- Deliver pixel
        topGate.setGateOpen();
        sleep(1000);
        topGate.setGateStopped();
        goTo(BackdropPos);
        setSpeed(Speed.FAST);
        drop.goToPosition(0);
        intake.stop();

    }

}

