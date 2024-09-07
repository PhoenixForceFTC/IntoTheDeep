package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vision.PropDetector;

public abstract class AbstractAutonomous extends OpMode {
    public PropDetector detector;
    public enum Case {
        LEFT, CENTER, RIGHT, UNDEFINED, INITIALIZED
    }
    private Case detectCase = Case.UNDEFINED;
    public static final int minDetected = 10;
    protected Case runCase = Case.UNDEFINED;

    private int detected = 0;
    public Side side;
    public enum Side {
        RED, BLUE
    }

    public void waitOpMode() {
        if (detectCase == detector.getCase()) {
            detected++;
        } else {
            detectCase = detector.getCase();
            detected = 0;
        }
        if (detected >= minDetected) {
            runCase = detectCase;
        }
        telemetry.addData("Case Detected", detectCase);
        telemetry.addData("Case to Run", 5);
    }

    public void startOpMode() {
        detector.close();
    }
}
