package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Drop;
import org.firstinspires.ftc.teamcode.Subsystems.TopGate;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeServo;
import org.firstinspires.ftc.teamcode.Subsystems.BottomGate;
import org.firstinspires.ftc.teamcode.Subsystems.SlidesServo;

/**
 * FTC WIRES TeleOp Example
 *
 */
@TeleOp(name = "test teleop", group = "00-Teleop")
public class TestTeleOp extends LinearOpMode {
    private TopGate topGate;
    private OpMode opMode;
    private IntakeServo axonServo;
    private BottomGate bottomGate;
    private Drop drop;
    //private SlidesServo slidesServo;
    private boolean isYPressed;
    private boolean isXPressed;
    private boolean isBPressed;


    //private NewLift2 newLift2;
    public void runOpMode() throws InterruptedException {
        drop = new Drop(this);
        bottomGate = new BottomGate(this, drop);
        axonServo = new IntakeServo(this);
       // slidesServo = new SlidesServo(this);
        axonServo.stop();
        topGate = new TopGate(this, drop);

        waitForStart();
        while (!isStopRequested()) {
            while (opModeIsActive()) {
                drop.controlLift2();
                drop.move(gamepad2.left_stick_y);
                topGate.controlGate();
                bottomGate.controlClaw();
                if (gamepad1.y) {
                    if (!isYPressed) {


                    }
                    axonServo.forward();
                    isYPressed = true;
                } else {
                    if (isYPressed) {


                    }
                    axonServo.stop();
                    telemetry.update();
                    isYPressed = false;
                }
                if (gamepad1.b) {
                    if (!isBPressed) {

                    }
                    isBPressed = true;
                } else {
                    if (isBPressed) {
                    }
                    telemetry.update();
                    isXPressed = false;
                }
                if (gamepad1.x) {
                    if (!isXPressed) {
                       // slidesServo.forward();

                    }
                    isXPressed = true;
                } else {
                    if (isXPressed) {
                        //slidesServo.stop();

                    }
                    telemetry.update();
                    isXPressed = false;
                }
            }
        }
    }
}