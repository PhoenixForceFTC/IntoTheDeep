package org.firstinspires.ftc.teamcode.core.tools;

import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ArmFeedforward;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.util.MathUtils;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.Subsystem;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

// ADDITIONAL MOTOR
// VARIES WITH SINE TIMES GRAVITY CONSTANT FOR ITS PID
// TRIGGERS MOVE EACH DIRECTION



@Config
public class Arm implements Subsystem {
    public enum Position {
        HOME(180),
        PENETRATION(-120), //changed from -15 due to altered starting pos
        GRABBING(-30),
        DUMPING(60),
        MANUAL(-1),
        CUSTOM(-1);

        public final Rotation2d angle;

        Position(double degrees) {
            this.angle = Rotation2d.fromDegrees(degrees);
        }
    }

    private final ArrayList<MotorEx> motors = new ArrayList<>(1); // only one for now, just want to make scalable
    private final DoubleSupplier manual;

    private ArmFeedforward armFeedforwardController;
    private final PIDController feedbackController;

    public static Position target = Position.HOME;
    public static double customAngle = 0D;

    public static double kP = 0.03, kI = 0, kD = 0.0005, kG = 0.1;

    private final Telemetry telemetry;

    public Arm(HardwareMap hardwareMap, Telemetry telemetry, @Nullable DoubleSupplier manualPowerController) {
        MotorEx motor1 = new MotorEx(hardwareMap, "rightArm", Motor.GoBILDA.RPM_43);
        motor1.setInverted(true);
        motor1.stopAndResetEncoder();

        motors.add(motor1);

        feedbackController = new PIDController(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(0, kG, 0, 0);

        this.manual = manualPowerController;
        this.telemetry = telemetry;
    }

    @Override
    public void update() {
        feedbackController.setPID(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(0, kG, 0, 0);

        double targetAngle = target.angle.getDegrees();

        switch (target) { // OVERRIDES
            case MANUAL:
                setPower(MathUtils.clamp(manual.getAsDouble() + (kG * (Math.cos(Math.toRadians(getCurrentPosition())))), -1, 1));
                return;
            case CUSTOM:
                targetAngle = customAngle;
        }

        double currentDegrees = getCurrentPosition();
        double currentRadians = Math.toRadians(currentDegrees);
        if (currentDegrees < -170) currentDegrees = 179;
        final double output =
                currentDegrees < 170 || targetAngle < 170 ?
                        feedbackController.calculate(currentDegrees, targetAngle) +
                                armFeedforwardController.calculate(
                                        currentRadians, 0
                                )
                : 0;
        telemetry.addData("target arm angle", targetAngle);
        telemetry.addData("current arm angle", currentDegrees);
        setPower(MathUtils.clamp(output, -1, 1));
    }

    public double getCurrentPosition() {
        return motors.get(0).getCurrentPosition() / motors.get(0).getCPR() * 360 + 180;
    }

    public void setPower(double power) {
        motors.forEach(m -> m.set(power));
    }

    public void setTargetPosition(Position position) {
        Arm.target = position;
    }
}
