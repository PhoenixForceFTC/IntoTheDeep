package oldcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import oldcode.OpModes.TeleOpMode;
import org.firstinspires.ftc.teamcode.util.ButtonToggle;

public class Swinch {
    private OpMode opMode;
    private final double closedPos = 0.55;
    private final double openPos = 1;
    private Servo gate;
    private Drop dropSlides;

    private TeleOpMode teleOp;
    //public ClawSensor sensor;

    private boolean canAutoClose = false;
    private ButtonToggle clawClosed;

    public Swinch(OpMode opMode) {
        this.opMode = opMode;
        gate = opMode.hardwareMap.get(Servo.class, "SWINCH");
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
        }
        else{
            gate.setPosition(closedPos);
        }

    }
}
