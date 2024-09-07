package oldcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class TopArm {
    private LinearOpMode currentOpMode;
    private Servo arm;

    private final double dropPosition =0.4;
    private final double intakePosition = 0.68;
    private final double transferPosition = 0.69;
    private final double intermediate = 0.65;

    public TopArm(LinearOpMode opMode){

        currentOpMode = opMode;
        arm = opMode.hardwareMap.get(Servo.class, "ARM");

    }

    public void setPosition(double position){

        arm.setPosition(position);
    }
    public void goToDropPosition(){
        setPosition(dropPosition);
        currentOpMode.telemetry.addData("TopArm: Going to drop position", dropPosition);
    }
    public void goToIntakePosition(){
        setPosition(intakePosition);
        currentOpMode.telemetry.addData("TopArm: Going to intake position", intakePosition);
    }
    public void goToIntermediate(){
        setPosition(intermediate);
        currentOpMode.telemetry.addData("TopArm: Going to intake position", intermediate);
    }
    public void goToTransfer(){
        setPosition(transferPosition);
        currentOpMode.telemetry.addData("TopArm: Going to intake position", transferPosition);
    }
}