package oldcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.ButtonToggle;

public class Flip {
    private OpMode opMode;

    private Servo grip;
    //public ClawSensor sensor;

    private boolean canAutoClose = false;
    private ButtonToggle clawFlipped;

    public Flip(OpMode opMode) {
        this.opMode = opMode;

        grip = opMode.hardwareMap.get(Servo.class, "FLIP");
        //grip.setDirection(Servo.Direction.REVERSE);
        //sensor = new ClawSensor(opMode.hardwareMap);

        clawFlipped = new ButtonToggle();
    }

    public void controlClaw() {
        clawFlipped.update(opMode.gamepad2.y);

        /*if (sensor.conePresent()) {
            if (canAutoClose) {
                clawClosed.setActive(true);
                canAutoClose = false;
            }
        } else {
            canAutoClose = true;
        }*/

        setClawClosed(clawFlipped.isActive());

        //opMode.telemetry.addData("Claw sensor distance", sensor.getDistance());
        opMode.telemetry.addData("Claw open", !isClosed());
      //  opMode.telemetry.addData("Can auto close", canAutoClose);
        //opMode.telemetry.addData("Is cone detected", sensor.conePresent());
        opMode.telemetry.update();
    }

    public boolean isClosed() {
        return clawFlipped.isActive();
    }

    public void setClawClosed(boolean flipped) {
        if (flipped) {
            grip.setPosition(0.85);
        } else {
            grip.setPosition(0.4);
        }
    }
}

