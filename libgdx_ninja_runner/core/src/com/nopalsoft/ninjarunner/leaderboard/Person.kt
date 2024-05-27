package com.nopalsoft.ninjarunner.leaderboard

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.Net.HttpMethods
import com.badlogic.gdx.Net.HttpResponseListener
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.PixmapTextureData
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

/**
 * Indicates the other persons playing this game in our social network.
 */
class Person(
    val accountType: AccountType,
    private val id: String,
    val name: String,
    @JvmField
    val score: Long,
    private val imageURL: String
) : Comparable<Person> {

    var image: TextureRegionDrawable? = null

    @JvmField
    val isMe = false// Indicates that this person is the user.

    fun downloadImage(listener: DownloadImageCompleteListener) {
        if (image != null) // If it exists, do not download it again.
            return

        val request = Net.HttpRequest(HttpMethods.GET)
        request.url = imageURL
        Gdx.net.sendHttpRequest(request, object : HttpResponseListener {
            override fun handleHttpResponse(httpResponse: Net.HttpResponse) {
                val bytes = httpResponse.result
                Gdx.app.postRunnable {
                    val pixmap = Pixmap(bytes, 0, bytes.size)
                    val texture = Texture(
                        PixmapTextureData(
                            pixmap,
                            pixmap.format,
                            false,
                            false,
                            true
                        )
                    )
                    pixmap.dispose()
                    image = TextureRegionDrawable(TextureRegion(texture))
                    listener.imageDownloaded()
                }
            }

            override fun failed(t: Throwable) {
                listener.imageDownloadFail()
                Gdx.app.log("EmptyDownloadTest", "Failed", t)
            }

            override fun cancelled() {
                Gdx.app.log("EmptyDownloadTest", "Cancelled")
            }
        })
    }

    // see: http://stackoverflow.com/a/15329259/3479489
    fun getScoreWithFormat(): String {
        var str = score.toString()
        val floatPos = if (str.contains(".")) {
            str.length - str.indexOf(".")
        } else {
            0
        }
        val nGroups = (str.length - floatPos - 1 - (if (str.contains("-")) 1 else 0)) / 3
        for (i in 0 until nGroups) {
            val commaPos = str.length - i * 4 - 3 - floatPos
            str = str.substring(0, commaPos) + "," + str.substring(commaPos)
        }
        return str
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Person) {
            (id == other.id) && (accountType == other.accountType)
        } else false
    }

    override fun compareTo(other: Person) = other.score.compareTo(score)

    override fun hashCode(): Int {
        var result = accountType.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + score.hashCode()
        result = 31 * result + imageURL.hashCode()
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + isMe.hashCode()
        return result
    }

    interface DownloadImageCompleteListener {
        fun imageDownloaded()

        fun imageDownloadFail()
    }
}