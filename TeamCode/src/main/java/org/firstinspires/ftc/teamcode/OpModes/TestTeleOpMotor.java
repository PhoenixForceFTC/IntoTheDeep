package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * FTC WIRES TeleOp Example
 *
 */
@TeleOp(name = "test teleop motor", group = "00-Teleop")
public class TestTeleOpMotor extends LinearOpMode {
    private OpMode opMode;
    private boolean isYPressed;
    //private NewLift2 newLift2;
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (!isStopRequested()) {
            while (opModeIsActive()) {
                if (gamepad1.y) {
                    if (!isYPressed) {

                    }
                    isYPressed = true;
                } else {
                    if (isYPressed) {

                    }
                    telemetry.update();
                    isYPressed = false;
                }
            }
        }
    }
}