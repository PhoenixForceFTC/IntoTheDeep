package oldcode.OpModes.Auto.RedScoringSideSubsystems;

import oldcode.OpModes.Auto.AutoOpMode;

public class right {

    private AutoOpMode opmode;
    public right(AutoOpMode opMode){
        this.opmode=opMode;
    }


    public static AutoOpMode.Position SPIKE_RIGHT = new AutoOpMode.Position(25, -44.625, 90);
    public static AutoOpMode.Position SPIKE_RIGHT_BACKUP = new AutoOpMode.Position(25, -52, 90);

    public static AutoOpMode.Position AFTER_SPIKE_POSITION = new AutoOpMode.Position(24, -52, 90);

    public static AutoOpMode.Position AFTER_SPIKE_TURN = new AutoOpMode.Position(24, -52, 180);

    public static AutoOpMode.Position DROP_START_INTERMEDIATE = new AutoOpMode.Position(44, -52, 180);

    public static AutoOpMode.Position DROP_PARK_INTERMEDIATE = new AutoOpMode.Position(44, -60, 180);
    public static AutoOpMode.Position DROP_POSITION_R = new AutoOpMode.Position(44, -44, 180);
    public static AutoOpMode.Position DROP_POSITION_TOUCH_BOARD_R = new AutoOpMode.Position(52, -44, 180);
    public static AutoOpMode.Position DROP_POSITION_BACKUP_BOARD_R = new AutoOpMode.Position(44, -44, 180);

    public static AutoOpMode.Position PARK_POSITION = new AutoOpMode.Position(60, -60, 180);
    public void go() {
        opmode.goTo(SPIKE_RIGHT);
        opmode.intake.returnPixel();
        opmode.sleep(2000);
        opmode.goTo(SPIKE_RIGHT_BACKUP);
        opmode.intake.stop();
        opmode.goTo(AFTER_SPIKE_POSITION);
        opmode.goTo(AFTER_SPIKE_TURN);
        opmode.goTo(DROP_START_INTERMEDIATE);
            opmode.goTo(DROP_POSITION_R);
            opmode.drop.goToPosition(3);
            opmode.setSpeed(AutoOpMode.Speed.SLOW);
            opmode.goTo(DROP_POSITION_TOUCH_BOARD_R);
            opmode.sleep(1000);
            opmode.topGate.setGateOpen();
            opmode.telemetry.update();
            opmode.sleep(1000);
            opmode.topGate.setGateStopped();
            opmode.goTo(DROP_POSITION_BACKUP_BOARD_R);




        opmode.setSpeed(AutoOpMode.Speed.MEDIUM);
        opmode.drop.goToBottom();
        opmode.sleep(1000);
        opmode.goTo(DROP_PARK_INTERMEDIATE);
        opmode.goTo(PARK_POSITION);
        opmode.sleep(10000);

    }

}
