package org.firstinspires.ftc.teamcode.core.tools;
// VARIES WITH SINE TIMES GRAVITY CONSTANT FOR ITS PID
// TRIGGERS MOVE EACH DIRECTION


import static org.firstinspires.ftc.teamcode.util.Utilities.listFromParams;

import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ArmFeedforward;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.util.MathUtils;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.Subsystem;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

// ADDITIONAL MOTOR


@Config
public class Arm implements Subsystem {
    public static double lastAutoAngle = 0;
    public enum Position {
        HOME(0),
        PENETRATION(-10), //changed from -15 due to altered starting pos
        GRABBING(-15), // changed from -30
        DUMPING(90),
        ANGLED_DUMP(70),
        MANUAL(-1),
        GRABBING_TELEOP(-1),
        CUSTOM(-1);

        public final double angle;

        Position(double degrees) {
            this.angle = degrees;
        }
    }
    void resetArmPosition() {
        armMotors.get(0).stopAndResetEncoder();
    }
    private final double liftInchesPerTicks = 31.875D/1581D;
    private final double axleHeightIn = 11; // inches
    private final double zeroExtension = 18; // inches

    private final ArrayList<MotorEx> armMotors = new ArrayList<>(1); // only one for now, just want to make scalable
    private final DoubleSupplier manual;

    private ArmFeedforward armFeedforwardController;
    private final PIDController feedbackController;

    public static Position target = Position.CUSTOM;
    public static double customAngle = 0D;

    public static double kP = 0.06, kI = 0, kD = 0.0038, kG = 0.16, kF = 0.0002, kFS = 0.00014;

    private final Telemetry telemetry;
    private final Lift extension;

    public static int extensionPosition = 0;
    public static final int MAX_EXTENSION = 1300;

    public Arm(HardwareMap hardwareMap, Telemetry telemetry, @Nullable DoubleSupplier manualPowerController) {
        Arm.extensionPosition = 0;
        Arm.target = Position.CUSTOM;

        MotorEx armMotor1 = new MotorEx(hardwareMap, "arm", Motor.GoBILDA.RPM_312);
        armMotor1.setInverted(true);
        if (Math.abs(lastAutoAngle) < 1e-6) {
            armMotor1.stopAndResetEncoder();
        }

        armMotors.add(armMotor1);

        feedbackController = new PIDController(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(0, kG, 0, 0);

        this.manual = manualPowerController;
        this.telemetry = telemetry;

        extension = new Lift(hardwareMap, telemetry, null);
        extension.setTargetPosition(Lift.Position.CUSTOM);
    }

    @Override
    public void update() {
        feedbackController.setPID(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(0, kG + kF * extension.getTargetPosition(), 0, 0);

        double targetAngle = target.angle;
        extension.setTargetPosition(Arm.extensionPosition);

        switch (target) { // OVERRIDES
            case MANUAL:
                setPower(MathUtils.clamp(manual.getAsDouble() + (kG * (Math.cos(Math.toRadians(getCurrentAngle())))), -1, 1));
                return;
            case CUSTOM:
                targetAngle = customAngle;
                break;
            case GRABBING_TELEOP:
                targetAngle = Math.toDegrees(Math.acos(axleHeightIn/(extension.getCurrentPosition() * liftInchesPerTicks + zeroExtension))) - 95;
        }

        double currentDegrees = getCurrentAngle();
        double currentRadians = Math.toRadians(currentDegrees);
        final double output =
                currentDegrees < Position.HOME.angle - 7D || targetAngle < Position.HOME.angle - 7D ?
                        feedbackController.calculate(currentDegrees, targetAngle) +
                                armFeedforwardController.calculate(
                                        currentRadians, 0
                                )+ (kFS * extension.getTargetPosition())
                : 0;
        telemetry.addData("target arm angle", targetAngle);
        telemetry.addData("current arm angle", currentDegrees);
        telemetry.addData("extensionTicks", extension.getCurrentPosition());
        telemetry.addData("extensionTarget", extension.getTargetPosition());
        setPower(MathUtils.clamp(output, -1, 1));
    }

    public double getCurrentAngle() {
        return armMotors.get(0).getCurrentPosition() / armMotors.get(0).getCPR() * 10D / 42D * 360 - 20;
    }

    public double getTargetAngle() {
        return target.angle != -1 ? target.angle : customAngle;
    }

    public void setPower(double power) {
        armMotors.forEach(m -> m.set(power));
    }

    public void setTargetAngle(Position position) {
        Arm.target = position;
    }

    public void setExtensionPosition(int position) {
        extension.setTargetPosition(Lift.Position.CUSTOM);
        Arm.extensionPosition = position;
    }

    public void setExtensionPosition(Lift.Position position) {
        extension.setTargetPosition(position);
    }

    public int getExtensionPosition() {
        return Arm.extensionPosition;
    }


    @Config
    public static class Lift implements Subsystem {
        public enum Position {
            ZERO(0),
            SPECIMEN_CLIP_ON(1200), // placeholder
            MAX(1800),
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
        private int customHeight = 0;

        public static double liftTickSpeed = 30;
        private final Telemetry telemetry;

        private int lastTargetTicks = 0;
        public static double kP = 0.02, kI = 0.00, kD = 0.000, kF = 0.00;
        public Lift(HardwareMap hardwareMap, Telemetry telemetry, @Nullable DoubleSupplier manualPowerController) {
            Lift.target = Position.CUSTOM;

            left = new MotorEx(hardwareMap, "extension2", Motor.GoBILDA.RPM_435);
            left.setInverted(true);

            right = new MotorEx(hardwareMap, "extension1", Motor.GoBILDA.RPM_435);
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
                            Position.ZERO.height,
                            Math.min(
                                    Position.MAX.height,
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

            motors.forEach(m -> m.set(MathUtils.clamp(feedbackOutput, -1, 1)));
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
            lastTargetTicks = Lift.target.height;
            Lift.target = position;
        }

        public void setTargetPosition(int ticks) {
            if (Lift.target != Position.CUSTOM) {
                throw new IllegalStateException("not in custom mode, can't set custom height.");
            }
            lastTargetTicks = ticks;
            customHeight = ticks;
        }

        public int getTargetPosition() {
            return target.height != -1 ? target.height : customHeight;
        }
    }
}
