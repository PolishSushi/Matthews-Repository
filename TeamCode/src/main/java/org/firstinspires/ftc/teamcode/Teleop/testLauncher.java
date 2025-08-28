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
@TeleOp(name="test launcher", group=".")
public class testLauncher extends CommandOpMode {

    private Launcher launcher;
    GamepadEx driver;
    private double power = 0;
    private double what = 0;


    @Override
    public void initialize() {
        driver = new GamepadEx(gamepad1);

        launcher = new Launcher(hardwareMap);


        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> {
                    power = 1;

                }));
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new InstantCommand(() -> {
                    power = 0;

                }));
        driver.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new InstantCommand(() -> {
                    what = 1;
                }));
        driver.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new InstantCommand(() -> {
                    what = 0;
                }));
        driver.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(new InstantCommand(() -> {
                    what = -1;
                }));
        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .whileActiveContinuous(new InstantCommand(() -> {
                    if (1 >= power && power>= -1) {
                        power -= 0.1;
                    }
                }));
        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1)
                .whileActiveContinuous(new InstantCommand(() -> {
                    if (1 >= power && power>= -1) {
                        power += 0.1;
                    }
                }));

        telemetry.addLine("READY!");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run();
        launcher.setPowerToLauncher(power);
        launcher.setPowerToFeeder(power);
        telemetry.update();
}
}
