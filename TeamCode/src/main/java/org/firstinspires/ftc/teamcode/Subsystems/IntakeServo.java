package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;


public class IntakeServo {
    private OpMode currentOpMode;
    private CRServo cRServo1; //--- Middle Wheel
    private CRServo cRServo2; //--- Front Intake Wheel
    private CRServo cRServo3; //--- Bottom Roller


    public IntakeServo(OpMode opMode){
        currentOpMode = opMode;
        cRServo1 = opMode.hardwareMap.get(CRServo.class, "CRS1");
        cRServo2 = opMode.hardwareMap.get(CRServo.class, "CRS2");
        cRServo3 = opMode.hardwareMap.get(CRServo.class, "CRS3");
    }

    public void setPower(double position){
        cRServo1.setPower(-position); //--- Middle Wheel
        cRServo2.setPower(-position); //--- Front Intake Wheel
        cRServo3.setPower(-position); //--- Bottom Roller
        currentOpMode.telemetry.addData("Intake Servo Power:",position);
    }

    public void setPower2(double position){
        cRServo2.setPower(-position);
    }

    public void frontWheelReverse() { setPower2(1); } //--- switched direction
    public void forward() { setPower(-1); } //--- switched direction
    public void transfer() { setPower(-1); } //--- switched direction
    public void backward() { setPower(0.4); } //--- switched direction
    public void backward2() { setPower(1); }  //--- switched direction
    public void stop() { setPower(0);}
}