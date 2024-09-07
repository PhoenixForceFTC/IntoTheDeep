package oldcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
public class SlidesServo {
    private LinearOpMode currentOpMode;
    private Servo cRServo1;
    private Servo cRServo2;
    public SlidesServo(LinearOpMode opMode){
        currentOpMode = opMode;
        cRServo1 = opMode.hardwareMap.get(Servo.class, "SS1");
        cRServo2 = opMode.hardwareMap.get(Servo.class, "SS2");
    }
    public void setPosition(float position){
        cRServo1.setPosition(position);
        cRServo2.setPosition(position);
    }
    public void forward(){setPosition(1);}
    public void stop(){setPosition(0);}
}
