package org.firstinspires.ftc.teamcode.opmodes.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Objects;
import java.util.Optional;

@Config
@TeleOp
public class ServoConfigurator extends LinearOpMode {
    public static String servoName = "";
    public static double servoPosition = 0D;

    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()) {
            if (!servoName.isEmpty()) {
                Optional.ofNullable(hardwareMap.tryGet(Servo.class, servoName)).ifPresent(s -> s.setPosition(servoPosition));
            }
        }
    }
}
