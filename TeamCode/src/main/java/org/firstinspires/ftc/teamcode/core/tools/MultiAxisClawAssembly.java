package org.firstinspires.ftc.teamcode.core.tools;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.util.MathUtils;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.frozenmilk.util.units.angle.Angle;
import dev.frozenmilk.util.units.angle.AngleUnits;
import dev.frozenmilk.util.units.angle.Angles;

@Config
public class MultiAxisClawAssembly extends ToggleablePositionServo {
    private static double SERVO_TICKS_PER_DEGREE = 1 / 355D;
    private static double PITCH_SERVO_OFFSET = 0, ROLL_SERVO_OFFSET = 0;
    private final Servo pitch, roll;

    private Angle pitchAngle = Angles.wrappedDeg(0), rollAngle = Angles.wrappedDeg(0);
    // add setpoint variables
    public MultiAxisClawAssembly(HardwareMap hardwareMap) {
        super(hardwareMap, .8, .2, "claw", false);
        pitch = hardwareMap.get(Servo.class, "pitch");
        roll = hardwareMap.get(Servo.class, "roll");
    }
    @Override
    public void update() {
        super.update();
        setServos();
    }

    private void setServos() {
        pitch.setPosition(
                MathUtils.clamp(
                        pitchAngle.get(AngleUnits.DEGREE) * SERVO_TICKS_PER_DEGREE + PITCH_SERVO_OFFSET,
                        0,
                        1
                )
        );
        roll.setPosition(
                MathUtils.clamp(
                        rollAngle.get(AngleUnits.DEGREE) * SERVO_TICKS_PER_DEGREE + ROLL_SERVO_OFFSET,
                        0,
                        1
                )
        );
    }

    public Angle getPitchAngle() {
        return pitchAngle;
    }

    public void setPitchAngleDegrees(double degrees) {
        this.pitchAngle = Angles.wrappedDeg(degrees);
    }

    public Angle getRollAngle() {
        return rollAngle;
    }

    public void setRollAngleDegrees(double degrees) {
        this.rollAngle = Angles.wrappedDeg(degrees);
    }
}
