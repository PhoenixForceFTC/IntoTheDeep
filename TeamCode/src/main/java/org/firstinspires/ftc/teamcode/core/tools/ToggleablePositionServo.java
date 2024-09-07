package org.firstinspires.ftc.teamcode.core.tools;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.core.Subsystem;

public class ToggleablePositionServo implements Subsystem {
    private final ServoEx servo;
    private final double position0, position1;
    private boolean lastToggle, toggle;

    public ToggleablePositionServo(@NonNull HardwareMap hardwareMap, double pos0, double pos1,
                                   String servoName, boolean startingToggle) {
        this.position0 = pos0;
        this.position1 = pos1;
        this.toggle = startingToggle;
        this.servo = hardwareMap.get(ServoEx.class, servoName);
        lastToggle = !toggle; // to ensure update in update method
        update();
    }

    public ToggleablePositionServo(HardwareMap hardwareMap, double pos0, double pos1, String servoName) {
        this(hardwareMap, pos0, pos1, servoName, false);
    }

    @Override
    public void update() {
        if (toggle != lastToggle) {
            servo.setPosition(toggle ? position1 : position0);
            lastToggle = toggle;
        }
    }

    public void toggle() {
        toggle = !toggle;
    }

    public void on() {
        toggle = true;
    }

    public void off() {
        toggle = false;
    }
}
