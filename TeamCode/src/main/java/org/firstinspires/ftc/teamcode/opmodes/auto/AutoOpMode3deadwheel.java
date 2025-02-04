package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.core.tools.Arm;
import org.firstinspires.ftc.teamcode.core.tools.MultiAxisClawAssembly;
import org.firstinspires.ftc.teamcode.RoadRunner3DeadWheels.MecanumDrive;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

//next year use trajectory build upon initialiation
public abstract class AutoOpMode3deadwheel extends LinearOpMode {
    public MecanumDrive drivetrain;
    public Arm arm;
    public MultiAxisClawAssembly claw;

    public MecanumDrive drive;
    public MultiAxisClawAssembly multiAxisClawAssembly;
    //public ToggleablePositionServo claw;
    public Speed speed = Speed.FAST;

    private static final TranslationalVelConstraint accelConstraint =
            new TranslationalVelConstraint(200);

    // In inches per second
    private static final double VERY_SLOW_MAX_VEL = 3;
    private static final double SLOW_MAX_VEL = accelConstraint.minTransVel / 4;
    private static final double MEDIUM_MAX_VEL = accelConstraint.minTransVel / 3;
    private static final double FAST_MAX_VEL = accelConstraint.minTransVel;

    public enum Speed {
        VERY_SLOW,
        SLOW,
        MEDIUM,
        FAST
    }

    @Config
    public static class Position {
        public double X;
        public double Y;
        public double HEADING;

        /**
         * Creates a new position
         * @param x the x coordinate
         * @param y the y coordinate
         * @param heading the heading, in degrees
         */
        public Position(double x, double y, double heading) {
            X = x;
            Y = y;
            HEADING = heading;
        }
        public Position(Position position) {
            HEADING = position.HEADING;
        }

        public Pose2d toPose2d() {
            return new Pose2d(X, Y, Math.toRadians(HEADING));
        }

        public Vector2d toVector2d() {
            return new Vector2d(X, Y);
        }
        public Rotation2d toRotation2d(){
            return new Rotation2d(Math.cos(Math.toRadians(HEADING)), Math.sin(Math.toRadians(HEADING)));
        }
    }

    public void setup(Position startPosition) {
        drive = new MecanumDrive(hardwareMap,startPosition.toPose2d());
        arm = new Arm(hardwareMap, telemetry);
        claw = new MultiAxisClawAssembly(hardwareMap);
        arm.setTargetAngle(Arm.Position.HOME);
        //arm.setExtensionPosition(Arm.Lift.Position.ZERO);
        Arm.autoRan = true;
        Arm.customAngle = Arm.Position.DUMPING.angle;
        claw.setPosition(MultiAxisClawAssembly.Position.SUBMERSIBLE_PICKUP_HORIZONTAL);
        this.multiAxisClawAssembly = new MultiAxisClawAssembly(hardwareMap);
        while(!isStarted() && !isStopRequested()){
//            camera.detection();
        }

        if (isStopRequested()) return;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public Speed getSpeed() {
        return speed;
    }

    void runForTime(long ms, Runnable run) {
        Timing.Timer timer = new Timing.Timer(ms,  TimeUnit.MILLISECONDS);
        timer.start();
        while (!timer.done() && !isStopRequested()) {
            run.run();
        }
    }

    void sleepTools(long ms) {
        runForTime(ms, () -> {
            arm.update();
            claw.update();
        });
    }

    private VelConstraint getVelConstraint() {
        double maxVel = 0;
        switch (speed) {
            case VERY_SLOW:
                maxVel = VERY_SLOW_MAX_VEL; break;
            case SLOW:
                maxVel = SLOW_MAX_VEL; break;
            case MEDIUM:
                maxVel = MEDIUM_MAX_VEL; break;
            case FAST:
                maxVel = FAST_MAX_VEL; break;
        }

        return new MinVelConstraint(
                Arrays.asList(
                        new TranslationalVelConstraint(accelConstraint.minTransVel),
                        new AngularVelConstraint(accelConstraint.minTransVel)
                ));
        };

    /**
     * Sets the starting position of the robot
     * @param position the starting position
     */
    public void setStartPosition(Position position) {
        drive.localizer.setPose(position.toPose2d());
    }

    /**
     * Spline to the specified position
     * @param position the position
     */
    public void splineTo(Position position) {
        Action traj = drive.actionBuilder(drive.localizer.getPose())

                .splineTo(position.toVector2d(), position.toRotation2d(), getVelConstraint()/*, accelConstraint*/)
                .build();

        Actions.runBlocking(traj);
    }

    /**
     * Go to the specified position
     * @param position the position
     */
    public void goTo(Position position) {

        Action traj = drive.actionBuilder(drive.localizer.getPose())
                .splineToLinearHeading(position.toPose2d(), Math.toRadians(position.HEADING), new TranslationalVelConstraint(500)/*, accelConstraint*/)
                .build();

        Actions.runBlocking(traj);
    }

    /**
     * Turns the specified amount
     * @param angle angle, in degrees
     */
    public void turn(double angle) {
        Action traj = drive.actionBuilder(drive.localizer.getPose())
                .turn(Math.toRadians(angle))
                .build();

        Actions.runBlocking(traj);
    }
}
