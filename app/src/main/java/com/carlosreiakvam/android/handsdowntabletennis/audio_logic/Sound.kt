package com.carlosreiakvam.android.handsdowntabletennis.audio_logic

class Sound(private var pathName: String) {

    private var fileName: String
    private var id: Int = 0

    init {
        val splitPath = pathName.split("/")
        fileName = splitPath[splitPath.size - 1]
    }

    fun getPathName(): String {
        return pathName
    }

    fun getFileName(): String {
        return fileName
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }
}