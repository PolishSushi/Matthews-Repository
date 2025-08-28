package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@Config
@TeleOp(name="Matthew's Teleop", group=".")
public class MatthewsTeleop extends CommandOpMode {

    private MecanumDrive drive;
    private Launcher launcher;
    GamepadEx driver;
    private double power = 0;
    double leftStickYVal;
    double leftStickXVal;


    @Override
    public void initialize() {
        driver = new GamepadEx(gamepad1);

        drive = new MecanumDrive();

        drive.init(hardwareMap);
        launcher = new Launcher(hardwareMap);


//        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
//                .whenPressed(new InstantCommand(() -> {
//                    power = 1;
//                }));
//        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
//                .whenPressed(new InstantCommand(() -> {
//                    power = 0;
//                }));
//        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
//                .whileActiveContinuous(new InstantCommand(() -> {
//                    if (1 >= power && power>= -1) {
//                        power -= 0.1;
//                    }
//                }));
        telemetry.addLine("READY!");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run();
        launcher.setPowerToLauncher(power);
        launcher.setPowerToFeeder(power);

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
