package com.carlosreiakvam.android.handsdowntabletennis.audio_logic

class Sound(var pathName: String) {

    var fileName: String
    var id: Int = 0
        set(value) {
            field = value
        }

    init {
        val splitPath = pathName.split("/")
        fileName = splitPath[splitPath.size - 1]
    }



//    fun setId(id: Int) {
//        this.id = id
//    }
}