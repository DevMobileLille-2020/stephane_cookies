package com.example.mycookie

class SettingsHandler private constructor() {
    var chooseImage: Int = 0
    var chooseBackground: Int = 0
    var chooseBonus: Int = 0
    var autoClick: Boolean = false
    var son: Boolean = true
    var reset: Boolean = false
    var save: Int = 0

    private object HOLDER {
        val INSTANCE = SettingsHandler()
    }

    companion object {
        val instance: SettingsHandler by lazy { HOLDER.INSTANCE }
    }
}