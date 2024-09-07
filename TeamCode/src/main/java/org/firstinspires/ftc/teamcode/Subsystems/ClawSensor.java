package org.firstinspires.ftc.teamcode.Subsystems;

//import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.MovingAverage;

//@Config
public class ClawSensor {
    public DistanceSensor distanceSensor;
    private MovingAverage average;

    public final static double CLOSE_DISTANCE = 15;

    public ClawSensor(HardwareMap map) {
        distanceSensor = (DistanceSensor) map.get(NormalizedColorSensor.class, "clr");
        average = new MovingAverage(10);
    }

    public void ConePresent() {
        double distance = distanceSensor.getDistance(DistanceUnit.CM);
        if (Double.isNaN(distance))
        { // --- Moving average doesn't like NaN, so set to far away value
            distance = 100.0;
        }
        average.addData(distance);

    }

    public double getDistance() {
        return average.getMean();
    }
}
