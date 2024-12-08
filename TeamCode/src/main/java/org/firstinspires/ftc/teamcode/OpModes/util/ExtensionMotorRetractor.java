package org.firstinspires.ftc.teamcode.opmodes.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Autonomous
public class ExtensionMotorRetractor extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        final DcMotorEx motor1 = hardwareMap.get(DcMotorEx.class, "extension1");
        final DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, "extension2");
        motor2.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        motor1.setPower(-1);
        motor2.setPower(-1);
        while (!isStopRequested() && motor1.getCurrent(CurrentUnit.AMPS) < 6) {}
    }
}
