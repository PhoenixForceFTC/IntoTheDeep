package oldcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.ButtonToggle;


public class ElbowArm {

    public Servo roll;
    public Servo pitch;
    public Servo grip;

    public ButtonToggle downPosToggle = new ButtonToggle(false);

    DcMotorEx elbowArm;
    LinearOpMode opMode;
    /*
      Position 0 is starting position.
      Position 1 is grabbing position.
      Position 2 is intermediate to rotate wrist
      Position 3+4 is for dropping high junction
      Position 5 is for dropping medium junction.
      position 6 - low junction
     */
    int lowElbow = 3100;
    int ELBOW_POSITIONS[] = {0,0,1750,2560,3100};
    double ROLL_POSITIONS[] = {0.050,0.050,0.050,0.724,0.724};
    double PITCH_POSITIONS[] = {0.9,0.57,0.57,0.369,0.42};

    int ELBOW_POSITIONS_STACK[] = {482,482,350,350,280,280,121,121,30,30};
    double ROLL_POSITIONS_STACK[] = {0.059,0.059,0.059,0.059,0.059,0.059,0.059,0.059,0.059,0.059,};
    double PITCH_POSITIONS_STACK[] = {0.9,0.49,0.9,0.539,0.9,0.554,0.9,0.58,0.9,0.57};

    int ELBOW_POSITIONS_AUTO[] = {482,482,350,350,280,280,121,121,30,30};
    double ROLL_POSITIONS_AUTO[] = {0.073,0.073,0.073,0.073,0.073,0.073,0.073,0.073,0.073,0.073,};
    double PITCH_POSITIONS_AUTO[] = {0.9,0.46,0.9,0.51,0.9,0.52,0.9,0.56,0.9,0.55};

    int lifterTarget = 0;
    int elbowPosition = 0;
    boolean dPadPressed = false;

    boolean isReadyToLift = false;

    ButtonToggle stackingToggle = new ButtonToggle();
    boolean preIsStacking = false;

    ButtonToggle manualToggle = new ButtonToggle();
    boolean preManualMode = false;

    //initializes the elbow arm motor.
    public ElbowArm(LinearOpMode opMode) {
        this.opMode = opMode;

        elbowArm = this.opMode.hardwareMap.get(DcMotorEx.class, "eA");
        //elbowArm.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        elbowArm.setDirection(DcMotorSimple.Direction.REVERSE);
        //setElbowPosition(0);

        elbowArm.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        elbowArm.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        elbowArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        roll = this.opMode.hardwareMap.get(Servo.class, "s1");
        grip = this.opMode.hardwareMap.get(Servo.class, "s2");
        pitch = this.opMode.hardwareMap.get(Servo.class, "s3");// rishi
        pitch.setDirection(Servo.Direction.REVERSE);
      //  resetPosition();
    }

    public void dropOnHigh(){
        elbowPosition = 3;
        gotoPosition(elbowPosition);
        // gripper will be released manually.
    }

    public void dropOnMedium(){
        elbowPosition = 2;
        gotoPosition(elbowPosition);
        elbowPosition = 3;
        gotoPosition(elbowPosition);
    }
    public void dropOnLow(){
        elbowPosition = 2;
        gotoPosition(elbowPosition);

        gotoPosition(elbowPosition);
    }

    public boolean reachedTarget()
    {
        int diff = Math.abs(elbowArm.getCurrentPosition()-elbowArm.getTargetPosition());
        if(diff<10)
        {
            return true;

        }
        else
        {
            return false;
        }
    }

    // once the cone is released (button b)
    public void beReadyToPickup(){
        elbowPosition = 0;
        gotoPosition(elbowPosition);
        elbowPosition = 2;
        gotoPosition(elbowPosition);

    }

    public void beReadyToPickupAuto(){
        elbowPosition = 0;
        gotoPositionAuto(elbowPosition);

        elbowPosition = 2;
        gotoPositionAuto(elbowPosition);
    }

    public void gotoPosition(int pos){
        boolean isStacking = stackingToggle.isActive();

        int[] currentElbowPositions = isStacking ? ELBOW_POSITIONS_STACK : ELBOW_POSITIONS;
        double[] currentRollPositions = isStacking ? ROLL_POSITIONS_STACK : ROLL_POSITIONS;
        double[] currentPitchPositions = isStacking ? PITCH_POSITIONS_STACK : PITCH_POSITIONS;

        elbowArm.setTargetPosition(currentElbowPositions[pos]);
        elbowArm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        elbowArm.setPower(1.0);
        roll.setPosition(currentRollPositions[pos]);
        pitch.setPosition(currentPitchPositions[pos]);

        isReadyToLift = currentElbowPositions[elbowPosition] > 0;

        // opMode.telemetry.addData(" ","Elbow Position: "+ elbowArm.getCurrentPosition() + " " +
        // "pos : " + pos + " ");

    }

    public boolean isStacking() {
        return stackingToggle.isActive();
    }

    public boolean isReadyToLift() {
        return isReadyToLift;
    }

    public void gotoPositionTest(int lifterTarget){

        elbowArm.setTargetPosition(lifterTarget);
        elbowArm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        elbowArm.setPower(1.0);
       // opMode.telemetry.addData("Target",""+ lifterTarget);
       // opMode.telemetry.update();
    }


    public void gotoPositionAuto(int pos){

        elbowArm.setTargetPosition(ELBOW_POSITIONS_AUTO[pos]);
        elbowArm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        elbowArm.setPower(1.0);
        roll.setPosition(ROLL_POSITIONS_AUTO[pos]);
        pitch.setPosition(PITCH_POSITIONS_AUTO[pos]);
        /*opMode.telemetry.addData(" ","Elbow Position: "+ elbowArm.getCurrentPosition() + " " +
                "pos : " + pos + " ");*/
        //opMode.telemetry.update();
    }

    public void controlElbowArm() {
        boolean isManaulMode = manualToggle.update(opMode.gamepad2.left_stick_button);
        if (isManaulMode != preManualMode) {
            preManualMode = isManaulMode;

            if (!isManaulMode) {
                elbowArm.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
                elbowPosition = 0;
            }
        }

        if (isManaulMode) {
            roll.setPosition(ROLL_POSITIONS[0]);
            pitch.setPosition(PITCH_POSITIONS[0]);
            elbowArm.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
            elbowArm.setPower(-opMode.gamepad2.left_stick_y);
            return;
        }

        boolean isStacking = stackingToggle.update(opMode.gamepad2.left_bumper);
        if (isStacking != preIsStacking) {
            elbowPosition = 0;
            preIsStacking = isStacking;
        }

        if (isStacking) {
            downPosToggle.update(opMode.gamepad2.a);
        } else {
            downPosToggle.setActive(false);
        }

        int[] currentElbowPositions = isStacking ? ELBOW_POSITIONS_STACK : ELBOW_POSITIONS;

        if (opMode.gamepad2.dpad_up) {
            if (!dPadPressed) {
                if (isStacking && elbowPosition < currentElbowPositions.length - 2) {
                    elbowPosition += 2;
                } else if (!isStacking && elbowPosition < currentElbowPositions.length - 1) {
                    elbowPosition++;
                }
            }

            dPadPressed = true;
        } else if (opMode.gamepad2.dpad_down) {
            if (elbowPosition > 0 && !dPadPressed) {
                elbowPosition--;
                if (isStacking) {
                    elbowPosition--;
                }
            }

            dPadPressed = true;
        }
        else {
            dPadPressed = false;

        }

        int actuatedPosition = elbowPosition;
        if (downPosToggle.isActive()) {
            actuatedPosition++;
        }

        gotoPosition(actuatedPosition);
    }
    public void testElbowArm() {
        if (opMode.gamepad2.y) {
            lifterTarget+=10;
            opMode.sleep(1);
        } else if (opMode.gamepad2.x) {
            lifterTarget-=10;
            opMode.sleep(1);
        }
        gotoPositionTest(lifterTarget);
        //opMode.telemetry.addData("Status: ", "Position: " + getElbowPosition());
      //  opMode.telemetry.update();
    }

    double shittyRoll = 0.080;
    double shittyPitch = 0.9;

    public void testRollServo() {
        if (opMode.gamepad2.dpad_up) {
            shittyRoll += 0.01;
            opMode.sleep(50);
        } else if (opMode.gamepad2.dpad_down) {
            shittyRoll -= 0.01;
            opMode.sleep(50);
        }
        roll.setPosition(shittyRoll);
        //opMode.telemetry.addData("Roll pos:",+ roll.getPosition() +"");
        //opMode.telemetry.update();
    }
    public void testShittyPitch() {
        if (opMode.gamepad2.dpad_right) {
            shittyPitch += 0.01;
            opMode.sleep(50);
        } else if (opMode.gamepad2.dpad_left) {
            shittyPitch -= 0.01;
            opMode.sleep(50);
        }
        pitch.setPosition(shittyPitch);
        //opMode.telemetry.addData("Pitch pos:",+ pitch.getPosition() +"");
       // opMode.telemetry.update();
    }

    public void setElbowPosition(int position) {
        elbowPosition = position;
    }

    public int getElbowPosition() {
        return elbowArm.getCurrentPosition();
    }
}