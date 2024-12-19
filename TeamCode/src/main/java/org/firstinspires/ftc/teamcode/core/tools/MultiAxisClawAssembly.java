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
    public enum Position {
        SUBMERSIBLE_PICKUP_HORIZONTAL(0, 90),
        SUBMERSIBLE_PICKUP_VERTICAL(0, 0),
        WALL_SPECIMEN_PICKUP(90, 90),
        DUMP_AND_WALL_REMOVAL_AND_HOME(180, 270);

        public final double pitch;
        public final double roll;

        Position(double pitch, double roll) {
            this.pitch = pitch;
            this.roll = roll;
        }

    }
    public static double SERVO_TICKS_PER_DEGREE = 1 / 355D;
    public static double PITCH_SERVO_OFFSET = 0.79, ROLL_SERVO_OFFSET = 0.21;
    private final Servo pitch, roll;


    private double pitchAngle = 0, rollAngle = 0;
    private Position position;
    // add setpoint variables
    public MultiAxisClawAssembly(HardwareMap hardwareMap) {
        super(hardwareMap, .8, .2, "CLAW", false);
        pitch = hardwareMap.get(Servo.class, "PITCH");
        roll = hardwareMap.get(Servo.class, "ROLL");
        this.position = Position.DUMP_AND_WALL_REMOVAL_AND_HOME;
    }
    @Override
    public void update() {
        super.update();
        if (pitch != null && roll != null) {
            setServos();
        }
    }

    private void setServos() {
        pitch.setPosition(
                MathUtils.clamp(
                        position.pitch * -SERVO_TICKS_PER_DEGREE + PITCH_SERVO_OFFSET,
                        0,
                        1
                )
        );
        roll.setPosition(
                MathUtils.clamp(
                        position.roll * SERVO_TICKS_PER_DEGREE + ROLL_SERVO_OFFSET,
                        0,
                        1
                )
        );
    }

    public double getPitchAngle() {
        return pitchAngle;
    }

    void setPitchAngleDegrees(double degrees) {
        this.pitchAngle = degrees;
    }

    public double getRollAngle() {
        return rollAngle;
    }

    void setRollAngleDegrees(double degrees) {
        this.rollAngle = degrees;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return this.position;
    }
}
