package oldcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
@Disabled
public class CameraVision {
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;
    LinearOpMode currentOpMode;

    static final double FEET_PER_METER = 3.28084;

    //tag ids from the 36h11 family
    int LEFT_TAG = 1;
    int MID_TAG = 2;
    int RIGHT_TAG = 3;

    //in meters
    double tagsize = 0.166;

    AprilTagDetection tagOfInterest = null;

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    //construct the camera
    public CameraVision(LinearOpMode opMode) {
        currentOpMode = opMode;
        int cameraMonitorViewId = currentOpMode.hardwareMap.appContext.getResources().getIdentifier
                ("cameraMonitorViewId", "id", currentOpMode.hardwareMap.appContext.getPackageName());

        camera = OpenCvCameraFactory.getInstance().createWebcam(currentOpMode.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
    }

    public void listener(){
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });


    }

    //created to run before start is pressed
    public void detection(){
        ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

        if(currentDetections.size() != 0) {
            boolean tagFound = false;

            for(AprilTagDetection tag : currentDetections) {
                if(tag.id == LEFT_TAG || tag.id == RIGHT_TAG || tag.id == MID_TAG) {
                    tagOfInterest = tag;
                    tagFound = true;
                    break;
                }
            }

            if(tagFound) {
                currentOpMode.telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                tagToTelemetry(tagOfInterest);
            }
            else {
                currentOpMode.telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null) {
                    currentOpMode.telemetry.addLine("(The tag has never been seen)");
                }
                else {
                    currentOpMode.telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }
            }
        }
        else {
            currentOpMode.telemetry.addLine("Don't see tag of interest :(");

            if(tagOfInterest == null) {
                currentOpMode.telemetry.addLine("(The tag has never been seen)");
            }
            else {
                currentOpMode.telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                tagToTelemetry(tagOfInterest);
            }

        }

        currentOpMode.telemetry.update();
        //sleep(20); can't use because not a linear op mode anymore
    }

    void tagToTelemetry(AprilTagDetection detection) {
        currentOpMode.telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        currentOpMode.telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        currentOpMode.telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        currentOpMode.telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        //currentOpMode.telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        //currentOpMode.telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        //currentOpMode.telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }

    public int getTagOfInterest(){
        if (tagOfInterest != null){
            return(tagOfInterest.id);
        } else {
            //if no tag is detected, try the middle one
            return(2);
        }
    }
}
