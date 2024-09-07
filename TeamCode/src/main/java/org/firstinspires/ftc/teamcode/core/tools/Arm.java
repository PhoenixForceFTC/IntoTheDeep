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

import org.firstinspires.ftc.teamcode.core.Subsystem;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

@Config
public class Arm implements Subsystem {
    public enum Position {
        HOME(180),
        PENETRATION(-30),
        GRABBING(-40),
        DUMPING(45),
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

    public static double kP = 0, kI = 0, kD = 0, kS = 0, kG = 0, kV = 0;

    public Arm(HardwareMap hardwareMap, @Nullable DoubleSupplier manualPowerController) {
        MotorEx motor1 = new MotorEx(hardwareMap, "leftLift", Motor.GoBILDA.RPM_43);
        motor1.stopAndResetEncoder();

        motors.add(motor1);

        feedbackController = new PIDController(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(kS, kG, kV, 0);

        this.manual = manualPowerController;

    }

    @Override
    public void update() {
        feedbackController.setPID(kP, kI, kD);
        armFeedforwardController = new ArmFeedforward(kS, kG, kV, 0);

        double targetAngle = target.angle.getDegrees();

        switch (target) { // OVERRIDES
            case MANUAL:
                setPower(MathUtils.clamp(manual.getAsDouble() + (kG * getCurrentPosition().getRadians()), -1, 1));
                return;
            case CUSTOM:
                targetAngle = customAngle;
        }

        final Rotation2d current = getCurrentPosition();
        final double output =
                current.getDegrees() > 0 ?
                        feedbackController.calculate(current.minus(Rotation2d.fromDegrees(targetAngle)).getDegrees(), 0) +
                                armFeedforwardController.calculate(
                                        current.getRadians(), getCurrentVelocity().getRadians()
                                )
                : 0;
        setPower(MathUtils.clamp(output, -1, 1));
    }

    public Rotation2d getCurrentPosition() {
        return Rotation2d.fromDegrees(
                motors.stream().mapToDouble(m -> m.getCurrentPosition() / m.getCPR()).average().orElse(0) * 360D
        );
    }

    public Rotation2d getCurrentVelocity() {
        return Rotation2d.fromDegrees(
                motors.stream().mapToDouble(m -> m.getCorrectedVelocity() / m.getCPR()).average().orElse(0) * 360D
        );
    }

    public void setPower(double power) {
        motors.forEach(m -> m.set(power));
    }

    public void setTargetPosition(Position position) {
        Arm.target = position;
    }
}
