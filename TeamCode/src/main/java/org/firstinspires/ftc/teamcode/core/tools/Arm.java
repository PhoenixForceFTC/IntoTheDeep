package org.firstinspires.ftc.teamcode.core.tools;

import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ArmFeedforward;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.util.MathUtils;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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
        motors.get(0).stopAndResetEncoder();
    }
    private final double liftInchesPerTicks = 31.875D/1581D;
    private final double axleHeightIn = 11; // inches
    private final double zeroExtension = 18; // inches

    private final ArrayList<MotorEx> motors = new ArrayList<>(1); // only one for now, just want to make scalable
    private final DoubleSupplier manual;

    private ArmFeedforward armFeedforwardController;
    private final PIDController feedbackController;

    public static Position target = Position.HOME;
    public static double customAngle = 0D;

    public static double kP = 0.03, kI = 0, kD = 0.001, kG = 0.145;

    private final Telemetry telemetry;
    private final DcMotorEx extension;

    public static int extensionPosition = 0;
    public static final int MAX_EXTENSION = 1300;

    public Arm(HardwareMap hardwareMap, Telemetry telemetry, @Nullable DoubleSupplier manualPowerController) {
        Arm.extensionPosition = 0;
        Arm.target = Position.HOME;

        MotorEx motor1 = new MotorEx(hardwareMap, "rightArm", Motor.GoBILDA.RPM_30);
        motor1.setInverted(true);
        if (Math.abs(lastAutoAngle) < 1e-6) {
            motor1.stopAndResetEncoder();
        }

        motors.add(motor1);

        feedbackController = new PIDController(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(0, kG, 0, 0);

        this.manual = manualPowerController;
        this.telemetry = telemetry;

        this.extension = hardwareMap.get(DcMotorEx.class, "liftExtension");
        resetExtensionMotor();
    }

    void resetExtensionMotor() {
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setTargetPosition(0);
        extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        extension.setPower(1);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void update() {
        feedbackController.setPID(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(0, kG, 0, 0);

        double targetAngle = target.angle;
        extension.setTargetPosition(Arm.extensionPosition);

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
        return motors.get(0).getCurrentPosition() / motors.get(0).getCPR() * 360 + Position.HOME.angle;
    }

    public void setPower(double power) {
        motors.forEach(m -> m.set(power));
    }

    public void setTargetPosition(Position position) {
        Arm.target = position;
    }

    public void setExtensionPosition(int position) {
        Arm.extensionPosition = position;
    }
}
