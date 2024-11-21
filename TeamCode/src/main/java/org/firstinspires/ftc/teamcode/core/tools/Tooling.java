package org.firstinspires.ftc.teamcode.core.tools;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.Subsystem;

@Config
public class Tooling implements Subsystem {
    final Lift lift;
    final ToggleablePositionServo claw;
    final GamepadEx gamepad;

    public static double tickIncreasePerLoop = 40D;
    public Tooling(HardwareMap hardwareMap, Telemetry telemetry, GamepadEx toolGamepad) {
        this.lift = new Lift(hardwareMap, telemetry, toolGamepad::getLeftY);
        this.claw = new ToggleablePositionServo(hardwareMap, .8, .2, "claw", true);
        this.gamepad = toolGamepad;


    }

    @Override
    public void update() {
        gamepad.readButtons();


        final double rightTrigger = gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        final double leftTrigger = gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
        if (gamepad.wasJustReleased(GamepadKeys.Button.RIGHT_STICK_BUTTON)) {
            lift.setTargetPosition(Lift.Position.MANUAL);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.DPAD_UP)) {
            lift.setTargetPosition(Lift.Position.TOP_POSITION_CONTROL);
        } else if (gamepad.wasJustReleased(GamepadKeys.Button.DPAD_DOWN)) {
            lift.setTargetPosition(Lift.Position.BOTTOM_POSITION_CONTROL);
        }

        if (gamepad.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER)) {
            claw.toggle();
        }


        lift.update();
        claw.update();

    }
}
