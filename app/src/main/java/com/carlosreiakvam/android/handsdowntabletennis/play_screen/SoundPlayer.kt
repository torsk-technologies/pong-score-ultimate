package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.Context
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import java.io.IOException

class SoundPlayer(context: Context) {
    private var soundPool: SoundPool
    private lateinit var assetManager: AssetManager
    private val SOUND_FOLDER = "sounds"
    private var soundList: ArrayList<Sound> = arrayListOf()

    init {
        this.assetManager = context.assets ?: assetManager
        val audioAttributes: AudioAttributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
        soundPool = SoundPool.Builder().setMaxStreams(3).setAudioAttributes(audioAttributes).build()
    }

    fun fetchSounds() {
        val soundFiles: Array<String> = assetManager.list(SOUND_FOLDER) as Array<String>

        for (filename in soundFiles) {
            try {
                val path: String = SOUND_FOLDER + "/" + filename
                val sound = Sound(path)
                loadSound(sound)
                soundList.add(sound)
            } catch (e: IOException) {
                Log.d("TAG", "Could not load sound $filename")
            }
        }
    }

    fun loadSound(sound: Sound) {
        val fileDescriptor = assetManager.openFd(sound.getPathName())
        val soundID = soundPool.load(fileDescriptor, 1)
        sound.setId(soundID)
    }

    fun playSound(soundId: Int) {
//        val soundId: Int = sound.getId()
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }
}