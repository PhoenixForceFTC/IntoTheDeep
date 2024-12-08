package org.firstinspires.ftc.teamcode.core.tools;

import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private enum State {
        FORWARD,
        BACKWARD,
        STOPPED
    }
    private DcMotorSimple motor;
    private double speed;
    private State state = State.STOPPED;
    public Intake(HardwareMap hardwareMap, String motorOrServoName, double speed) {
        motor = hardwareMap.get(DcMotorSimple.class, motorOrServoName);
        this.speed = speed;
    }

    public Intake(HardwareMap hardwareMap, String motorOrServoName) {
        this(hardwareMap, motorOrServoName, 1);
    }

    public void extake() {
        if (state == State.BACKWARD) {
            motor.setPower(0);
            state = State.STOPPED;
            return;
        }
        motor.setPower(-speed);
        state = State.BACKWARD;
    }

    public void intake() {
        if (state == State.FORWARD) {
            motor.setPower(0);
            state = State.STOPPED;
            return;
        }
        motor.setPower(speed);
        state = State.FORWARD;
    }

    public void stop() {
        motor.setPower(0);
        state = State.STOPPED;
    }

}
