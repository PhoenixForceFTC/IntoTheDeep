package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Subsystems.Lights;
@TeleOp
public class LightsTest extends LinearOpMode {

    private Lights lights;
    private int counter = 0;
    private final RevBlinkinLedDriver.BlinkinPattern pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
    private final RevBlinkinLedDriver.BlinkinPattern pattern2 = RevBlinkinLedDriver.BlinkinPattern.SINELON_FOREST_PALETTE;

    @Override
    public void runOpMode() {
        waitForStart();
        lights = new Lights(this);
        while (!isStopRequested()) {
            while (opModeIsActive()) {
                counter++;
                lights.setPattern(pattern);
                telemetry.addLine("red");
                sleep(1000);
                lights.setPattern(pattern2);
                telemetry.addLine("blue");
                sleep(1000);
                telemetry.addLine(String.valueOf(counter));
                telemetry.update();
                //when transfering use shot white because it shows transfering
            }
        }
    }

}