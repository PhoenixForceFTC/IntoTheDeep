package oldcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


public class PlaneServo {
    private LinearOpMode currentOpMode;
    private Servo rollServo;
    public PlaneServo (LinearOpMode opMode){
        currentOpMode = opMode;
        rollServo = opMode.hardwareMap.get(Servo.class, "AIR");
    }

    public void GoToPosition (float position){
        rollServo.setPosition(position);
    }
}
