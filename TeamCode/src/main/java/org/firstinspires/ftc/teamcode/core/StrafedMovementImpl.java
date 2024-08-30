package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class StrafedMovementImpl implements StrafingMovement {
    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backRight;
    private final DcMotorEx backLeft;

    public StrafedMovementImpl(DcMotorEx frontLeft, DcMotorEx frontRight, DcMotorEx backRight, DcMotorEx backLeft) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
        this.backLeft = backLeft;

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public StrafedMovementImpl(HardwareMap hardwareMap) {
        this(hardwareMap.get(DcMotorEx.class, "leftFront"),
                hardwareMap.get(DcMotorEx.class, "rightFront"),
                hardwareMap.get(DcMotorEx.class, "rightRear"),
                hardwareMap.get(DcMotorEx.class, "leftRear"));
    }

    @Override
    public void drivePower(double frontLeftPower, double frontRightPower, double backRightPower, double backLeftPower) {
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
        backLeft.setPower(backLeftPower);
    }

    public double[] motorVelocities() {
        return new double[] {
                frontLeft.getVelocity(),
                frontLeft.getVelocity(),
                backRight.getVelocity(),
                backLeft.getVelocity()
        };
    }

    @Override
    public void driveDRS(double drive, double rotate, double strafe) {
        double frontLeftPower = drive - rotate + strafe;
        double frontRightPower = drive + rotate - strafe;
        double backLeftPower = drive - rotate - strafe;
        double backRightPower = drive + rotate + strafe;

        this.drivePower(frontLeftPower, frontRightPower, backRightPower, backLeftPower);
    }

    @Override
    public void drive(double velocity) {
        this.driveDRS(velocity, 0, 0);
    }

    @Override
    public void rotate(double velocity) {
        this.driveDRS(0, velocity, 0);
    }

    @Override
    public void strafe(double velocity) {
        this.driveDRS(0, 0, velocity);
    }

    @Override
    public void stop() {
        this.drivePower(0, 0, 0, 0);
    }

}
