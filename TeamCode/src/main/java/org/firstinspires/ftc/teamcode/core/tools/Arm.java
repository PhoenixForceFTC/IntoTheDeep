package org.firstinspires.ftc.teamcode.core.tools;
// VARIES WITH SINE TIMES GRAVITY CONSTANT FOR ITS PID
// TRIGGERS MOVE EACH DIRECTION


import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
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
        HOME(180 + 27D),
        PENETRATION(-14), //changed from -15 due to altered starting pos
        GRABBING(-44), // changed from -30
        DUMPING(42),
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

    public static double kP = 0.038, kI = 0, kD = 0.0023, kG = 0.16, kF = 0.0002;

    private final Telemetry telemetry;
    private final MultipleMotorLift extension;

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

        extension = new MultipleMotorLift(hardwareMap, telemetry, null);
        extension.setTargetPosition(MultipleMotorLift.Position.CUSTOM);
    }

    @Override
    public void update() {
        feedbackController.setPID(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(0, kG + kF * extension.getTargetPosition(), 0, 0);

        double targetAngle = target.angle;
        // TODO uncomment
        // MultipleMotorLift.customHeight = Arm.extensionPosition;

        switch (target) { // OVERRIDES
            case MANUAL:
                setPower(MathUtils.clamp(manual.getAsDouble() + (kG * (Math.cos(Math.toRadians(getCurrentPosition())))), -1, 1));
                return;
            case CUSTOM:
                targetAngle = customAngle;
                break;
            case GRABBING_TELEOP:
                targetAngle = Math.toDegrees(Math.acos(axleHeightIn/(extension.getCurrentPosition() * liftInchesPerTicks + zeroExtension))) - 95;
        }

        double currentDegrees = getCurrentPosition();
        double currentRadians = Math.toRadians(currentDegrees);
        final double output =
                currentDegrees < Position.HOME.angle - 7D || targetAngle < Position.HOME.angle - 7D ?
                        feedbackController.calculate(currentDegrees, targetAngle) +
                                armFeedforwardController.calculate(
                                        currentRadians, 0
                                )
                : 0;
        telemetry.addData("target arm angle", targetAngle);
        telemetry.addData("current arm angle", currentDegrees);
        telemetry.addData("extensionTicks", extension.getCurrentPosition());
        telemetry.addData("extensionTarget", extension.getTargetPosition());
        setPower(MathUtils.clamp(output, -1, 1));
    }

    public double getCurrentPosition() {
        return armMotors.get(0).getCurrentPosition() / armMotors.get(0).getCPR() * 10D / 42D * 360;
    }

    public void setPower(double power) {
        armMotors.forEach(m -> m.set(power));
    }

    public void setTargetPosition(Position position) {
        Arm.target = position;
    }

    public void setExtensionPosition(int position) {
        Arm.extensionPosition = position;
    }
}
