package oldcode.OpModes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import oldcode.Subsystems.MecanumWheels;
import oldcode.Subsystems.Drop;
import oldcode.Subsystems.Swinch;
import oldcode.Subsystems.PlaneServo;
import oldcode.Subsystems.Snagger;
import oldcode.Subsystems.TopGate;
import oldcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.ButtonToggle;
/**
 * FTC WIRES TeleOp Example
 *
 */
@TeleOp(name = "Phoenix Tuff Tele", group = "00-Teleop")

public class TeleOpMode extends LinearOpMode {

    private OpMode opMode;
    private PlaneServo planeServo;
    private MecanumWheels mecanumWheels;
    private TopGate topGate;
    private Swinch swinch;
    private Intake intake;

    //    private MotorEncoder motorEncoder;
    //private IntakeServo axonServo;
    private boolean isBPressed;
    private ButtonToggle manualMode = new ButtonToggle(false);
    private Drop drop;
    private Snagger snagger;
    // @Override
    /*
     * Constructor for passing all the subsystems in order to make the subsystem be able to use
     * and work/be active
     */

    //@Override
    public void runOpMode() throws InterruptedException {
        //this.telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        /* Create Subsystem Objects*/
        // driveTrain = new DriveTrain(hardwareMap);
        //newLift2 = new NewLift2(this);
        drop = new Drop(this);
        snagger= new Snagger(this);
        topGate= new TopGate(this,drop);
        mecanumWheels = new MecanumWheels(this);
        intake = new Intake(this,drop,topGate);
        planeServo = new PlaneServo(this);
        swinch = new Swinch(this);
        waitForStart();


        while (!isStopRequested()) {
            while (opModeIsActive()) {
                //     sensor.ConePresent();
                mecanumWheels.move();
                if ((!gamepad1.dpad_left)&&snagger.reachedTarget()) {
                    snagger.move(gamepad2.left_stick_y);
                } else {
                    snagger.controlLift2();
                }
                topGate.controlGate();
                snagger.move(gamepad2.left_stick_y);
                intake.lights();
                intake.buzzController();
                manualMode.update(gamepad2.options);
                if (manualMode.isActive()) {
                    drop.move(gamepad2.right_stick_y);
                } else {
                    drop.controlLift2();
                }


                if (gamepad1.left_trigger > 0.5 && gamepad1.right_trigger > 0.5 && gamepad1.left_bumper && gamepad1.right_bumper) {
                    planeServo.GoToPosition((float) 0.13);
                } else {
                    planeServo.GoToPosition(0);
                }


                if (gamepad2.left_stick_button) {
                    intake.transferPixel();
                } else if (gamepad2.dpad_up) {
                    intake.returnPixelTeleOpMode();
                } else if (gamepad2.left_trigger > 0.2) {
                    intake.eatPixel();
                } else {
                    intake.stop();
                }

               // if (gamepad2.right_stick_button) {
               //     drop.resetPosition();
              //  }
                if(gamepad2.left_bumper){
                    swinch.setClawClosed(false);
                }else{
                    swinch.setClawClosed(true);
                }
                telemetry.update();
                //telemetry2.update();
            }


            mecanumWheels.stop();
            drop.stop();
            snagger.stop();
            intake.stop();
        }
    }
}