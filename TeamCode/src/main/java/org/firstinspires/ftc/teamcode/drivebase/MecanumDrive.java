package org.firstinspires.ftc.teamcode.drivebase;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class MecanumDrive {
    public DcMotorEx leftMotor;
    public DcMotorEx rightMotor;

    public void init(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.get(DcMotorEx.class, "lm");
        rightMotor = hardwareMap.get(DcMotorEx.class, "rm");

        DcMotorEx[] motors = new DcMotorEx[]{
                leftMotor,rightMotor
        };

        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void setPowers(double power) {
        leftMotor.setPower(power);
        rightMotor.setPower(-power);
    }


}