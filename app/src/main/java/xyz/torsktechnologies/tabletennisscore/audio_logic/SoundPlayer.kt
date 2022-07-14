package xyz.torsktechnologies.tabletennisscore.audio_logic

import android.content.Context
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import java.io.IOException

class SoundPlayer(context: Context) {
    private var soundPool: SoundPool
    private lateinit var assetManager: AssetManager
    private val SOUNDFOLDER = "sounds"
    private var soundList: ArrayList<Sound> = arrayListOf()

    init {
        this.assetManager = context.assets ?: assetManager
        val audioAttributes: AudioAttributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
        soundPool = SoundPool.Builder().setMaxStreams(4).setAudioAttributes(audioAttributes).build()
    }


    fun fetchSounds() {
        val soundFiles: Array<String> = assetManager.list(SOUNDFOLDER) as Array<String>

        for (filename in soundFiles) {
            try {
                val path = "$SOUNDFOLDER/$filename"
                val sound = Sound(path)
                loadSound(sound)
                soundList.add(sound)
            } catch (e: IOException) {
            }
        }
    }

    private fun loadSound(sound: Sound) {
        val fileDescriptor = assetManager.openFd(sound.pathName)
        sound.id = soundPool.load(fileDescriptor, 1)
    }

    fun playSound(soundId: Int) {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }


    fun release() {
        soundPool.release()
    }
}