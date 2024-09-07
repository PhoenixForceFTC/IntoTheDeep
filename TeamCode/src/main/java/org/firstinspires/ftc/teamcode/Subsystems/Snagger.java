package org.firstinspires.ftc.teamcode.Subsystems;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;



public class Snagger {
    private DcMotorEx leftLift;
    private DcMotorEx rightLift;


    private double powerMultiplierL;
    private double powerMultiplierR;
    private LinearOpMode opMode;
//  int LIFT_POSITIONS[] = {0,1033,2066,2900,3100};
//  int LIFT_POSITIONS[] = {0,0,1200,1300,1450};
    //int LIFT_POSITIONS[] = {0,800,900,1400,1470,1500};
    int LIFT_POSITIONS[] = {0,740,825,750,900,1050,50}; /// 1130 5

    //825 is center position
    //650 left and right
    //960 is stack1
    //900 is backup from stack1


    // 0 = Retracted
    // 1 = Right Pos for spike mark deploy
    // 2 = Center Turn spike mark deploy
    // 3 = Full out prior to speed change
    // 4 = Full out
    int liftPosition = 0;

    boolean dPadPressed = false;
    private boolean prePressed = false;

    private boolean previouslyReset = true;
    private boolean runManually = false;
    private final int safetyRange = 100;
    public Snagger(LinearOpMode opMode){
        this.opMode = opMode;
        leftLift = this.opMode.hardwareMap.get(DcMotorEx.class, "LS");
        rightLift = this.opMode.hardwareMap.get(DcMotorEx.class, "RS");
        leftLift.setDirection(DcMotor.Direction.FORWARD);
        rightLift.setDirection(DcMotor.Direction.REVERSE);
        leftLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        goToPosition(0);
        runManually = false;
    }

    //magnitude comes from a gampad stick for use in teleop
    public void move(double magnitude){
        if(!runManually){
            leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        }

        if(runManually){
            if (magnitude > 0.2 || magnitude < -0.2) {
                opMode.telemetry.addLine("Drop is in manual modde");
                leftLift.setPower(-magnitude);
                rightLift.setPower(-magnitude);
            }else{
                leftLift.setPower(0);
                rightLift.setPower(0);
            }
        }
        opMode.telemetry.addData("Snagger slide position", "left lift:"+leftLift.getCurrentPosition());
        opMode.telemetry.addData("snagger slide position", "right lift"+rightLift.getCurrentPosition());
        runManually=true;
    }
    public void resetEncoders(){
        leftLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void goToPosition(int pos) {
        if (pos == 0) {
            goToPositionAfter(pos);
        } else if (pos == 1) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            goToPositionAfter(pos);
                        }
                    },
                    1000 // Delay in milliseconds
            );
        }
        else {
            goToPositionAfter(pos);
        }
    }

    public void goToPosition(int pos, double speed) {
        if (pos == 0) {
            goToPositionAfter(pos);
        } else if (pos == 1) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            goToPositionAfter(pos);
                        }
                    },
                    1000 // Delay in milliseconds
            );
        }
        else {
            goToPositionAfter(pos);
        }
    }

    public void controlLift2() {
        if (opMode.gamepad2.dpad_left) {
            runManually = false;
            liftPosition=0;
            goToPosition(liftPosition);
        }

    }

    private void goToPositionAfter(int pos)
    {
        goToPositionAfter(pos, 1.0);
    }

    private void goToPositionAfter(int pos, double speed){
        leftLift.setTargetPosition(LIFT_POSITIONS[pos]);
        rightLift.setTargetPosition(LIFT_POSITIONS[pos]);
        leftLift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        rightLift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        boolean setToBottom = pos == 0;
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (rightLift.getCurrentPosition() > safetyRange || !setToBottom || leftLift.getCurrent(CurrentUnit.AMPS)>4) {
            if(leftLift.getCurrent(CurrentUnit.AMPS)>5) {
                powerMultiplierL=5*(1/leftLift.getCurrent(CurrentUnit.AMPS));
            } else {
                powerMultiplierL=1;
            }
            if(rightLift.getCurrent(CurrentUnit.AMPS)>5) {
                powerMultiplierR=5*(1/rightLift.getCurrent(CurrentUnit.AMPS));
            } else {
                powerMultiplierR=1;
            }
            leftLift.setPower(speed);
            rightLift.setPower(speed);
        } else {
            leftLift.setPower(speed);
            rightLift.setPower(speed);
        }
        opMode.telemetry.addData("snagger slide amps", "left lift:"+leftLift.getCurrent(CurrentUnit.AMPS));
        opMode.telemetry.addData("snagger slide amps", "right lift"+rightLift.getCurrent(CurrentUnit.AMPS));
        opMode.telemetry.addData("snagger slide position", "left lift:"+leftLift.getCurrentPosition());
        opMode.telemetry.addData("snagger slide position", "right lift"+rightLift.getCurrentPosition());
        previouslyReset = false;
    }

    public boolean reachedTarget()
    {
        int diff = Math.abs(leftLift.getCurrentPosition()-leftLift.getTargetPosition());
        if(diff<safetyRange/2)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void resetPosition(){
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public int getPos(){
        return liftPosition;
    }
    public double getTicks(){return leftLift.getCurrentPosition();}
    public void stop() {
        goToPosition(0);
    }

}