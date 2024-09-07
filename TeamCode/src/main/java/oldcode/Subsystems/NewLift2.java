package oldcode.Subsystems;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class NewLift2 {
    private DcMotorEx rightLift;
    private LinearOpMode opMode;

    public NewLift2(LinearOpMode opMode){
        this.opMode = opMode;
        rightLift = this.opMode.hardwareMap.get(DcMotorEx.class, "LIFT");
        rightLift.setDirection(DcMotor.Direction.FORWARD);
        rightLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

    }
    public void liftSleep(){

        while (rightLift.isBusy()) {
            opMode.sleep(100);
        }
    }
    public void controlLift(boolean canLift) {

        if (canLift) {
            rightLift.setPower(opMode.gamepad2.right_stick_y);
                rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
        rightLift.setPower(0);
    }
}
