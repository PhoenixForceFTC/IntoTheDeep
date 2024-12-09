package org.firstinspires.ftc.teamcode.core.tools;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class MultiAxisClawAssembly extends ToggleablePositionServo {
    private static final double DEGREES_TO_SERVO_MULT = 0;
    private static final double PITCH_SERVO_OFFSET = 0, ROLL_SERVO_OFFSET = 0;

    final Servo pitch, roll;
    // add setpoint variables
    public MultiAxisClawAssembly(HardwareMap hardwareMap) {
        super(hardwareMap, .8, .2, "claw", false);
        pitch = hardwareMap.get(Servo.class, "pitch");
        roll = hardwareMap.get(Servo.class, "roll");
    }
    @Override
    public void update() {
        super.update();
        // update pitch/roll to setpoints
    }
    // set rotation values for each axis servo (apply offsets and multiplier)
    // get rotation values of servo
}
