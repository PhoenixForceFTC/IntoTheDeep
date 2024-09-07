package oldcode.OpModes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.VelConstraint;
//import com.acmerobotics.roadrunner.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import oldcode.Subsystems.Drop;
import oldcode.Subsystems.Intake;
import oldcode.Subsystems.Snagger;
import oldcode.Subsystems.Swinch;
import oldcode.Subsystems.TopGate;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.Arrays;
//next year use trajectory build upon initialiation
public abstract class AutoOpMode extends LinearOpMode {

    public MecanumDrive drive;
    public TopGate topGate;
    public Intake intake;
    public Swinch swinch;

    public Drop drop;
    public Snagger snagger;

    public Speed speed = Speed.FAST;

    private static final TranslationalVelConstraint accelConstraint =
            new TranslationalVelConstraint(100);

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
        drop = new Drop(this);
        snagger = new Snagger(this);
        topGate = new TopGate(this, drop);
        intake = new Intake(this, drop, topGate);
        swinch = new Swinch(this);
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
        drive.pose=position.toPose2d();
    }

    /**
     * Spline to the specified position
     * @param position the position
     */
    public void splineTo(Position position) {
        Action traj = drive.actionBuilder(drive.pose)

                .splineTo(position.toVector2d(), position.toRotation2d(), getVelConstraint()/*, accelConstraint*/)
                .build();

        Actions.runBlocking(traj);
    }

    /**
     * Go to the specified position
     * @param position the position
     */
    public void goTo(Position position) {
        Action traj = drive.actionBuilder(drive.pose)
                .strafeTo(position.toVector2d(), getVelConstraint()/*, accelConstraint*/)
                .build();

        Actions.runBlocking(traj);
    }

    /**
     * Turns the specified amount
     * @param angle angle, in degrees
     */
    public void turn(double angle) {
        Action traj = drive.actionBuilder(drive.pose)
                .turn(Math.toRadians(angle))
                .build();

        Actions.runBlocking(traj);
    }
}
