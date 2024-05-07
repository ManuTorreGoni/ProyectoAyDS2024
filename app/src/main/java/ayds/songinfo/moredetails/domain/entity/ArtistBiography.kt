package ayds.songinfo.moredetails.domain.entity

sealed class Biography{

    data class ArtistBiography(
        val artistName: String,
        val biography: String,
        val articleUrl: String
    ): Biography()
}
