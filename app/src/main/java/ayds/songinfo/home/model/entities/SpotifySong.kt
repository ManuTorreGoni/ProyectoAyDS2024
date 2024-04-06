package ayds.songinfo.home.model.entities

import java.time.Month
import java.time.Year

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

        val formatDate: String = formatDate()

        private fun formatDate():String{
            val dateList = releaseDate.split("-").map{it.toInt() }
            return when (releaseDatePrecision){
                "day"-> return getDayReleaseDate(dateList)
                "month"-> return getMonthReleaseDate(dateList)
                "year"-> return getYearReleaseDate(dateList)
                else -> "invalidReleaseDateException"
            }
        }
        private fun getDayReleaseDate(dateList: List<Int>): String{
            return  "${dateList[2]}/${dateList[1]}/${dateList[0]}"
        }

        private fun getMonthReleaseDate(dateList: List<Int>): String{
            val month= Month.of(dateList[1]).toString()
            return "${month},${dateList[0]}"
        }

        private fun getYearReleaseDate(dateList: List<Int>): String{
            return "${dateList[0]} ${isLeapYear(dateList[0])}"
        }

        private fun isLeapYear(year: Int): String{
            return if (Year.of(year).isLeap)
                "(leap year)"
            else
                "(not a leap year)"
        }

    }

    object EmptySong : Song()
}