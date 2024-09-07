package oldcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import oldcode.Subsystems.MecanumWheels;

/**
 * FTC WIRES TeleOp Example
 *
 */
@TeleOp(name = "mecanum wheels only", group = "00-Teleop")

public class MecanumDriveTeleOp extends LinearOpMode {

    private OpMode opMode;
    private MecanumWheels mecanumWheels;

    //    private MotorEncoder motorEncoder;
    //private IntakeServo axonServo;
    private boolean isBPressed;





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
        mecanumWheels = new MecanumWheels(this);
        waitForStart();


        while (!isStopRequested()) {
            while (opModeIsActive()) {
                //     sensor.ConePresent();


                mecanumWheels.move();

                telemetry.update();
                //telemetry2.update();
            }


            mecanumWheels.stop();
        }
    }
}