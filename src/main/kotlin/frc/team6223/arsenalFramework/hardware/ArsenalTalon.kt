package frc.team6223.arsenalFramework.hardware

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX

class ArsenalTalon(talonId: Int) {
    private val talonSrx: TalonSRX = TalonSRX(talonId)
    val followers: List<FollowerSRX> = ArrayList()

    val reportedPosition = talonSrx.getSelectedSensorPosition(0)
    val reportedVelocity = talonSrx.getSelectedSensorVelocity(0)

    fun setPercentOut(percentOut: Double) {
        talonSrx.set(ControlMode.PercentOutput, percentOut)
    }


    inner class FollowerSRX(followerId: Int) {
        private val follower: TalonSRX = TalonSRX(followerId)
        init {
            follower.follow(this@ArsenalTalon.talonSrx)
        }
    }

}