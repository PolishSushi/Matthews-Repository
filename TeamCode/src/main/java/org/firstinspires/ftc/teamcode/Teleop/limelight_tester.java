package org.firstinspires.ftc.teamcode.Teleop;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher;
import org.firstinspires.ftc.teamcode.Subsystems.MecanumDrive;

import java.util.Locale;

@Config
@TeleOp(name="limelight tester", group=".")
public class limelight_tester extends CommandOpMode {

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
    double rightStickXVal;
    private Timer pathTimer;
    private Limelight3A limelight;
    public static double HEADING_KP_TX = 0.023;
    public static double ROTATION_MIN_POWER = 0.0;
    private boolean isHeadingLocked = false;
    double finalRotation;

    @Override
    public void initialize() {
        driver = new GamepadEx(gamepad1);

        drive = new MecanumDrive();
        pathTimer = new Timer();
        drive.init(hardwareMap);
        launcher = new Launcher(hardwareMap);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();


        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> {
                    outtakePosition = (outtakePosition + 1) % 2;
                    testVariable = true;
                    pathTimer.resetTimer();
                    //if needed test intake using left and right bumper?
                }));
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> {
                    intakePosition = (intakePosition + 1) % 2;

                }));
//        driver.getGamepadButton(GamepadKeys.Button.X)
//                .whenPressed(new InstantCommand(() -> {
//
//                }));
        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new InstantCommand(() -> {
                    isHeadingLocked = true;
                }));
        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new InstantCommand(() -> {
                    isHeadingLocked = false;
                }));
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

        rightStickXVal = gamepad1.right_stick_x;
        rightStickXVal = Range.clip(rightStickXVal, -1, 1);

        if (leftStickYVal < -0.1) {
            drive.driveForward(-leftStickYVal);
        } else if (leftStickYVal > 0.1) {
            drive.driveBack(leftStickYVal);
        } else {
            drive.stopMotors();
        }
        LLResult result = limelight.getLatestResult();
        if (isHeadingLocked) {


            double error = result.getTx();

            if (result.isValid()) {
                finalRotation = (error * HEADING_KP_TX);
                testVariable = true;
                if (Math.abs(finalRotation) > 0 && Math.abs(finalRotation) < ROTATION_MIN_POWER) {
                    finalRotation = Math.signum(finalRotation) * ROTATION_MIN_POWER;
//                } else {
//                    finalRotation = 0;
//                    t
//                }


                }
                drive.leftMotor.setPower(-finalRotation);
                drive.rightMotor.setPower(-finalRotation);
            }
        }
            else {
                if (rightStickXVal > 0.1) {
                    drive.rotateRight(rightStickXVal);
                } else if (rightStickXVal < -0.1) {
                    drive.rotateLeft(-rightStickXVal);
                }


            //intake automations


            switch (intakePosition) {
                case 0:
                    //set to half power
                    intakeMotor = -




























































                            0.75;
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
                    if (launcherMotor < 1) {
                        launcherMotor = 1;
                        feederServo = 1;
                    }
                    break;
                case 1:
                    if (launcherMotor > 0) {
                        launcherMotor = 0;
                        outtakePosition = 2;
                    }
                    break;
                case 2:
                    feederServo = -1;
                    if (pathTimer.getElapsedTimeSeconds() > 2) {

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


            telemetry.addData("Battery Voltage", hardwareMap.voltageSensor.iterator().next().getVoltage());

            telemetry.addData("tx", result.getTx());
            telemetry.addData("txnc", result.getTxNC());
            telemetry.addData("ty", result.getTy());
            telemetry.addData("tync", result.getTyNC());
            telemetry.addData("Heading Lock", isHeadingLocked ? "ON" : "OFF");
            //telemetry.addData("test Variable", testVariable);
            telemetry.addData("Path timer: ", pathTimer.getElapsedTimeSeconds());
            telemetry.addData("test: ", testVariable);
            telemetry.addData("rotation", finalRotation);
            telemetry.update();

        }
    }
}
