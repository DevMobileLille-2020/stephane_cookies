package com.example.mycookie

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mediaPlayer: MediaPlayer? = null
    var mediaClick: MediaPlayer? = null

    var score = 0
        set(value) {
            field = value
            score_text.text = "Score: $value"
            saveInfo("score", value)
        }

    private var scoreLevel = 1
        set(value) {
            field = value
            level_text.text = "Level: $value"
            saveInfo("scoreLevel", value)
        }

    private var scoreLevelUp = 100
        set(value) {
            field = value
            level_up.text = "$score / $value"
            saveInfo("scoreLevelUp", value)
        }

    private fun saveInfo(name: String, elem: Int) {
        var pref = getSharedPreferences("info", MODE_PRIVATE);
        var editor = pref.edit();

        editor.putInt(name, elem);
        editor.apply()
    }

    private fun getSaveInfo(name: String): Int {
        var shared = getSharedPreferences("info", MODE_PRIVATE)
        var stringTemp = shared.getInt(name, 0);

        return stringTemp
    }

    private fun openSettings() {
        settings.setOnClickListener{
            val intent = Intent(this, Settings::class.java)

            startActivity(intent)
        }
    }

    private fun animationLevelUp() {
        val alpha = AlphaAnimation(0F, 1F)

        alpha.duration = 500
        level.startAnimation(alpha)
    }

    fun animationMonster() {
        val translate = TranslateAnimation(0F, 100F, 0F, 0F)

        translate.duration = 500
        monster.startAnimation(translate)
    }

    fun modificationValue(id: Int) {
        if (id == 1) {
            scoreLevel++
            score -= scoreLevelUp
            scoreLevelUp += 100
        }
        if (id == 2) {
            score++
        }
        if (SettingsHandler.instance.reset) {
            score = 0
        }

        infoBar.max = scoreLevelUp
        infoBar.progress = score;
        level_up.text = "$score / $scoreLevelUp"
    }

    fun setVisibilityWin() {
        if (win.visibility == View.INVISIBLE && win_square.visibility == View.INVISIBLE){
            win.visibility = View.VISIBLE
            win_square.visibility = View.VISIBLE
        }
        monster.postDelayed({
            win.visibility = View.INVISIBLE
            win_square.visibility = View.INVISIBLE
        }, 100)
    }

    private fun initListener() {
        monster.setOnClickListener {
            modificationValue(2)
            setVisibilityWin()
            animationMonster()
            soundOnClick()
            SettingsHandler.instance.reset = false
        }
        level.setOnClickListener {
            condLevelUp()
            animationLevelUp()
        }
    }

    private fun autoClick() {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                if (SettingsHandler.instance.autoClick) {
                    modificationValue(2)
                    SettingsHandler.instance.reset = false
                    setVisibilityWin()
                    animationMonster()
                    mainHandler.postDelayed(this, 0)
                }
                else {
                    mainHandler.removeCallbacksAndMessages(null)
                }
            }
        })
    }

    private fun soundOnClick() {
        if(mediaClick != null && mediaClick!!.isPlaying){
            mediaClick!!.stop();
            mediaClick!!.reset();
            mediaClick!!.release();
            mediaClick = null;
        }
        mediaClick =  MediaPlayer();
        mediaClick = MediaPlayer.create(applicationContext, R.raw.hit);
        if (!SettingsHandler.instance.son) {
            if (mediaClick != null) {
                Log.i("info", "son désactiver")
                mediaClick!!.stop()
                mediaClick!!.release()
                mediaClick = null
            }
        } else {
            mediaClick!!.start();
        }
    }

    private fun playSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music)
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.start()
        } else mediaPlayer!!.start()
        if (!SettingsHandler.instance.son) {
            if (mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
        }
    }

    private fun condLevelUp() {
        if (score < scoreLevelUp) {
            Toast.makeText(this, "Bonjour, votre score est trop bas pour augmenter votre level", Toast.LENGTH_SHORT).show()
        }
        else {
            modificationValue(1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        score = getSaveInfo("score")
        scoreLevel = getSaveInfo("scoreLevel")
        scoreLevelUp = getSaveInfo("scoreLevelUp")
        monster.setBackgroundResource(getSaveInfo("image"))

        if (scoreLevel >= 2) {
            Log.i("enter autoclcik -> ", "$scoreLevel")
            autoClick()
        }
        openSettings()
    }

    override fun onResume() {
        super.onResume()

        score = getSaveInfo("score")
        scoreLevel = getSaveInfo("scoreLevel")
        scoreLevelUp = getSaveInfo("scoreLevelUp")
        modificationValue(0)
       if (SettingsHandler.instance.chooseImage == 0) {
            monster.setBackgroundResource(getSaveInfo("image"))
        }
        if (SettingsHandler.instance.chooseBackground == 0) {
            constrainId.setBackgroundResource(getSaveInfo("background"))
        }
        initListener()
        playSound()
        if (scoreLevel >= 2) {
            Log.i("enter autoclcik -> ", "$scoreLevel")
            autoClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        if (mediaClick != null) {
            mediaClick!!.release()
            mediaClick = null
        }
    }
}