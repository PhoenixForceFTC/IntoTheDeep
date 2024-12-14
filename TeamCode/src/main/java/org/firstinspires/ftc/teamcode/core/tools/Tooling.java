package org.firstinspires.ftc.teamcode.core.tools;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.core.Subsystem;

@Config
public class Tooling implements Subsystem {
    final Arm arm;
    final Arm.Lift lift;
    final GamepadEx gamepad;
   //commented cuz no intake foo final Intake intake;
    public static int extensionIncreasePerLoop = 17;
    public static double armIncreasePerLoop = 1.7D;
    public Tooling(HardwareMap hardwareMap, Telemetry telemetry, GamepadEx toolGamepad) {
        this.arm = new Arm(hardwareMap, telemetry, null);
        this.lift = new Arm.Lift(hardwareMap, telemetry, null);
    //    this.intake = new Intake(hardwareMap, "intake", -1);
        this.gamepad = toolGamepad;
    }

    @Override
    public void update() {
        gamepad.readButtons();
        /*
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
        */
        final double rightTrigger = gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        final double leftTrigger = gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
        if (leftTrigger > rightTrigger * .8) {
            Arm.customAngle += (armIncreasePerLoop * leftTrigger);
        } else {
            Arm.customAngle -= (armIncreasePerLoop * rightTrigger);
        }
        Arm.extensionPosition = Math.max(gamepad.getButton(GamepadKeys.Button.START) ? -3500 : 0, Arm.extensionPosition + (
                extensionIncreasePerLoop * (
                        gamepad.getButton(GamepadKeys.Button.LEFT_BUMPER) ? 1 :
                                (gamepad.getButton(GamepadKeys.Button.RIGHT_BUMPER) ? -1 : 0)
                )));

        /*
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
         */
        if (gamepad.wasJustReleased(GamepadKeys.Button.RIGHT_BUMPER)) {
            // run intake
        }

        arm.update();
        lift.update();
        // update intake

    }
}
