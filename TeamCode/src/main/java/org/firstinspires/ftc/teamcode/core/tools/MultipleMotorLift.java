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
    private final PIDFController feedbackController;
    private final DoubleSupplier manual;

    public static Position target = Position.CUSTOM;
    public static int customHeight = 0;

    public static double liftTickSpeed = 30;
    private final Telemetry telemetry;

    private int lastTargetTicks = 0;
    public static double kP = 0.02, kI = 0.00, kD = 0.000, kF = 0.00;
    public MultipleMotorLift(HardwareMap hardwareMap, Telemetry telemetry, @Nullable DoubleSupplier manualPowerController) {
        MultipleMotorLift.target = Position.CUSTOM;

        left = new MotorEx(hardwareMap, "extension1", Motor.GoBILDA.RPM_435);
        left.setInverted(true);

        right = new MotorEx(hardwareMap, "extension2", Motor.GoBILDA.RPM_435);
        right.setInverted(false);

        motors = listFromParams(left, right);
        motors.forEach(m -> {
            m.stopAndResetEncoder();
            m.setDistancePerPulse(10/42D);
        });

        feedbackController = new PIDFController(kP, kI, kD, kF);

        this.manual = manualPowerController;
        this.telemetry = telemetry;
    }

    @Override
    public void update() {
        feedbackController.setPIDF(kP, kI, kD, kF);

        int targetTicks = target.height;

        final int current = getCurrentPosition();

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

        // the numbers below can be different, likely should be eliminated if not worried about a "slam"
        final double feedbackOutput = /*current > 2 || targetTicks > 1 ? */feedbackController.calculate(current, targetTicks) /*: 0*/;

        telemetry.addData("current extension", current);
        telemetry.addData("target lift", targetTicks);

        motors.forEach(m -> m.set(MathUtils.clamp(-feedbackOutput, -1, 1)));
    }

    public int getCurrentPosition() {
        return (left.getCurrentPosition() + right.getCurrentPosition()) / 2;
    }

    public void setPower(double power) {
        motors.forEach(m -> m.set(power));
    }

    public void setTargetPosition(Position position) {
        if (manual == null && position == Position.MANUAL) {
            throw new IllegalStateException("can't set to manual mode, no manual supplier given");
        }
        lastTargetTicks = MultipleMotorLift.target.height;
        MultipleMotorLift.target = position;
    }

    public int getTargetPosition() {
        return target.height != -1 ? target.height : customHeight;
    }
}
