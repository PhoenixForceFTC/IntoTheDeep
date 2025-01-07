package org.firstinspires.ftc.teamcode.core.tools;
// VARIES WITH SINE TIMES GRAVITY CONSTANT FOR ITS PID
// TRIGGERS MOVE EACH DIRECTION


import static org.firstinspires.ftc.teamcode.util.Utilities.listFromParams;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ArmFeedforward;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.util.MathUtils;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.core.Subsystem;

import java.util.ArrayList;

// ADDITIONAL MOTOR


@Config
public class Arm implements Subsystem {
    public static boolean autoRan = false;
    public static double MIN_ANGLE = -25, MAX_ANGLE = 105;
    public static double lastAutoAngle = 0;
    private boolean pullingArm = false;
    public enum Position {
        HOME(0),
        GRABBING(-7), // changed from -30
        DUMPING(95),
        SPECIMEN_PICKUP(17),
        CUSTOM(-1);

        public final double angle;

        Position(double degrees) {
            this.angle = degrees;
        }
    }

    void resetArmPosition() {
        armMotors.get(0).stopAndResetEncoder();
    }

    private final ArrayList<MotorEx> armMotors = new ArrayList<>(1); // only one for now, just want to make scalable

    private ArmFeedforward armFeedforwardController;
    private final PIDController feedbackController;

    public static Position target = Position.CUSTOM;
    public static double customAngle = 0D;

    public static double kP = 0.06, kI = 0, kD = 0.0038, kG = 0.013, kF = 0.0002, kFS = 0.00014;

    private final Telemetry telemetry;
    private final Lift extension;

    public static int extensionPosition = 0;
    public static final int MAX_EXTENSION = 1300;

    public Arm(HardwareMap hardwareMap, Telemetry telemetry) {
        Arm.customAngle = 0;
        Arm.extensionPosition = 0;
        Arm.target = Position.CUSTOM;

        MotorEx armMotor1 = new MotorEx(hardwareMap, "arm", Motor.GoBILDA.RPM_223);
        armMotor1.setInverted(true);
        armMotors.add(armMotor1);

        if (!Arm.autoRan) {
            armMotor1.stopAndResetEncoder();
        } else {
            Arm.customAngle = getCurrentAngle();
        }

        feedbackController = new PIDController(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(0, kG, 0, 0);

        this.telemetry = telemetry;

        extension = new Lift(hardwareMap, telemetry);
        extension.setTargetPosition(Lift.Position.CUSTOM);
    }

    @Override
    public void update() {
        Arm.customAngle = MathUtils.clamp(Arm.customAngle, Arm.MIN_ANGLE, Arm.MAX_ANGLE);
        feedbackController.setPID(kP, kI, kD);
        if (!pullingArm) {
            armFeedforwardController = new ArmFeedforward(0, kG + kF * extension.getTargetPosition(), 0, 0);

            double targetAngle = Arm.target.angle;
            extension.setTargetPosition(Arm.extensionPosition);

            if (Arm.target == Position.CUSTOM) {
                targetAngle = customAngle;
            }

            double currentDegrees = getCurrentAngle();
            double targetRadians = Math.toRadians(targetAngle);
            final double output = feedbackController.calculate(currentDegrees, targetAngle) +
                    armFeedforwardController.calculate(
                            targetRadians, 0
                    ) + (kFS * extension.getTargetPosition());
            telemetry.addData("target arm angle", targetAngle);
            telemetry.addData("current arm angle", currentDegrees);
            telemetry.addData("extensionTicks", extension.getCurrentPosition());
            telemetry.addData("extensionTarget", extension.getTargetPosition());

            setPower(MathUtils.clamp(output, -1, 1));
        }
        extension.update();
    }

    public double getCurrentAngle() {
        return armMotors.get(0).getCurrentPosition() / armMotors.get(0).getCPR() * 10D / 42D * 360 - 25;
    }

    public double getTargetAngle() {
        return target.angle != -1 ? target.angle : customAngle;
    }

    public void setPower(double power) {
        armMotors.forEach(m -> m.set(power));
    }

    public void setTargetAngle(Position position) {
        Arm.customAngle = position.angle;
    }

    public void setExtensionPosition(int position) {
        extension.setTargetPosition(Lift.Position.CUSTOM);
        Arm.extensionPosition = position;
    }

    public void setExtensionPosition(Lift.Position position) {
        extension.setTargetPosition(position);
    }

    public int getTargetExtensionPosition() {
        return Arm.extensionPosition;
    }

    public int getCurrentExtensionPosition() {
        return extension.getCurrentPosition();
    }

    public void resetArmMotors() {
        armMotors.forEach(m -> m.set(0));
        armMotors.forEach(Motor::stopAndResetEncoder);
    }

    public void resetExtensionMotors(){
        extension.motors.forEach(m -> m.set(0));
        extension.motors.forEach(Motor::stopAndResetEncoder);
    }

    @Config
    public static class Lift implements Subsystem {
        public enum Position {
            ZERO(0),
            SPECIMEN_CLIP_ON(1200), // placeholder
            MAX(1800),
            CUSTOM(-1);

            public final int height;

            Position(int ticks) {
                this.height = ticks;
            }
        }

        private final ArrayList<MotorEx> motors;
        private final MotorEx left, right;
        private final PIDFController feedbackController;

        public static Position target = Position.CUSTOM;
        private int customHeight = 0;

        public static double liftTickSpeed = 30;
        private final Telemetry telemetry;

        private boolean pulling = false;

        private int lastTargetTicks = 0;
        public static double kP = 0.02, kI = 0.00, kD = 0.000, kF = 0.00;

        public Lift(HardwareMap hardwareMap, Telemetry telemetry) {
            Lift.target = Position.CUSTOM;

            left = new MotorEx(hardwareMap, "extension2", Motor.GoBILDA.RPM_435);
            left.setInverted(true);

            right = new MotorEx(hardwareMap, "extension1", Motor.GoBILDA.RPM_435);
            right.setInverted(false);

            motors = listFromParams(left, right);
            motors.forEach(m -> {
                if (!Arm.autoRan) {
                    m.stopAndResetEncoder();
                }
                m.setDistancePerPulse(10 / 42D);
                m.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
            });

            feedbackController = new PIDFController(kP, kI, kD, kF);

            this.telemetry = telemetry;
        }

        @Override
        public void update() {
            if (!pulling) {
                feedbackController.setPIDF(kP, kI, kD, kF);

                int targetTicks = target.height;

                final int current = getCurrentPosition();

                if (target == Position.CUSTOM) {
                    targetTicks = customHeight;
                }

                // the numbers below can be different, likely should be eliminated if not worried about a "slam"
                final double feedbackOutput = /*current > 2 || targetTicks > 1 ? */feedbackController.calculate(current, targetTicks) /*: 0*/;

                telemetry.addData("current extension", current);
                telemetry.addData("target lift", targetTicks);
                telemetry.addData("extension power", feedbackOutput);

                motors.forEach(m -> m.set(MathUtils.clamp(feedbackOutput, -1, 1)));
            }
        }

        public int getCurrentPosition() {
            return (left.getCurrentPosition() + right.getCurrentPosition()) / 2;
        }

        public void setPower(double power) {
            motors.forEach(m -> m.set(power));
        }

        public void setTargetPosition(Position position) {
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

        protected void pull() {
            pulling = true;
            motors.forEach(m -> m.set(-1));
        }

        protected void stopPulling() {
            Lift.target = Position.CUSTOM;
            Arm.extensionPosition = getCurrentPosition();
            pulling = false;
        }
    }

    public void pull() {
        pullingArm = true;
        extension.pull();
    }

    public void pullArm() {
        pullingArm = true;
        armMotors.forEach(m -> m.set(-0.5));
    }

    public void stopPullingArm() {
        Arm.target = Position.CUSTOM;
        Arm.customAngle = getCurrentAngle();
        pullingArm = false;
    }

    public void stopPulling() {
        pullingArm = false;
        extension.stopPulling();
    }

    public double getExtensionCurrent() {
        return extension.motors.get(0).motorEx.getCurrent(CurrentUnit.AMPS);
    }

    public double getArmCurrent() {
        return armMotors.get(0).motorEx.getCurrent(CurrentUnit.AMPS);
    }
}
