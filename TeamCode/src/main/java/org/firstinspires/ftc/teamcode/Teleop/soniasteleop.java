package org.firstinspires.ftc.teamcode.Teleop;

import org.firstinspires.ftc.teamcode.Subsystems.tankDrive;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@Config
@TeleOp
public class soniasteleop extends CommandOpMode{
    private tankDrive drive;
    GamepadEx driver;
    double leftStickYVal;
    double rightStickXVal;

    public void initialize (){

        driver= new GamepadEx(gamepad1);
        drive= new tankDrive();


    }
    @Override
    public void  run() {
        leftStickYVal = gamepad1.left_stick_y;
        leftStickYVal = Range.clip(leftStickYVal, -1, 1);

        rightStickXVal = gamepad1.right_stick_x;
        rightStickXVal = Range.clip(rightStickXVal, -1, 1);
    }
}
