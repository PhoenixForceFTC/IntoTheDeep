package org.firstinspires.ftc.teamcode.core.tools;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.util.MathUtils;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.Subsystem;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import static org.firstinspires.ftc.teamcode.util.Utilities.*;

import androidx.annotation.Nullable;

@Config
public class MultipleMotorLift implements Subsystem {
    public enum Position {
        BOTTOM_POSITION_CONTROL(0),
        TOP_POSITION_CONTROL(1800),
        MANUAL(-1),
        CUSTOM(-1);

        public final int height;

        Position(int ticks) {
            this.height = ticks;
        }
    } 

    private final ArrayList<MotorEx> motors;
    private final MotorEx left, right;
    private final PIDFController feedbackControllerLeft, feedbackControllerRight;
    private final DoubleSupplier manual;

    public static Position target = Position.BOTTOM_POSITION_CONTROL;
    public static int customHeight = 0;

    public static double liftTickSpeed = 30;
    private final Telemetry telemetry;

    private int lastTargetTicks = 0;
    public static double kP = 0.02, kI = 0.007, kD = 0.0005, kF = 0.002;
    public MultipleMotorLift(HardwareMap hardwareMap, Telemetry telemetry, @Nullable DoubleSupplier manualPowerController) {
        MultipleMotorLift.target = Position.BOTTOM_POSITION_CONTROL;

        left = new MotorEx(hardwareMap, "leftLift", Motor.GoBILDA.RPM_435);
        left.stopAndResetEncoder();

        right = new MotorEx(hardwareMap, "rightLift", Motor.GoBILDA.RPM_435);
        right.setInverted(true);
        right.stopAndResetEncoder();

        motors = listFromParams(left, right);

        feedbackControllerLeft = new PIDFController(kP, kI, kD, kF);
        feedbackControllerRight = new PIDFController(kP, kI, kD, kF);

        this.manual = manualPowerController;
        this.telemetry = telemetry;
    }

    @Override
    public void update() {
        feedbackControllerLeft.setPIDF(kP, kI, kD, kF);
        feedbackControllerRight.setPIDF(kP, kI, kD, kF);

        int targetTicks = target.height;

        final int currentLeft = getCurrentLeftPosition(), currentRight = getCurrentRightPosition();

        switch (target) { // OVERRIDES
            case MANUAL:
                final double gamepad = manual.getAsDouble();
                targetTicks = Math.max(
                        Position.BOTTOM_POSITION_CONTROL.height,
                        Math.min(
                                Position.TOP_POSITION_CONTROL.height,
                                lastTargetTicks + ((int) Math.round(gamepad * liftTickSpeed))
                        )
                );
                lastTargetTicks = targetTicks;
                break;
            case CUSTOM:
                targetTicks = customHeight;
                break;
        }

        final double feedbackOutputLeft = currentLeft > 50 || targetTicks > 10 ? feedbackControllerLeft.calculate(currentLeft, targetTicks) : 0;
        final double feedbackOutputRight = currentRight > 50 || targetTicks > 10 ? feedbackControllerRight.calculate(currentRight, targetTicks + 30) : 0;

        telemetry.addData("current left lift", currentLeft);
        telemetry.addData("current right lift", currentRight);
        telemetry.addData("target lift", targetTicks);

        left.set(MathUtils.clamp(feedbackOutputLeft, -1, 1));
        right.set(MathUtils.clamp(feedbackOutputRight, -1, 1));
    }

    public int getCurrentLeftPosition() {
        return left.getCurrentPosition();
    }

    public int getCurrentRightPosition() {
        return right.getCurrentPosition();
    }

    public void setPower(double power) {
        motors.forEach(m -> m.set(power));
    }

    public void setTargetPosition(Position position) {
        lastTargetTicks = MultipleMotorLift.target.height;
        MultipleMotorLift.target = position;
    }
}
