package org.firstinspires.ftc.teamcode.core.tools;

import androidx.annotation.Nullable;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.core.Subsystem;

public class Tooling implements Subsystem {
    final Arm arm;
    final MultipleMotorLift lift;
    final ToggleablePositionServo claw;
    final GamepadEx gamepad;

    final ToggleButtonReader manualArmToggle, manualLiftToggle;

    public Tooling(HardwareMap hardwareMap, GamepadEx toolGamepad) {
        this.arm = new Arm(hardwareMap, toolGamepad::getRightY);
        this.lift = new MultipleMotorLift(hardwareMap, toolGamepad::getLeftY);
        this.claw = new ToggleablePositionServo(hardwareMap, 0, 1, "claw", true);
        this.gamepad = toolGamepad;

        manualArmToggle = new ToggleButtonReader(toolGamepad, GamepadKeys.Button.RIGHT_STICK_BUTTON);
        manualLiftToggle = new ToggleButtonReader(toolGamepad, GamepadKeys.Button.LEFT_STICK_BUTTON);

    }

    @Override
    public void update() {
        gamepad.readButtons();
        manualArmToggle.readValue();
        manualLiftToggle.readValue();

        if (manualArmToggle.getState()) {
            arm.setTargetPosition(Arm.Position.MANUAL);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.B)) {
            arm.setTargetPosition(Arm.Position.DUMPING);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.A)) {
            arm.setTargetPosition(Arm.Position.PENETRATION);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.X)) {
            arm.setTargetPosition(Arm.Position.GRABBING);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.Y)) {
            arm.setTargetPosition(Arm.Position.HOME);
        }

        if (manualLiftToggle.getState()) {
            lift.setTargetPosition(MultipleMotorLift.Position.MANUAL);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.DPAD_UP)) {
            lift.setTargetPosition(MultipleMotorLift.Position.TOP_POSITION_CONTROL);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.DPAD_DOWN)) {
            lift.setTargetPosition(MultipleMotorLift.Position.BOTTOM_POSITION_CONTROL);
        }

        if (gamepad.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER)) {
            claw.toggle();
        }

        arm.update();
        lift.update();
        claw.update();

    }
}
