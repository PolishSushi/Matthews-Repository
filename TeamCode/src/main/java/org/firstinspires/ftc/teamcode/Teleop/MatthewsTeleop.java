package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.pedropathing.util.Timer;
@Config
@TeleOp(name="Matthew's Teleop", group=".")
public class MatthewsTeleop extends CommandOpMode {

    private MecanumDrive drive;
    private Launcher launcher;
    private int outtakePosition = -1;
    private int intakePosition = -1;
    GamepadEx driver;
    private boolean testVariable = false;
    private double launcherMotor = 0;
    private double feederServo = 0;
    private double intakeMotor = 0;
    double leftStickYVal;
    double leftStickXVal;
    private Timer pathTimer;

    @Override
    public void initialize() {
        driver = new GamepadEx(gamepad1);

        drive = new MecanumDrive();
        pathTimer = new Timer();
        drive.init(hardwareMap);
        launcher = new Launcher(hardwareMap);


        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> {
                    outtakePosition = (outtakePosition +1) % 2;
                    pathTimer.resetTimer();
                    //if needed test intake using left and right bumper?
                }));
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> {
                    intakePosition = (intakePosition+1) % 2;

                }));
//        driver.getGamepadButton(GamepadKeys.Button.X)
//                .whenPressed(new InstantCommand(() -> {
//
//                }));
//        driver.getGamepadButton(GamepadKeys.Button.A)
//                .whenPressed(new InstantCommand(() -> {
//
//                }));
        //yes to whoever is reading this, i still need the commented code below
//        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
//                .whileActiveContinuous(new InstantCommand(() -> {
//                    if (1 >= launcherMotor && launcherMotor >= -1) {
//                        launcherMotor -= 0.1;
//                    }
//                }));
//        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1)
//                .whileActiveContinuous(new InstantCommand(() -> {
//                    if (1 >= launcherMotor && launcherMotor >= -1) {
//                        launcherMotor += 0.1;
//                    }
//                }));
        telemetry.addLine("READY!");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run();
        launcher.setPowerToLauncher(launcherMotor);
        launcher.setPowerToFeeder(-feederServo);
        launcher.setPowerToIntake(intakeMotor);

        leftStickYVal = gamepad1.left_stick_y;
        leftStickYVal = Range.clip(leftStickYVal, -1, 1);
        drive.leftMotor.setPower(leftStickYVal);
        drive.rightMotor.setPower(-leftStickYVal);

        leftStickXVal = gamepad1.right_stick_x;
        leftStickXVal = Range.clip(leftStickXVal, -1, 1);

        //intake automations
        switch (intakePosition) {
            case 0:
                //set to half power
                intakeMotor = 0.5;
                break;
            case 1:
                intakeMotor = 0;
                break;
            default:
                break;
        }
        //outtake automations
        switch (outtakePosition) {
            case 0:
                launcherMotor = 1;
                feederServo = 1;
                break;
            case 1:
                if (launcherMotor > 0) {
                    outtakePosition = 0;
                    launcherMotor = 0;
                    outtakePosition +=1;
                }
                break;
            case 2:
                feederServo = -1;
                if (pathTimer.getElapsedTimeSeconds() > 2){

                    outtakePosition += 1;
                }
                break;
            case 3:
                feederServo = 0;
                outtakePosition = -1;
                break;

            default:
                break;
        }

        drive.leftMotor.setPower(-leftStickXVal);
        drive.rightMotor.setPower(-leftStickXVal);

        telemetry.addData("Battery Voltage", hardwareMap.voltageSensor.iterator().next().getVoltage());

        //telemetry.addData("test Variable", testVariable);
        telemetry.addData("Path timer: ", pathTimer.getElapsedTimeSeconds());
        telemetry.update();
}
}
