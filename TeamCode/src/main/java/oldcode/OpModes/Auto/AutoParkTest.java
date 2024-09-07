package oldcode.OpModes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Config
@Disabled
@Autonomous()
public class AutoParkTest extends AutoOpMode {
    public static Position START = new Position(-37, -57, 90);
    public static Position MOVE_ONE = new Position(-1, -1, 0);
    public static Position PARK_TWO = new Position(-57, -10, 0);
    @Override
    public void runOpMode() {
        setup(START);


     //   goTo(MOVE_ONE);
        goTo(PARK_TWO);
        //
        // splineTo(MOVE_ONE);
        // turn(degrees);
    }
}
