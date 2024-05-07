package ayds.songinfo.moredetails.presentation

interface moreDetailsPresenter{
}

private const val ARTICLE_BD_NAME = "database-article"
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private lateinit var articleTextView: TextView
private lateinit var openUrlButton: Button
private lateinit var lastFMImageView: ImageView
private lateinit var articleDatabase: ArticleDatabase
private lateinit var lastFMAPI: LastFMAPI

fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_other_info)
    initViewProperties()
    initArticleDatabase()
    initLastFMAPI()
    getArtistInfoAsync()
}
private fun initViewProperties() {
    articleTextView = findViewById(R.id.textPane1)
    openUrlButton = findViewById(R.id.openUrlButton)
    lastFMImageView = findViewById(R.id.lastFMImageView)
}
private fun initArticleDatabase() {
    articleDatabase =
        Room.databaseBuilder(this, ArticleDatabase::class.java, ARTICLE_BD_NAME).build()
}
private fun initLastFMAPI() {
    val retrofit = Retrofit.Builder()
        .baseUrl(LASTFM_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    lastFMAPI = retrofit.create(LastFMAPI::class.java)
}
private fun getArtistInfoAsync() {
    Thread {
        getArtistInfo()
    }.start()
}
private fun getArtistInfo() {
    val artistBiography = getArtistInfoFromRepository()
    updateUi(artistBiography)
}

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
