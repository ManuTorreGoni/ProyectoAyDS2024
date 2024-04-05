package ayds.songinfo.home.model.entities

import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class Song {
    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        var releaseDatePrecision : String,
        val spotifyUrl: String,
        val imageUrl: String,
        var isLocallyStored: Boolean = false
    ) : Song() {

        val year: String = releaseDate.split("-").first()
    }

    object EmptySong : Song()
}

