package org.firstinspires.ftc.teamcode.core.tools;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.Subsystem;

@Config
public class Tooling implements Subsystem {
    final Arm arm;
    final MultipleMotorLift lift;
    final ToggleablePositionServo claw;
    final GamepadEx gamepad;

    public static double tickIncreasePerLoop = 40D;
    public Tooling(HardwareMap hardwareMap, Telemetry telemetry, GamepadEx toolGamepad) {
        this.arm = new Arm(hardwareMap, telemetry, toolGamepad::getRightY);
        this.lift = new MultipleMotorLift(hardwareMap, telemetry, toolGamepad::getLeftY);
        this.claw = new ToggleablePositionServo(hardwareMap, .8, .2, "claw", true);
        this.gamepad = toolGamepad;


    }

    @Override
    public void update() {
        gamepad.readButtons();

        if (gamepad.wasJustReleased(GamepadKeys.Button.RIGHT_STICK_BUTTON)) {
            arm.setTargetPosition(Arm.Position.MANUAL);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.X)) {
            arm.setTargetPosition(Arm.Position.PENETRATION);
            Arm.extensionPosition = 0;
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.A)) {
            arm.setTargetPosition(Arm.Position.GRABBING_TELEOP);
            Arm.extensionPosition = 0;
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.START)) {
            arm.setTargetPosition(Arm.Position.HOME);
            Arm.extensionPosition = 0;
        }
        final double rightTrigger = gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        final double leftTrigger = gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
        Arm.extensionPosition = Math.max(0, Math.min(Arm.MAX_EXTENSION, Arm.extensionPosition + (int) Math.round((rightTrigger > leftTrigger ? rightTrigger : leftTrigger * -1) * tickIncreasePerLoop)));

        if (gamepad.wasJustReleased(GamepadKeys.Button.LEFT_STICK_BUTTON)) {
            lift.setTargetPosition(MultipleMotorLift.Position.MANUAL);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.DPAD_UP)) {
            lift.setTargetPosition(MultipleMotorLift.Position.TOP_POSITION_CONTROL);
            arm.setTargetPosition(Arm.Position.DUMPING);
            Arm.extensionPosition = 500;
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.DPAD_DOWN)) {
            lift.setTargetPosition(MultipleMotorLift.Position.BOTTOM_POSITION_CONTROL);
            arm.setTargetPosition(Arm.Position.PENETRATION);
            Arm.extensionPosition = 0;
        }

        if (gamepad.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER)) {
            claw.toggle();
        }

        if (gamepad.wasJustReleased(GamepadKeys.Button.BACK)) {
            arm.resetExtensionMotor();
        }

        arm.update();
        lift.update();
        claw.update();

    }
}
