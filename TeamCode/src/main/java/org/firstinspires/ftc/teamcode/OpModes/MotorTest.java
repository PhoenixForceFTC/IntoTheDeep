package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp
public class MotorTest extends LinearOpMode {
    public DcMotorEx motor1;


    @Override
    public void runOpMode() {
        motor1 = hardwareMap.get(DcMotorEx.class, "MOTORTEST");

        telemetry.addData("motor1 : ", motor1.getPower());

        waitForStart();

        telemetry.addLine();
        telemetry.update();

        while (!isStopRequested()) {

            while (opModeIsActive()) {

                if(gamepad1.a) {
                    motor1.setPower(motor1.getPower()-0.05);
                    sleep(100);
                }
                if(gamepad1.b)
                {

                    motor1.setPower(motor1.getPower()+0.05);
                    sleep(100);
                }
                if(gamepad1.x)
                {

                    motor1.setPower(0.5);
                    sleep(100);
                }
                telemetry.addData("AMPS >", motor1.getCurrent(CurrentUnit.AMPS));
                telemetry.addData("Position >", motor1.getPower());
                telemetry.update();
                waitForStart();
            }
        }
    }
}