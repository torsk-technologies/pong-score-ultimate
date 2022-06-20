package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.Context
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log

class SoundPlayer(context: Context) {
    private var soundPool: SoundPool
    private lateinit var assetManager: AssetManager
    private val SOUND_FOLDER = "raw"

    init {
//        assetManager = Context.assets ?: assetManager
        this.assetManager = context.assets ?: assetManager
        val audioAttributes: AudioAttributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
        soundPool = SoundPool.Builder().setMaxStreams(3).setAudioAttributes(audioAttributes).build()
    }

    private fun fetchSounds() {
        val soundFiles: String
        try {
            soundFiles = assetManager.list(SOUND_FOLDER).toString()
            Log.d("TAG", soundFiles)
        } finally {
        }

        val path: String = SOUND_FOLDER + "/" + "ding_1.mp3"
        val sound = Sound(path)
        loadSound(sound)
    }

    private fun loadSound(sound: Sound) {
        val fileDescriptor = assetManager.openFd(sound.getPathName())
        val soundID = soundPool.load(fileDescriptor, 1)
        sound.setId(soundID)
    }

    private fun playSound(sound: Sound) {
        val soundId: Int = sound.getId()
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }
}