package frc.team6223.robot

import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import frc.team6223.robot.auto.AutoUtilities
import frc.team6223.arsenalFramework.software.controllers.ArcadeDriveController
import frc.team6223.arsenalFramework.drive.ArsenalDrive
import frc.team6223.arsenalFramework.hardware.ArsenalNavXMicro
import frc.team6223.arsenalFramework.hardware.ArsenalRobot
import frc.team6223.arsenalFramework.hardware.motor.ArsenalTalon
import frc.team6223.arsenalFramework.operator.ArsenalOperatorInterface
import frc.team6223.arsenalFramework.software.FullTrajectory
import frc.team6223.arsenalFramework.software.commands.MoveDriveTrainCommand
import frc.team6223.arsenalFramework.software.controllers.ForceMovementController
import frc.team6223.arsenalFramework.software.controllers.MotionProfileController
import frc.team6223.arsenalFramework.software.controllers.NoMovementController
import frc.team6223.arsenalFramework.software.readMotionProfile
import frc.team6223.arsenalFramework.software.units.*
import frc.team6223.robot.conf.LEFT_DRIVE_CONTROLLER
import frc.team6223.robot.conf.RIGHT_DRIVE_CONTROLLER
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint

class Robot: ArsenalRobot(TimedRobot.DEFAULT_PERIOD, 0.05) {

    private lateinit var driveSubsystem: ArsenalDrive
    //private val pdpSubsystem = PDP(PDP_CAN_ID)
    private val robotSideChooser = AutoUtilities.generateSendableChooser()

    override fun injectAutonomousCommands(): SendableChooser<Command> {
        val sendableChooser = SendableChooser<Command>()
        /*sendableChooser
                .addDefault(
                        "Force Move",
                        MoveDriveTrainCommand(
                            ForceMovementController(
                                    Time(1.5, TimeUnits.SECONDS),
                                    -0.35, 0.1
                            ), driveSubsystem))*/
        sendableChooser.addDefault(
                        "Move 5ft using Motion Profiling",
                        MoveDriveTrainCommand(
                                MotionProfileController(
                                        arrayOf(
                                                Waypoint(0.0, 0.0, 0.0),
                                                Waypoint(5.0, 5.0, 90.0)
                                        ),
                                        Trajectory.Config(
                                                Trajectory.FitMethod.HERMITE_QUINTIC,
                                                Trajectory.Config.SAMPLES_HIGH,
                                                0.02,
                                                1.0,
                                                1.0,
                                                60.0),
                                        Velocity(Distance(6.54, DistanceUnits.FEET), Time(1.0, TimeUnits.SECONDS))),
                                driveSubsystem)
                )
        return sendableChooser
    }

    override fun allocateSubsystems(preferences: Preferences) {
        driveSubsystem = ArsenalDrive(
                NoMovementController(),
                ArsenalNavXMicro(),
                ArsenalTalon(LEFT_DRIVE_CONTROLLER, true,
                        startingSensorPhase = true,
                        startInverted = false,
                        invertSensorOutput = false),
                ArsenalTalon(RIGHT_DRIVE_CONTROLLER, true,
                        startingSensorPhase = true,
                        startInverted = true,
                        invertSensorOutput = false)
        )
    }

    override fun retrieveMotionProfiles(): List<FullTrajectory> {
        return listOf()
    }

    override fun allocateOperatorInterface(preferences: Preferences): ArsenalOperatorInterface {
        return OI()
    }

    override fun setTeleoperatedCommand() {
        MoveDriveTrainCommand(ArcadeDriveController(operatorInterface.primaryJoystick), driveSubsystem).start()
    }

    override fun autonomousInit() {
        super.autonomousInit()
        this.driveSubsystem.resetEncoders()
        //this.driveSubsystem.zeroYaw()
    }

    override fun teleopInit() {
        super.teleopInit()
        this.driveSubsystem.resetEncoders()
    }

    override fun dashboardPeriodic() {
        this.driveSubsystem.dashboardPeriodic()
    }
}
