package ayds.songinfo.moredetails.data.external

import ayds.songinfo.moredetails.domain.entity.Biography
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

interface externalBiographyRepository{
    fun getArticleFromService(artistName: String): Biography.ArtistBiography
}

internal class externalBiographyRepository_Imp(private val lastFMAPI: LastFMAPI): externalBiographyRepository{

    override fun getArticleFromService(artistName: String): Biography.ArtistBiography {

        var artistBiography = Biography.ArtistBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            artistBiography = getArtistBioFromExternalData(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return artistBiography
    }

    private fun getArtistBioFromExternalData(
        serviceData: String?,
        artistName: String
    ): Biography.ArtistBiography {
        val gson = Gson()
        val jobj = gson.fromJson(serviceData, JsonObject::class.java)

        val artist = jobj["artist"].getAsJsonObject()
        val bio = artist["bio"].getAsJsonObject()
        val extract = bio["content"]
        val url = artist["url"]
        val text = extract?.asString ?: "No Results"

        return Biography.ArtistBiography(artistName, text, url.asString)
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

}