package org.firstinspires.ftc.teamcode.OpModes.util;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp
public class ServoTest extends LinearOpMode {
    public Servo servo1;


    @Override
    public void runOpMode() {
        servo1 = hardwareMap.get(Servo.class, "PITCH");

        telemetry.addData("Servo1 : ", servo1.getPosition());

        waitForStart();

        telemetry.addLine();
        telemetry.update();

        while (!isStopRequested()) {

            while (opModeIsActive()) {

                if(gamepad1.a) {
                    servo1.setPosition(servo1.getPosition()-0.01);
                    sleep(100);
                }
                if(gamepad1.b)
                {

                    servo1.setPosition(servo1.getPosition()+0.01);
                    sleep(100);
                }
                if(gamepad1.x)
                {

                    servo1.setPosition(0.5);
                    sleep(100);
                }

                telemetry.addData("Position >", servo1.getPosition());
                telemetry.update();
                waitForStart();
            }
        }
    }
}