package oldcode.Subsystems;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


public class PixelCounter {
    private LinearOpMode opMode;
    NormalizedColorSensor dropClrFront;
    NormalizedColorSensor dropClrBack;
    NormalizedColorSensor intakeClrFront;
    NormalizedColorSensor intakeClrBack;
    public PixelCounter(LinearOpMode opMode) {
        this.opMode = opMode;
        //dropClrFront = opMode.hardwareMap.get(NormalizedColorSensor.class, "dropClrFront");
        //dropClrBack = opMode.hardwareMap.get(NormalizedColorSensor.class, "dropClrBack");
        intakeClrFront = opMode.hardwareMap.get(NormalizedColorSensor.class, "intakeClrFront");
        intakeClrBack = opMode.hardwareMap.get(NormalizedColorSensor.class, "intakeClrBack");
    }
    // This method returns a integer telling how many pixels are in the intake system currently
    // call this all the time so we constantly know

/*    public int getDropPixels() {
        int pixels = 0;
        if (((DistanceSensor) dropClrFront).getDistance(DistanceUnit.CM) < 3) {
            pixels = pixels + 1;
        }
        if (((DistanceSensor) dropClrBack).getDistance(DistanceUnit.CM) < 3) {
            pixels = pixels + 1;
        }
        return pixels;
    }*/

    public void telemetry() {
        //opMode.telemetry.addData("drop front",((DistanceSensor) dropClrFront).getDistance(DistanceUnit.CM));
        //opMode.telemetry.addData("drop back",((DistanceSensor) dropClrBack).getDistance(DistanceUnit.CM));
        opMode.telemetry.addData("intake front",((DistanceSensor) intakeClrFront).getDistance(DistanceUnit.CM));
        opMode.telemetry.addData("intake back",((DistanceSensor) intakeClrBack).getDistance(DistanceUnit.CM));
        //opMode.telemetry.addData("drop pixels",getDropPixels());
        opMode.telemetry.addData("intake pixels",getIntakePixels());

    }
    public int getIntakePixels() {
        int pixels = 0;
        if (((DistanceSensor) intakeClrFront).getDistance(DistanceUnit.CM) < 1) {
            pixels = pixels + 1;
        }

        if (((DistanceSensor) intakeClrBack).getDistance(DistanceUnit.CM) < 1) {
            pixels = pixels + 1;
        }
        return pixels;
    }

}
