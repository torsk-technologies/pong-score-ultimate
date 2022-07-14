package xyz.torsktechnologies.tabletennisscore.audio_logic

class Sound(var pathName: String) {

    private var fileName: String
    var id: Int = 0

    init {
        val splitPath = pathName.split("/")
        fileName = splitPath[splitPath.size - 1]
    }
}