package org.firstinspires.ftc.teamcode.Subsystems;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.CRServo;
public class Launcher {
    public DcMotorEx launcherMotor;
    public DcMotorEx intakeMotor;
    public CRServo servo;

    public Launcher(HardwareMap hardwareMap) {
        launcherMotor = hardwareMap.get(DcMotorEx.class, "launcher");
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        servo = hardwareMap.get(CRServo.class, "servo");

        launcherMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void setPowerToLauncher(double power) {

        launcherMotor.setPower(power);
    }
    public void setPowerToIntake(double power) {

        intakeMotor.setPower(power);
    }
    public void setPowerToFeeder(double power) {

        servo.setPower(power);
    }
}
