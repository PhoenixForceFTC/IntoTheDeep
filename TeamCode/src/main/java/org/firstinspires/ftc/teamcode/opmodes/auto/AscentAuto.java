package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.opmodes.Main.lastAutoPose;

import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.core.tools.Arm;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.concurrent.TimeUnit;

@Autonomous
public class AscentAuto extends LinearOpMode {
    private MecanumDrive drivetrain;
    private Arm arm;

    private void sleepTools(int ms) {
        Timing.Timer timer = new Timing.Timer(ms, TimeUnit.MILLISECONDS);
        timer.start();
        while (!timer.done() && !isStopRequested()) {
            arm.update();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrive(hardwareMap, lastAutoPose);
        arm = new Arm(hardwareMap, telemetry);
        hardwareMap.getAll(LynxModule.class).forEach(hub -> hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO)); // auto caching of sensor reads
        waitForStart();
        Arm.autoRan = true;
        drivetrain.setDrivePowers(0, 0.5, 0);
        sleepTools(3000);
        Arm.customAngle = Arm.MAX_ANGLE;
        Arm.extensionPosition = 300;
        drivetrain.setDrivePowers(0.3, 0, 0);
        sleepTools(1500);
        drivetrain.setDrivePowers(0, 0, 0);
        Arm.customAngle = 45;
        sleepTools(30000);
    }
}
