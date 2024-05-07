package ayds.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import ayds.songinfo.moredetails.domain.entity.Biography
import ayds.songinfo.moredetails.fulllogic.LASTFM_IMAGE_URL
import com.squareup.picasso.Picasso
import java.util.Locale


private fun updateUi(artistBiography: Biography.ArtistBiography) {
    runOnUiThread {
        updateOpenUrlButton(artistBiography)
        updateLastFMLogo()
        updateArticleText(artistBiography)
    }
}

private fun updateOpenUrlButton(artistBiography: Biography.ArtistBiography) {
    openUrlButton.setOnClickListener {
        navigateToUrl(artistBiography.articleUrl)
    }
}

private fun navigateToUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setData(Uri.parse(url))
    startActivity(intent)
}

private fun updateLastFMLogo() {
    Picasso.get().load(LASTFM_IMAGE_URL).into(lastFMImageView)
}

private fun updateArticleText(artistBiography: Biography.ArtistBiography) {
        val text = artistBiography.biography.replace("\\n", "\n")
        articleTextView.text = Html.fromHtml(textToHtml(text, artistBiography.artistName))
    }

 private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

companion object {
    const val ARTIST_NAME_EXTRA = "artistName"
}


