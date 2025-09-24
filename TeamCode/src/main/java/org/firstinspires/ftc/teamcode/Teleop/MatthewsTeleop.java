package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.Subsystems.tankDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Launcher;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.pedropathing.util.Timer;


@Config
@TeleOp(name="Matthew's Teleop", group=".")
public class MatthewsTeleop extends CommandOpMode {
    //declaring variables

    private tankDrive drive;
    private Launcher launcher;
    private int outtakePosition = -1;
    private int intakePosition = -1;
    private int intakeREVERSEPosition = -1;
    GamepadEx driver;
    GamepadEx control2;
    private boolean testVariable = false;
    private double launcherMotor = 0;
    private double feederServo = 0;
    private double intakeMotor = 0;
    double leftStickYVal;
    double rightStickXVal;
    double maxSpeed = 1;
    private Timer pathTimer;

    @Override
    public void initialize() {
        //initializing all of my classes and setting up automations
        driver = new GamepadEx(gamepad1);
        control2 = new GamepadEx(gamepad2);

        drive = new tankDrive();
        pathTimer = new Timer();
        drive.init(hardwareMap);
        launcher = new Launcher(hardwareMap);


        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> {
                    outtakePosition = (outtakePosition +1) % 2;
                    testVariable = true;
                    pathTimer.resetTimer();
                }));
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> {
                    intakePosition = (intakePosition+1) % 2;
                    if (intakeREVERSEPosition != -1){
                        intakeREVERSEPosition = 1;
                    }

                }));
        driver.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new InstantCommand(() -> {
                    intakeREVERSEPosition = (intakeREVERSEPosition +1) % 2;
                    if (intakePosition != -1){
                        intakePosition = 1;
                    }
                }));

        control2.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new InstantCommand(() -> {
                    maxSpeed = 1;
                }));
        control2.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(() -> {
                    maxSpeed = 0.5;
                }));
        control2.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(new InstantCommand(() -> {
                    maxSpeed = 0.35;
                }));
//        new Trigger(() -> control2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
//                .whileActiveContinuous(new InstantCommand(() -> {
//                    if (maxSpeed >= 0.1 && maxSpeed <= 1){
//                        maxSpeed -= 0.0001;
//                    }
//
//                }));
//        new Trigger(() -> control2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1)
//                .whileActiveContinuous(new InstantCommand(() -> {
//                    if (maxSpeed >= 0.01 && maxSpeed <= 1){
//                        maxSpeed += 0.0001;
//                    }
//                }));


        telemetry.addLine("READY!");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run();
        //setting power to all motors
        launcher.setPowerToLauncher(launcherMotor);
        launcher.setPowerToFeeder(-feederServo);
        launcher.setPowerToIntake(intakeMotor);

        leftStickYVal = gamepad1.left_stick_y;
        leftStickYVal = Range.clip(leftStickYVal, -maxSpeed, maxSpeed);

        rightStickXVal = gamepad1.right_stick_x;
        rightStickXVal = Range.clip(rightStickXVal, -1, 1);

        //drive stuff
        if (leftStickYVal < -0.1) {
            drive.driveForward(-leftStickYVal);
        } else if (leftStickYVal > 0.1) {
            drive.driveBack(leftStickYVal);
        } else if (rightStickXVal > 0.1) {
            drive.rotateRight(rightStickXVal);
        } else if (rightStickXVal < -0.1) {
            drive.rotateLeft(-rightStickXVal);
        } else {
            drive.stopMotors();
        }

        //SWITCH CASES FOR AUTOMATION!!!
        //intake automations
        switch (intakePosition) {
            case 0:
                //set to half power
                intakeMotor = -0.75;
                break;
            case 1:
                intakeMotor = 0;
                intakePosition = -1;
                break;
            default:
                break;
        }
        //intake automation to reverse just incase ball gets stuck
        switch (intakeREVERSEPosition) {
            case 0:
                //set to half power
                intakeMotor = 0.3;
                break;
            case 1:
                intakeMotor = 0;
                intakeREVERSEPosition = -1;
                break;
            default:
                break;
        }
        //outtake automations
        switch (outtakePosition) {
            case 0:
                if (launcherMotor <1) {
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



        telemetry.addData("Battery Voltage", hardwareMap.voltageSensor.iterator().next().getVoltage());

        telemetry.addData("Maximum driving speed as a percentage: ", maxSpeed);
        telemetry.addData("Path timer: ", pathTimer.getElapsedTimeSeconds());
        telemetry.addData("test: ",testVariable);
        telemetry.addData("Intake: ", intakePosition);
        telemetry.addData("Intake out: ", intakeREVERSEPosition);
        telemetry.update();
}
}
