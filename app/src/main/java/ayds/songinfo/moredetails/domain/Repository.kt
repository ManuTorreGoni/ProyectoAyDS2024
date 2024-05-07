package ayds.songinfo.moredetails.domain

import ayds.songinfo.moredetails.domain.entity.Biography

interface BiographyRepository {
    fun getArtistInfoFromRepository(artistName : String): Biography.ArtistBiography
}