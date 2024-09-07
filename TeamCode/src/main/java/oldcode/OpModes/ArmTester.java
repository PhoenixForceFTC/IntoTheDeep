package oldcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import oldcode.Subsystems.Claw;
import oldcode.Subsystems.ElbowArm;
import oldcode.Subsystems.MecanumWheels;
import oldcode.Subsystems.NewLift;
import org.firstinspires.ftc.teamcode.util.ButtonToggle;

/**
 * FTC WIRES TeleOp Example
 *
 */
@TeleOp(name = "Armtester", group = "00-Teleop")
public class ArmTester extends LinearOpMode {


    private MecanumWheels mecanumWheels;
    //private NewLift newLift;
    private ElbowArm elbowArm;
    public Servo Roll;
    public Servo Pitch;
    public Claw claw;
    private NewLift newLift;
    private ButtonToggle highButton;
   // @Override
    /*
     * Constructor for passing all the subsystems in order to make the subsystem be able to use
     * and work/be active
    */
    public void runOpMode() throws InterruptedException {
    /* Create Subsystem Objects*/
    // driveTrain = new DriveTrain(hardwareMap);

        highButton = new ButtonToggle();
        mecanumWheels = new MecanumWheels(this );
        elbowArm = new ElbowArm(this);
        claw = new Claw(this);
        Roll = hardwareMap.get(Servo.class, "s1");
        newLift = new NewLift(this);
        Pitch = hardwareMap.get(Servo.class, "s3");
        int lifterTarget = 0;


        //telemetry.clearAll();
        //telemetry.addData("Running TeleOp","10100");
        telemetry.update();
        /* Wait for Start or Stop Button to be pressed */

        waitForStart();
        //elbowArm.resetPosition();


        boolean xpressed;
        // Gamepad 1
        // - Left stick -
        // - RIght stick -
        // - DPad up - Elbow arm up (to drop cones)
        // - DPad down - Elbow arm down (to pick up cones)
        //
        //
        /*If Start is pressed, enter loop and exit only when Stop is pressed */
        while (!isStopRequested()) {
            while (opModeIsActive()) {
                mecanumWheels.move(gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x);

                elbowArm.testRollServo();
                elbowArm.testShittyPitch();
                elbowArm.testElbowArm();
                newLift.controlLift(true);
                claw.controlClaw();
                /*telemetry.addData("Status: ","Position: "+ elbowArm.getElbowPosition());
                telemetry.addData("Roll pos:",+ Roll.getPosition() +"");
                telemetry.addData("Pitch pos:",+ Pitch.getPosition() +"");*/
                        telemetry.update();
            }

                //elbowArm.adjPosPosi((gamepad2.x));
                //elbowArm.adjPosNeg((gamepad2.b));
            }

        }
    }

