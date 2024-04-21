package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import java.io.IOException
import java.util.Locale
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class OtherInfoWindow : Activity() {

    private var textPane1: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane1 = findViewById(R.id.textPane1)
        open(intent.getStringExtra("artistName"))
    }

    private fun getARtistInfo(artistName: String?) {
        // create
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        Log.e("TAG", "artistName $artistName")
        getARtistInfoFromDataBase(artistName, lastFMAPI)
    }

    private fun getARtistInfoFromDataBase(artistName: String?, lastFMAPI: LastFMAPI) {
        Thread {
            val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)
            val text = if (article != null) { // exists in database
                getInfoFromDataBase( article)
            } else { // get from service
                getInfoFromExternalService(lastFMAPI, artistName)
            }
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Log.e("TAG", "Get Image from $imageUrl")
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
                textPane1!!.text = Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY)
            }
        }.start()
    }

    private fun getInfoFromExternalService(lastFMAPI: LastFMAPI, artistName: String): String {
        var text1 = ""
        val callResponse: Response<String>
        try {
            callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            Log.e("TAG", "JSON " + callResponse.body())
            val gson = Gson()
            val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val artist = jobj["artist"].getAsJsonObject()
            val bio = artist["bio"].getAsJsonObject()
            val extract = bio["content"]
            val url = artist["url"]
            text1 = if (extract == null) {
                "No Results"
            } else {
                saveToDataBase(extract, artistName, url)
            }
            setURLOnClick(url)
        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }
        return text1
    }

    private fun setURLOnClick(url: JsonElement) {
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url.asString))
            startActivity(intent)
        }
    }

    private fun saveToDataBase(extract: JsonElement, artistName: String, url: JsonElement): String {
        val artistInfoModify = extract.asString.replace("\\n", "\n")
        val artistInfoTOHTML = textToHtml(artistInfoModify, artistName)
        // save to DB  <o/
        Thread {
            dataBase!!.ArticleDao().insertArticle(
                ArticleEntity(
                    artistName, artistInfoTOHTML, url.asString
                )
            )
        }
            .start()
        return artistInfoTOHTML
    }

    private fun getInfoFromDataBase(article: ArticleEntity): String {
        val text1 = "[*]" + article.biography
        val urlString = article.articleUrl
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(urlString))
            startActivity(intent)
        }
        return text1
    }

    private var dataBase: ArticleDatabase? = null
    private fun open(artist: String?) {
        dataBase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        Thread {
            dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("test"))
        }.start()
        getARtistInfo(artist)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
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
    }

}