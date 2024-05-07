package ayds.songinfo.moredetails.data

import ayds.songinfo.moredetails.data.external.externalBiographyRepository
import ayds.songinfo.moredetails.data.local.localBiographyRepository
import ayds.songinfo.moredetails.domain.BiographyRepository
import ayds.songinfo.moredetails.domain.entity.Biography

internal class BiographyRepositoryImpl(
    private val biographyLocalStorage: localBiographyRepository,
    private val biographyExternalService: externalBiographyRepository
) : BiographyRepository {

    override fun getArtistInfoFromRepository(artistName : String): Biography.ArtistBiography {

        val dbArticle = biographyLocalStorage.getArticleFromDB(artistName)

        val artistBiography: Biography.ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.markItAsLocal()
        } else {
            artistBiography = biographyExternalService.getArticleFromService(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                biographyLocalStorage.insertArtistIntoDB(artistBiography)
            }
        }
        return artistBiography
    }

    private fun Biography.ArtistBiography.markItAsLocal() = copy(biography = "[*]$biography")

    /*
    private fun getArtistName() =
        intent.getStringExtra(ARTIST_NAME_EXTRA) ?: throw Exception("Missing artist name")
    */
}

