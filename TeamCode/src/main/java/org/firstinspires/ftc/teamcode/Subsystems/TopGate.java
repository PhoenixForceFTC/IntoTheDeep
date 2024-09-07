package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.util.ButtonToggle;
// This is to control the delivery of the pixels
//
public class TopGate {
    private OpMode opMode;
    private final double closedPos = 1;
    private final double openPos = -1;
    private final double stoppedPos = 0;
    private CRServo gate;
    private Drop dropSlides;
    //public ClawSensor sensor;

    private boolean canAutoClose = false;
    private boolean gateClosed = true ;
    public TopGate(OpMode opMode, Drop drop) {
        this.opMode = opMode;
        dropSlides = drop;
        gate = opMode.hardwareMap.get(CRServo.class, "GATE2");
        //grip.setDirection(Servo.Direction.REVERSE);
        //sensor = new ClawSensor(opMode.hardwareMap);


    }

    public void controlGate() {
        //if(dropSlides.reachedTarget() && dropSlides.getTicks()>200) {
            gateClosed = !opMode.gamepad2.right_bumper;
        //}


        if(gateClosed){
            //setGateClosed();
        }
        else{
            setGateOpen();
        }


        opMode.telemetry.addData("gate open", !isClosed());
        opMode.telemetry.addData("gate auto close", canAutoClose);

    }

    public boolean isClosed() {
        return gateClosed;
    }

    public void setGateOpen() {
        if(dropSlides.getTicks()>200){
            gate.setPower(openPos);
            opMode.telemetry.addLine("ran gate");
        }
        opMode.telemetry.addData("Opening Top Gate", String.valueOf(openPos));
        opMode.telemetry.addData(" Ticks",dropSlides.getTicks());
    }
    public void setGateClosed() {
        gate.setPower(closedPos);
        opMode.telemetry.addLine("Closing Top Gate");
    }
    public void setGateStopped() {
        gate.setPower(stoppedPos);
        opMode.telemetry.addLine("Stopping Top Gate");
    }
}
