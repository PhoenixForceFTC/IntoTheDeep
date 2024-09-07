package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@TeleOp
public class Main extends LinearOpMode {
    public static Pose2d lastAutoPose = new Pose2d(0, 0, 0);

    @Override
    public void runOpMode() throws InterruptedException {
        final MecanumDrive drivetrain = new MecanumDrive(hardwareMap, lastAutoPose);
        final GamepadEx driverGamepad = new GamepadEx(gamepad1), toolGamepad = new GamepadEx(gamepad2);
        waitForStart();
        while (opModeIsActive()) {
            drivetrain.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(driverGamepad.getLeftY(), driverGamepad.getLeftX()),
                    driverGamepad.getRightX()
            ));
        }
    }
}
