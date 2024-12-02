package org.firstinspires.ftc.teamcode.core.tools;

import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotorSimple motor;
    private double speed;
    public Intake(HardwareMap hardwareMap, String motorOrServoName, double speed) {
        hardwareMap.get(DcMotorSimple.class, motorOrServoName);
        this.speed = speed;
    }

    public Intake(HardwareMap hardwareMap, String motorOrServoName) {
        this(hardwareMap, motorOrServoName, 1);
    }

    public void extake() {
        motor.setPower(-speed);
    }

    public void intake() {
        motor.setPower(speed);
    }

    public void stop() {
        motor.setPower(0);
    }

}
