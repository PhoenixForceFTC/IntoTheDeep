package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.opmodes.Main.lastAutoPose;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.core.tools.Arm;
import org.firstinspires.ftc.teamcode.core.tools.MultiAxisClawAssembly;
import org.firstinspires.ftc.teamcode.RoadRunner3DeadWheels.MecanumDrive;

import java.util.concurrent.TimeUnit;

@Autonomous
public class SpecimenAuto extends LinearOpMode {
    private MecanumDrive drivetrain;
    private Arm arm;
    private MultiAxisClawAssembly claw;

    private void sleepTools(int ms) {
        Timing.Timer timer = new Timing.Timer(ms, TimeUnit.MILLISECONDS);
        timer.start();
        while (!timer.done() && !isStopRequested()) {
            arm.update();
            claw.update();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new MecanumDrive(hardwareMap, lastAutoPose);
        arm = new Arm(hardwareMap, telemetry);
        claw = new MultiAxisClawAssembly(hardwareMap);
        hardwareMap.getAll(LynxModule.class).forEach(hub -> hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO)); // auto caching of sensor reads
        waitForStart();
        Arm.autoRan = true;
        Arm.customAngle = Arm.Position.DUMPING.angle;
        claw.setPosition(MultiAxisClawAssembly.Position.SUBMERSIBLE_PICKUP_HORIZONTAL);
        drivetrain.setDrivePowers(new PoseVelocity2d(new Vector2d(0.3, 0),0));
        sleepTools(700);
        Arm.extensionPosition = 1010;
        sleepTools(2500);
        Arm.customAngle = 67;
        drivetrain.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0),0));
        sleepTools(750);
        Arm.extensionPosition = 0;
        sleepTools(2000);
        claw.off();
        sleepTools(30000);
    }
}
