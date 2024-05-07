package ayds.songinfo.moredetails.presentation

data class MoreDetailsUIState(
    val articleBiography: String,
    val articleURL: String
){
    companion object {
        const val LASTFM_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}