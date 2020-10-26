package com.example.mycookie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.CheckBox
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.infoBar

class Settings : AppCompatActivity() {
    fun statusBar() {
        infoBar.max = getSaveInfo("scoreLevelUp")
        infoBar.progress = getSaveInfo("score");
    }

    private fun LoopInfoPlayer() {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                statusBar()
                stats.text = "Score -> ${getSaveInfo("score")} \nLevel -> ${getSaveInfo("scoreLevel")} \n\n${getSaveInfo("score")} / ${getSaveInfo("scoreLevelUp")}"
                mainHandler.postDelayed(this, 0)
            }
        })
    }

    private fun infoPlayer() {
        LoopInfoPlayer()
    }

    private fun saveInfo(name: String, elem: Int) {
        var pref = getSharedPreferences("info", MODE_PRIVATE);
        var editor = pref.edit();

        editor.putInt(name, elem);
        editor.apply()
    }

    fun getSaveInfo(name: String): Int {
        var shared = getSharedPreferences("info", MODE_PRIVATE)
        var stringTemp = shared.getInt(name, 0);

        return stringTemp
    }

    private fun reset() {
        reset_game.setOnClickListener {
            SettingsHandler.instance.reset = true

            saveInfo("score", 0)
            saveInfo("scoreLevel", 1)
            saveInfo("scoreLevelUp", 100)
        }
    }

    private fun setAutoClick(id: Boolean, string: String) {
        if ( getSaveInfo("scoreLevel") >= 10) {
            SettingsHandler.instance.autoClick = id
            statusBar()
            Toast.makeText(applicationContext, "Auto click $string", Toast.LENGTH_SHORT).show()
        } else {
            SettingsHandler.instance.autoClick = false
            Toast.makeText(applicationContext, "Auto click ne peut pas être activer avant le niveau 10!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playSound(id: Boolean, string: String) {
        SettingsHandler.instance.son = id
        Toast.makeText(applicationContext, "Son $string", Toast.LENGTH_SHORT).show()
    }

    private fun checkCheckbox(id: CheckBox) {
        id.setOnClickListener {
            if (id == yes_auto_click) {
                if (id.isChecked) {
                    setAutoClick(true, "activer")
                }
            }
            if (id == no_auto_click) {
                if (id.isChecked) {
                    setAutoClick(false, "désactiver")
                }
            }
            if (id == yes_sound) {
                if (id.isChecked) {
                    playSound(true, "activer")
                }
            }
            if (id == no_sound) {
                if (id.isChecked) {
                    playSound(false, "désactiver")
                }
            }
        }
    }

    private fun goToShop() {
        boutique.setOnClickListener {
            val intent = Intent(this, Shopping::class.java)

            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (SettingsHandler.instance.chooseBackground == 0) {
            background_settings.setBackgroundResource(getSaveInfo("background"))
        }
        infoPlayer()
        checkCheckbox(yes_auto_click)
        checkCheckbox(no_auto_click)
        checkCheckbox(yes_sound)
        checkCheckbox(no_sound)
        goToShop()
        reset()
    }

    override fun onResume() {
        super.onResume()
        if (SettingsHandler.instance.chooseBackground == 0) {
            background_settings.setBackgroundResource(getSaveInfo("background"))
        }
    }
}