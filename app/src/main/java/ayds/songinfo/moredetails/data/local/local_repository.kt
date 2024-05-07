package ayds.songinfo.moredetails.data.local

import ayds.songinfo.moredetails.domain.entity.Biography

interface localBiographyRepository{
    fun getArticleFromDB(artistName: String): Biography.ArtistBiography?
    fun insertArtistIntoDB(artistBiography: Biography.ArtistBiography)
}

class localRepository_Imp(val articleDatabase: ArticleDatabase):localBiographyRepository{

    private val articleDao = articleDatabase.ArticleDao()
    override fun getArticleFromDB(artistName: String): Biography.ArtistBiography? {
        val artistEntity = articleDao.getArticleByArtistName(artistName)
        return artistEntity?.let {
            Biography.ArtistBiography(artistName, artistEntity.biography, artistEntity.articleUrl)
        }
    }

    override fun insertArtistIntoDB(artistBiography: Biography.ArtistBiography) {
        articleDao.insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }
}