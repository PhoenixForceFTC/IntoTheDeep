package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class NewLift {
    private DcMotorEx rightLift;
    private LinearOpMode opMode;

    int LIFT_POSITIONS[] = {0,800};
    int liftPosition = 0;

    boolean dPadPressed = false;

    public NewLift(LinearOpMode opMode){
        this.opMode = opMode;
        rightLift = this.opMode.hardwareMap.get(DcMotorEx.class, "LIFT");
        rightLift.setDirection(DcMotor.Direction.FORWARD);
        rightLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }
    public void liftSleep(){

        while (rightLift.isBusy()) {
            opMode.sleep(100);
        }
    }
    public void controlLift(boolean canLift) {
        if (opMode.gamepad2.right_stick_y > 0.5) {
            if (liftPosition < LIFT_POSITIONS.length - 1 && !dPadPressed && canLift) {
                liftPosition++;
            }

            dPadPressed = true;
        } else if (opMode.gamepad2.right_stick_y < -0.5) {
            if (liftPosition > 0 && !dPadPressed) {
                liftPosition--;
            }

            dPadPressed = true;
        } else {
            dPadPressed = false;
        }

        goToPosition(liftPosition);
    }


        public void goToPosition(int pos){
            rightLift.setTargetPosition(LIFT_POSITIONS[pos]);
            rightLift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            boolean setToBottom = LIFT_POSITIONS[pos] == 0;

            if (setToBottom) {
                rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            } else {
                rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            if (rightLift.getCurrentPosition() > 10 || !setToBottom) {
                rightLift.setPower(0.2);
            } else {
                rightLift.setPower(0);
            }
        }
    public boolean reachedTarget()
    {
        int diff = Math.abs(rightLift.getCurrentPosition()-rightLift.getTargetPosition());
        if(diff<5)
        {
            return true;

        }
        else
        {
            return false;
        }
    }

        public void goToHighJunction(){
            //setPower(1);
            liftPosition = 1;
        }

        public void goToPickupPosition(){
            //setPower(0);
            liftPosition = 0;
        }

        public void stop() {
            liftPosition=0;
        }
    }
