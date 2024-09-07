package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.OpModes.TeleOpMode;

import org.firstinspires.ftc.teamcode.util.ButtonToggle;

public class BottomGate {
    private OpMode opMode;
    private final double closedPos = 0.6;
    private final double openPos = 0.8;
    private Servo gate;
    private Drop dropSlides;

    private TeleOpMode teleOp;
    //public ClawSensor sensor;

    private boolean canAutoClose = false;
    private ButtonToggle clawClosed;

    public BottomGate(LinearOpMode opMode, Drop drop) {
        this.opMode = opMode;
        this.dropSlides = drop;
        gate = opMode.hardwareMap.get(Servo.class, "GATE1");
        //grip.setDirection(Servo.Direction.REVERSE);
        //sensor = new ClawSensor(opMode.hardwareMap);

        clawClosed = new ButtonToggle();


    }

    public void controlClaw() {
        if (dropSlides.reachedTarget() && dropSlides.getPos()==0) {
            clawClosed.update(opMode.gamepad2.dpad_down);
        }


        setClawClosed(clawClosed.isActive());
        opMode.telemetry.addData("gate open", !isClosed());
        opMode.telemetry.addData("gate auto close", canAutoClose);
    }

    public boolean isClosed() {
        return clawClosed.isActive();
    }

    public void setClawClosed(boolean closed) {
        if(!closed){

            gate.setPosition(openPos);
           if((gate.getPosition()>openPos)/*checks if the position of the gate is more than the position of the open position which now is 0.8*/||(gate.getPosition()<closedPos-0.1)/*checks if the position of the gate is less than the position of the closed position which now is 0.5*/){
                gate.setPosition(0.1);/*sets the gate to its closed position*/
            }
        }
        else{
            gate.setPosition(closedPos);
        }

    }
}
