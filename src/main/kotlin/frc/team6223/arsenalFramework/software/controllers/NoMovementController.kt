package frc.team6223.arsenalFramework.software.controllers

import frc.team6223.arsenalFramework.drive.ControllerInput
import frc.team6223.arsenalFramework.drive.DriveController
import frc.team6223.arsenalFramework.drive.DriveControllerOutput
import frc.team6223.arsenalFramework.hardware.MotorControlMode
import frc.team6223.arsenalFramework.software.units.Distance

class NoMovementController: DriveController {
    override fun calculateMotorOutput(controllerInput: ControllerInput): DriveControllerOutput {
        return DriveControllerOutput(MotorControlMode.VoltagePercentOut, 0.0, 0.0)
    }

    override fun start(leftInitial: Distance, rightInitial: Distance) {
        println("No Movement Controller enabled")
    }

    override fun stop() {
        println("Disabling No Movement Controller")
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun dashboardPeriodic() {}
}