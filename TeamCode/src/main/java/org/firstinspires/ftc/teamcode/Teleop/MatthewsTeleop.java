package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.drivebase.MecanumDrive;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@Config
@TeleOp(name="Matthew's Teleop", group=".")
public class MatthewsTeleop extends CommandOpMode {

    private MecanumDrive drive;
    GamepadEx driver;
    double leftStickYVal;
    double leftStickXVal;


    @Override
    public void initialize() {
        driver = new GamepadEx(gamepad1);

        drive = new MecanumDrive();

        drive.init(hardwareMap);


        telemetry.addLine("READY!");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run();

        leftStickYVal = gamepad1.left_stick_y;
        leftStickYVal = Range.clip(leftStickYVal, -1, 1);
        drive.leftMotor.setPower(leftStickYVal);
        drive.rightMotor.setPower(-leftStickYVal);

        leftStickXVal = gamepad1.right_stick_x;
        leftStickXVal = Range.clip(leftStickXVal, -1, 1);

        drive.leftMotor.setPower(-leftStickXVal);
        drive.rightMotor.setPower(-leftStickXVal);

        telemetry.addData("Battery Voltage", hardwareMap.voltageSensor.iterator().next().getVoltage());

        telemetry.addData("leftStickXVal", leftStickYVal);
        telemetry.update();
}
}
