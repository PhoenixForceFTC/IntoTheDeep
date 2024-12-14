package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.sun.tools.javac.jvm.ClassWriter;

import org.firstinspires.ftc.teamcode.util.ButtonToggle;


public class twoDOF {

    public Servo roll;
    public Servo pitch;
    public Servo grip;

    //public ButtonToggle downPosToggle = new ButtonToggle(false);
    LinearOpMode opMode;
    /*
      Roll Positions:

      Position 0: Rolled Right
      Position 1: Middle (Normal Grabbing Position)
      Position 2: Rolled Left
     */
    double ROLL_POSITIONS[] = {0.1997, 0.54,0.54, 0.87};
    /*
    Pitch Positions:

    Position 0: Claw facing fully up
    Position 1: Claw in middle
    Position 2: Claw Facing fully down
     */
    double PITCH_POSITIONS[] = {0, 0.5, 1};
    private ButtonToggle clawClosed;

    //ignore
    double ROLL_POSITIONS_AUTO[] = {0.073, 0.073, 0.073, 0.073, 0.073, 0.073, 0.073, 0.073, 0.073, 0.073,};
    double PITCH_POSITIONS_AUTO[] = {0.9, 0.46, 0.9, 0.51, 0.9, 0.52, 0.9, 0.56, 0.9, 0.55};

    //init servos
    public twoDOF(LinearOpMode opMode) {
        this.opMode = opMode;
        clawClosed = new ButtonToggle();
        roll = this.opMode.hardwareMap.get(Servo.class, "ROLL");
        grip = this.opMode.hardwareMap.get(Servo.class, "CLAW");
        pitch = this.opMode.hardwareMap.get(Servo.class, "PITCH");

        //  resetPosition();
    }

    public void SUBGRAB() {
      roll.setPosition(0.54);
      pitch.setPosition(1);
        // gripper will be released manually.
    }

    public void DROP() {
        roll.setPosition(0.54);
        pitch.setPosition(0.5);
    }

    public void gotoPosition(int pos) {
        double[] currentRollPositions = ROLL_POSITIONS;
        double[] currentPitchPositions = PITCH_POSITIONS;
        roll.setPosition(currentRollPositions[pos]);
        pitch.setPosition(currentPitchPositions[pos]);
    }

    public void control2DOF() {

        if (opMode.gamepad2.dpad_down) {
            roll.setPosition(0.54);
            pitch.setPosition(1);
        }
        else if (opMode.gamepad2.dpad_up) {
            roll.setPosition(0.54);
            pitch.setPosition(0.5);
        }

    }

    public void ControlCLAW()
    {
        clawClosed.update(opMode.gamepad2.b);
        setClawClosed(clawClosed.isActive());
    }
    public boolean isClosed() {
        return clawClosed.isActive();
    }
    public void setClawClosed(boolean closed)
    {
        if(closed)
        {
            grip.setPosition(0.5);
        }else {
            grip.setPosition(0.13);
        }
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

}