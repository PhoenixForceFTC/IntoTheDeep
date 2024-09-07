package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.ButtonToggle;

public class Claw {
    private OpMode opMode;

    private Servo grip;
    //public ClawSensor sensor;

    private boolean canAutoClose = false;
    private ButtonToggle clawClosed;

    public Claw(OpMode opMode) {
        this.opMode = opMode;

        grip = opMode.hardwareMap.get(Servo.class, "CLAW");
        //grip.setDirection(Servo.Direction.REVERSE);
        //sensor = new ClawSensor(opMode.hardwareMap);

        clawClosed = new ButtonToggle();
    }

    public void controlClaw() {
        clawClosed.update(opMode.gamepad2.b);

        /*if (sensor.conePresent()) {
            if (canAutoClose) {
                clawClosed.setActive(true);
                canAutoClose = false;
            }
        } else {
            canAutoClose = true;
        }*/

        setClawClosed(clawClosed.isActive());

        //opMode.telemetry.addData("Claw sensor distance", sensor.getDistance());
        opMode.telemetry.addData("Claw open", !isClosed());
        opMode.telemetry.addData("Can auto close", canAutoClose);
        //opMode.telemetry.addData("Is cone detected", sensor.conePresent());

    }

    public boolean isClosed() {
        return clawClosed.isActive();
    }

    public void setClawClosed(boolean closed) {
        if (closed) {
            grip.setPosition(0.6);
        } else {
            grip.setPosition(1);
        }
    }
}
