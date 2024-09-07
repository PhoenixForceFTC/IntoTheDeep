package oldcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import oldcode.Subsystems.Drop;
import oldcode.Subsystems.Intake;
import oldcode.Subsystems.MecanumWheels;
import oldcode.Subsystems.PlaneServo;
import oldcode.Subsystems.Snagger;
import oldcode.Subsystems.TopGate;
import org.firstinspires.ftc.teamcode.util.ButtonToggle;

/**
 * FTC WIRES TeleOp Example
 *
 */
@TeleOp(name = "Presentaion Demo", group = "00-Teleop")



public class DemoTeleOpMode extends LinearOpMode {
    private OpMode opMode;
    private PlaneServo planeServo;
    private MecanumWheels mecanumWheels;
    private TopGate topGate;
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


    public void runOpMode() throws InterruptedException {

        /* Create Subsystem Objects*/
        // driveTrain = new DriveTrain(hardwareMap);
        //newLift2 = new NewLift2(this);
        drop = new Drop(this);
        snagger= new Snagger(this);
        topGate= new TopGate(this,drop);
        mecanumWheels = new MecanumWheels(this);
        intake = new Intake(this,drop,topGate);
        planeServo = new PlaneServo(this);

        waitForStart();


        while (!isStopRequested()) {
            while (opModeIsActive()) {

                if(gamepad1.y)
                {
                    //Extend Intake
                    snagger.move(-1);
                    sleep(1500);
                    snagger.stop();
                    intake.eatPixel();
                    sleep(2500);
                    intake.stop();
                }
                if(gamepad1.x)
                {
                    //Expand drop, drop pixel
                    drop.goToPosition(4);
                    sleep(3000);
                    topGate.setGateOpen();
                    sleep(1000);
                    topGate.setGateStopped();

                }
                if(gamepad1.b)
                {
                    snagger.move(1);
                    sleep(1500);
                    snagger.stop();
                    sleep(500);
                    drop.goToPosition(0);
                    sleep(1000);
                    drop.stop();
                }
                if(gamepad1.a)
                {
                    //Plane Launcher
                    planeServo.GoToPosition((float) 0.13);
                }
                else{
                    planeServo.GoToPosition(0);
                }
                if(gamepad1.right_bumper)
                {
                    //Transfer Pixel
                    intake.transferPixel();
                    sleep(1500);
                    intake.stop();
                }


                telemetry.update();
            }

            mecanumWheels.stop();
          drop.stop();
          snagger.stop();
          intake.stop();
        }
    }
}