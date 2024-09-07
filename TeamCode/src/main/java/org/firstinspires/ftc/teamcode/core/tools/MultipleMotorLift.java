package org.firstinspires.ftc.teamcode.core.tools;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.util.MathUtils;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.Subsystem;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import static org.firstinspires.ftc.teamcode.util.Utilities.*;

import androidx.annotation.Nullable;

@Config
public class MultipleMotorLift implements Subsystem {
    public enum Position {
        BOTTOM_POSITION_CONTROL(0),
        TOP_POSITION_CONTROL(5000),
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

    public static Position target = Position.BOTTOM_POSITION_CONTROL;
    public static int customHeight = 0;

    public static double kP = 0, kI = 0, kD = 0, kF = 0, kS = 0, kG = 0, kV = 0;
    public MultipleMotorLift(HardwareMap hardwareMap, @Nullable DoubleSupplier manualPowerController) {
        left = new MotorEx(hardwareMap, "leftLift", Motor.GoBILDA.RPM_435);
        left.setInverted(true);
        left.stopAndResetEncoder();

        right = new MotorEx(hardwareMap, "rightLift", Motor.GoBILDA.RPM_435);
        right.stopAndResetEncoder();

        motors = listFromParams(left, right);

        feedbackController = new PIDFController(kP, kI, kD, kF);

        this.manual = manualPowerController;
    }


    @Override
    public void update() {
        feedbackController.setPIDF(kP, kI, kD, kF);

        int targetTicks = target.height;
        switch (target) { // OVERRIDES
            case MANUAL:
                setPower(MathUtils.clamp(manual.getAsDouble() + (kF * getCurrentPosition()), -1, 1));
                return;
            case CUSTOM:
                targetTicks = customHeight;
        }
        final double current = getCurrentPosition();
        final double feedbackOutput = current > 0 ? feedbackController.calculate(current, targetTicks) : 0;
        setPower(MathUtils.clamp(feedbackOutput, -1, 1));
    }

    public double getCurrentPosition() {
        return motors.stream().mapToInt(MotorEx::getCurrentPosition).average().orElse(-1);
    }

    public void setPower(double power) {
        motors.forEach(m -> m.set(power));
    }

    public void setTargetPosition(Position position) {
        MultipleMotorLift.target = position;
    }
}
