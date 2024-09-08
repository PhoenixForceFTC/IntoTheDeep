package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.tools.Tooling;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@TeleOp
public class Main extends LinearOpMode {
    public static Pose2d lastAutoPose = new Pose2d(0, 0, 0);
    private final MultipleTelemetry telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), super.telemetry);
    @Override
    public void runOpMode() throws InterruptedException {
        hardwareMap.getAll(LynxModule.class).forEach(hub -> hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO)); // auto caching of sensor reads
        final MecanumDrive drivetrain = new MecanumDrive(hardwareMap, lastAutoPose);
        final GamepadEx driverGamepad = new GamepadEx(gamepad1), toolGamepad = new GamepadEx(gamepad2);
        final Tooling tooling = new Tooling(hardwareMap, this.telemetry, toolGamepad);
        waitForStart();
        while (opModeIsActive()) {
            drivetrain.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(driverGamepad.getLeftY(), -driverGamepad.getLeftX()),
                    -driverGamepad.getRightX()
            ));
            tooling.update();
            telemetry.update();
        }
    }
}
