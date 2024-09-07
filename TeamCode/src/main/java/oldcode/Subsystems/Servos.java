package oldcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


public class Servos {
    private LinearOpMode currentOpMode;
    private Servo rollServo;
    private Servo gripperServo;
    private Servo pitchServo;
    public Servos (LinearOpMode opMode){
        currentOpMode = opMode;
        rollServo = opMode.hardwareMap.get(Servo.class, "s1");
        gripperServo = opMode.hardwareMap.get(Servo.class, "s2");
        pitchServo = opMode.hardwareMap.get(Servo.class, "s3");
    }
    public void openGripper (){
        gripperServo.setPosition(0);
    }
    public void closeGripper (){
        gripperServo.setPosition(0.5);
    }
    public void pickupConePosition (){
        pitchServo.setPosition(0.5);
        rollServo.setPosition(0);

    }
}
