package oldcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Belt {
    private DcMotorEx rightLift;
    private LinearOpMode opMode;

    public Belt(LinearOpMode opMode){
        this.opMode = opMode;
        rightLift = this.opMode.hardwareMap.get(DcMotorEx.class, "BELT");
        rightLift.setDirection(DcMotor.Direction.FORWARD);
        rightLift.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }
    public void liftSleep(){

        while (rightLift.isBusy()) {
            opMode.sleep(100);
        }
    }
    public void controlLift(boolean canLift) {

        if (canLift) {
            rightLift.setPower(opMode.gamepad2.left_stick_y/4);
        }

    }
    public boolean reachedTarget()
    {
        int diff = Math.abs(rightLift.getCurrentPosition()-rightLift.getTargetPosition());
        if(diff<5)
        {
            return true;

        }
        else
        {
            return false;
        }
    }


        public void stop() {

        }
    }
