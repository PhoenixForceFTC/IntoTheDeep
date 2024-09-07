package oldcode.OpModes.Auto.RedScoringSideSubsystems;

import oldcode.OpModes.Auto.AutoOpMode;

public class left {

    public static AutoOpMode.Position SPIKE_LEFT_TURN = new AutoOpMode.Position(11, -36, 165);
    public static AutoOpMode.Position DROP_POSITION_TOUCH_BOARD_L = new AutoOpMode.Position(52, -28, 180);
    public static AutoOpMode.Position RED_PILE_1_PREPOSITION = new AutoOpMode.Position(12, -12, 180);
    public static AutoOpMode.Position RED_PILE_1_POSITION = new AutoOpMode.Position(-18, -16, 185);

    private AutoOpMode opmode;
    public left(AutoOpMode opMode){
        this.opmode=opMode;
    }

    public void go() {

        opmode.goTo(SPIKE_LEFT_TURN);
        opmode.intake.returnPixel();
        opmode.sleep(1000);
        opmode.drop.goToPosition(3);
        opmode.goTo(DROP_POSITION_TOUCH_BOARD_L);
        opmode.topGate.setGateOpen();
        opmode.sleep(1000);
        opmode.topGate.setGateStopped();
        opmode.splineTo(RED_PILE_1_PREPOSITION);
        opmode.intake.stop();
        opmode.drop.goToPosition(0);
        opmode.goTo(RED_PILE_1_POSITION);


        opmode.snagger.goToPosition(3);
        opmode.telemetry.update();
        int counter = 0;
        opmode.intake.eatPixel();
        while (counter < 40 && opmode.intake.getIntakePixels() < 2) {

            opmode.sleep(100);
            counter++;
            opmode.intake.pixelCounterTelemetry();
            opmode.telemetry.update();
        }
        opmode.intake.frontWheelReverse();
        opmode.snagger.goToPosition(0);

        opmode.sleep(1000);

        opmode.intake.transferPixel();
        opmode.sleep(2000);



        //goTo(SPIKE_LEFT_BACKUP);
        //goTo(SPIKE_LEFT_ADJUST_HEADING);
        //coneDrop(1);

    }

}
