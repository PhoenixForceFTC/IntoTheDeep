package oldcode.OpModes.Auto.RedScoringSideSubsystems;

import oldcode.OpModes.Auto.AutoOpMode;

public class middle {
    public static AutoOpMode.Position SPIKE_CENTER = new AutoOpMode.Position(12, -36, 90);
    public static AutoOpMode.Position SPIKE_CENTER_BACKUP = new AutoOpMode.Position(12, -44, 90);

    public static AutoOpMode.Position AFTER_SPIKE_POSITION = new AutoOpMode.Position(24, -52, 90);

    public static AutoOpMode.Position AFTER_SPIKE_TURN = new AutoOpMode.Position(24, -52, 180);

    public static AutoOpMode.Position DROP_START_INTERMEDIATE = new AutoOpMode.Position(44, -52, 180);

    public static AutoOpMode.Position DROP_PARK_INTERMEDIATE = new AutoOpMode.Position(44, -60, 180);
    public static AutoOpMode.Position DROP_POSITION = new AutoOpMode.Position(44, -36, 180);
    public static AutoOpMode.Position DROP_POSITION_TOUCH_BOARD = new AutoOpMode.Position(52, -36, 180);
    public static AutoOpMode.Position DROP_POSITION_BACKUP_BOARD = new AutoOpMode.Position(44, -36, 180);

    public static AutoOpMode.Position PARK_POSITION = new AutoOpMode.Position(60, -60, 180);




    private AutoOpMode opmode;
    public middle(AutoOpMode opMode){
        this.opmode=opMode;
    }

    public void go() {
        opmode.goTo(SPIKE_CENTER);
        opmode.intake.returnPixel();
        opmode.sleep(2000);
        opmode.goTo(SPIKE_CENTER_BACKUP);
        opmode.intake.stop();
        opmode.goTo(AFTER_SPIKE_POSITION);
        opmode.goTo(AFTER_SPIKE_TURN);
        opmode.goTo(DROP_START_INTERMEDIATE);
            opmode.goTo(DROP_POSITION);
            opmode.drop.goToPosition(3);
            opmode.setSpeed(AutoOpMode.Speed.SLOW);
            opmode.goTo(DROP_POSITION_TOUCH_BOARD);
            opmode.sleep(1000);
            opmode.topGate.setGateOpen();
            opmode.telemetry.update();
            opmode.sleep(1000);
            opmode.topGate.setGateStopped();
            opmode.goTo(DROP_POSITION_BACKUP_BOARD);




        opmode.setSpeed(AutoOpMode.Speed.MEDIUM);
        opmode.drop.goToBottom();
        opmode.sleep(1000);
        opmode.goTo(DROP_PARK_INTERMEDIATE);
        opmode.goTo(PARK_POSITION);
        opmode.sleep(10000);

    }

}
