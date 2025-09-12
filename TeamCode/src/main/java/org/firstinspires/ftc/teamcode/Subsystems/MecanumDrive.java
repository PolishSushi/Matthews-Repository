package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class MecanumDrive extends SubsystemBase {
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
    public void driveForward(double speed) {
        leftMotor.setPower(-speed);
        rightMotor.setPower(speed);
    }

    public void driveBack(double speed) {
        leftMotor.setPower(speed);
        rightMotor.setPower(-speed);
    }

    public void rotateLeft(double speed) {
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);

    }

    public void rotateRight(double speed) {
        leftMotor.setPower(-speed);
        rightMotor.setPower(-speed);

    }
    public void stopMotors(){
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

}