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
    final GamepadEx driverGamepad, toolGamepad;
  //  final Intake intake;
    final MultiAxisClawAssembly multiAxisClawAssembly;

            ;
    public static int extensionIncreasePerLoop = 17;
    public static int LOCKOUT = 100;
    public static double armIncreasePerLoop = 1.7D;
    public Tooling(HardwareMap hardwareMap, Telemetry telemetry, GamepadEx driverGamepad, GamepadEx toolGamepad) {
        this.arm = new Arm(hardwareMap, telemetry);
        //  this.intake = new Intake(hardwareMap, "intake", -1);
        this.driverGamepad = driverGamepad;
        this.toolGamepad = toolGamepad;
        this.multiAxisClawAssembly = new MultiAxisClawAssembly(hardwareMap);
    }

    @Override
    public void update() {
        driverGamepad.readButtons();
        toolGamepad.readButtons();
        if (driverGamepad.wasJustReleased(GamepadKeys.Button.DPAD_DOWN)) {
            arm.setTargetAngle(Arm.Position.GRABBING);
        } else if (toolGamepad.wasJustReleased(GamepadKeys.Button.DPAD_LEFT)) {
            arm.setTargetAngle(Arm.Position.SPECIMEN_PICKUP);
        } else if (toolGamepad.wasJustReleased(GamepadKeys.Button.DPAD_RIGHT)) {
            arm.setTargetAngle(Arm.Position.HOME);
            Arm.extensionPosition = 0;
        }

        final double rightTrigger = toolGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        final double leftTrigger = toolGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
        if (leftTrigger > rightTrigger * .8) {
            Arm.customAngle += (armIncreasePerLoop * leftTrigger);
        } else {
            Arm.customAngle -= (armIncreasePerLoop * rightTrigger);
        }

        final int oldDelta = Math.abs(Arm.extensionPosition - arm.getCurrentExtensionPosition());
        final int newExtensionPosition = Math.max(toolGamepad.getButton(GamepadKeys.Button.START) ? -3500 : 0, Arm.extensionPosition + (
                extensionIncreasePerLoop * (
                        toolGamepad.getButton(GamepadKeys.Button.LEFT_BUMPER) ? 1 :
                                (toolGamepad.getButton(GamepadKeys.Button.RIGHT_BUMPER) ? -1 : 0)
                )));
        if (!(oldDelta > LOCKOUT && Math.signum(oldDelta) == Math.signum(newExtensionPosition - Arm.extensionPosition))) {
            Arm.extensionPosition = newExtensionPosition;
        }
        if (toolGamepad.wasJustReleased(GamepadKeys.Button.A)) {
            multiAxisClawAssembly.setPosition(
                    multiAxisClawAssembly.getPosition() == MultiAxisClawAssembly.Position.SUBMERSIBLE_PICKUP_HORIZONTAL
                            ? MultiAxisClawAssembly.Position.SUBMERSIBLE_PICKUP_VERTICAL
                            : MultiAxisClawAssembly.Position.SUBMERSIBLE_PICKUP_HORIZONTAL
            );
        } else if (toolGamepad.wasJustReleased(GamepadKeys.Button.Y)) {
            multiAxisClawAssembly.setPosition(MultiAxisClawAssembly.Position.DUMP_AND_WALL_REMOVAL_AND_HOME);
        } else if (toolGamepad.wasJustReleased(GamepadKeys.Button.X)) {
            multiAxisClawAssembly.setPosition(MultiAxisClawAssembly.Position.WALL_SPECIMEN_PICKUP);
        }

        if (driverGamepad.wasJustReleased(GamepadKeys.Button.A)) {
            multiAxisClawAssembly.toggle();
        }

        if (driverGamepad.wasJustPressed(GamepadKeys.Button.BACK)) {
            arm.pull();
        } else if (driverGamepad.wasJustReleased(GamepadKeys.Button.BACK)) {
            arm.stopPulling();
        }

        arm.update();
        multiAxisClawAssembly.update();
    }
}
