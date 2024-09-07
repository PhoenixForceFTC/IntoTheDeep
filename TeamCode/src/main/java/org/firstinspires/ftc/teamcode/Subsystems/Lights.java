package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Lights {
    RevBlinkinLedDriver blinkin;

    public Lights(LinearOpMode opMode) {
        blinkin = opMode.hardwareMap.get(RevBlinkinLedDriver.class, "lights");
    }

    public void setPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        blinkin.setPattern(pattern);
    }
}
