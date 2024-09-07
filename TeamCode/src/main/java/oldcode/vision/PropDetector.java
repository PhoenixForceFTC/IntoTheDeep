package oldcode.vision;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import oldcode.OpModes.Auto.AbstractAutonomous.Case;
import oldcode.OpModes.Auto.AbstractAutonomous.Side;
import org.firstinspires.ftc.vision.VisionPortal;
import android.util.Size;




public class PropDetector {
    private VisionPortal portal;
    private ColorPropProcessor proc;
    public PropDetector(OpMode opMode, boolean right, Side side) {
        proc = new ColorPropProcessor(right, side);
        portal = new VisionPortal.Builder()
                .setCamera(opMode.hardwareMap.get(WebcamName.class, "camera"))
                .setCameraResolution(new Size(640, 480))
                .addProcessor(proc)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();
    }
    public void close() {
        portal.close();
    }

    public Case getCase() {
        return proc.getCase();
    }
    public ColorPropProcessor getProc(){return proc;}
}
