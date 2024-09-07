package oldcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config

public class Drop {
    private PIDController controller;
    public static double P =0.001,I=0,D=0;
    public static double F=0.01;
    private int target = 0;
    private static double ticksPerDegree=384.5/360;
    private DcMotorEx leftLift;
    private DcMotorEx rightLift;

    private double powerMultiplierL;
    private double powerMultiplierR;
    private LinearOpMode opMode;
    private TopArm arm;
    int LIFT_POSITIONS[] = {0,100,700,1000,1400,2100}; //3 was 1200
                           // 0 = Arm.Intake
                           // 1 = Arm.Intermediate
                           // 2+ = Arm.Drop
    int liftPosition = 0;

    boolean dPadPressed = false;
    private boolean isTransfering = false;

    private boolean previouslyReset = true;
    private boolean runManually = false;
    private final int safetyRange = 100;
    public Drop(LinearOpMode opMode){
        controller=new PIDController(P,I,D);
        this.opMode = opMode;
        leftLift = this.opMode.hardwareMap.get(DcMotorEx.class, "LD");
        rightLift = this.opMode.hardwareMap.get(DcMotorEx.class, "RD");
        leftLift.setDirection(DcMotor.Direction.REVERSE);
        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        runManually = false;
        arm = new TopArm(opMode);

    }
    public void move(double magnitude){
        boolean run = false;
        if(!runManually){
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        leftLift.setTargetPosition(leftLift.getCurrentPosition()+(int)(1000*magnitude));
        rightLift.setTargetPosition(rightLift.getCurrentPosition()+(int)(1000*magnitude));
        leftLift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        rightLift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        runManually=true;
        if (magnitude > 0.2 || magnitude < -0.2) {

            opMode.telemetry.addLine("Drop is in manual modde");


            leftLift.setPower(-magnitude);
            rightLift.setPower(-magnitude);
            target = rightLift.getCurrentPosition();
        }else{
            controller.setPID(P, I, D);
            double pid = controller.calculate(rightLift.getCurrentPosition(),target);
            double ff = Math.cos(Math.toRadians(target/ticksPerDegree))*F;
            double power = pid+ff;
            leftLift.setPower(power);
            rightLift.setPower(power);

        }
        opMode.telemetry.addData("pos: ",rightLift.getCurrentPosition());
        opMode.telemetry.addData("target: ",target);
        opMode.telemetry.addData("Drop slide position", "left lift:"+leftLift.getCurrentPosition());
        opMode.telemetry.addData("Drop slide position", "right lift"+rightLift.getCurrentPosition());
        if(!isTransfering){
            if(opMode.gamepad2.right_trigger>0.4){
                arm.goToIntermediate();
            } else{
                arm.goToDropPosition();
            }
        }
        isTransfering = false;
    }


    public void controlLift(boolean canLift) {

        if (opMode.gamepad2.right_bumper) {
            if (liftPosition < LIFT_POSITIONS.length - 1 && !dPadPressed && canLift) {
                liftPosition++;
            }

            dPadPressed = true;
        } else if (opMode.gamepad2.right_trigger > .2) {
            if (liftPosition > 0 && !dPadPressed) {
                liftPosition--;
            }

            dPadPressed = true;
        } else {
            dPadPressed = false;
        }

        goToPosition(liftPosition);
    }
    public void resetEncoders(){
        leftLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }
    public void controlLift2() {
        if(runManually){
            resetEncoders();
        }

        if (opMode.gamepad2.a) {
            liftPosition= 0;
            runManually = false;
            goToBottom();
        }
        if (opMode.gamepad2.x) {
            runManually = false;
            liftPosition= 2;
            goToPosition(liftPosition);

        }
        if (opMode.gamepad2.b) {
            runManually = false;
            liftPosition=4;
            goToPosition(liftPosition);

        }
        if (opMode.gamepad2.y) {
            runManually = false;
            liftPosition=5;
            goToPosition(liftPosition);
        }
        opMode.telemetry.addData("Drop slide amps", "left lift:"+leftLift.getCurrent(CurrentUnit.AMPS));
        opMode.telemetry.addData("Drop slide amps", "right lift"+rightLift.getCurrent(CurrentUnit.AMPS));
        opMode.telemetry.addData("Drop slide position", "left lift:"+leftLift.getCurrentPosition());
        opMode.telemetry.addData("Drop slide position", "right lift"+rightLift.getCurrentPosition());
        if((leftLift.getCurrent(CurrentUnit.AMPS)>4||rightLift.getCurrent(CurrentUnit.AMPS)>4)&&rightLift.getCurrentPosition()<150&&liftPosition==0){
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            resetPosition();
                            previouslyReset = true;
                        }
                    },
                    100 // Delay in milliseconds
            );
        }
    }

    private void goToBottomSecondPart() {
    }
    public void goToBottom() {

        Runnable myMethodWrapper = this::goToBottomSecondPart;

        if(getTicks()>LIFT_POSITIONS[1]+safetyRange) {
            liftPosition = 1;
            goToPosition(liftPosition);
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            liftPosition = 0;
                            goToPosition(liftPosition);
                        }
                    },
                    1000 // Delay in milliseconds
            );
        }

    }



    public void transfer(){
        arm.goToTransfer();
        isTransfering = true;
    }



    public void goToPosition(int pos) {
        if (pos == 0) {
            arm.goToIntakePosition();
            goToPositionAfter(pos);
        } else if (pos == 1){
            arm.goToIntermediate();
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
            arm.goToDropPosition();
            goToPositionAfter(pos);
        }

    }


    private void goToPositionAfter(int pos){
        leftLift.setTargetPosition(LIFT_POSITIONS[pos]);
        rightLift.setTargetPosition(LIFT_POSITIONS[pos]);
        leftLift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        rightLift.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        boolean setToBottom = pos == 0;

        if (setToBottom || rightLift.getCurrentPosition()<100) {
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        } else {
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        if (rightLift.getCurrentPosition() > safetyRange/2 || !setToBottom || leftLift.getCurrent(CurrentUnit.AMPS)>4) {

            if(leftLift.getCurrent(CurrentUnit.AMPS)>5){
                powerMultiplierL=5*(1/leftLift.getCurrent(CurrentUnit.AMPS));
            }else{
                powerMultiplierL=1;
            }
            if(rightLift.getCurrent(CurrentUnit.AMPS)>5){
                powerMultiplierR=5*(1/rightLift.getCurrent(CurrentUnit.AMPS));
            }else{
                powerMultiplierR=1;
            }

            leftLift.setPower(1.0);
            rightLift.setPower(1.0);
        } else {
            leftLift.setPower(0);
            rightLift.setPower(0);

        }
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

    public void moveToTop(){
        //setPower(1);
        liftPosition = 1;
    }

    public void moveToBottom(){
        //setPower(0);
        liftPosition = 0;
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
        goToBottom();
    }

}