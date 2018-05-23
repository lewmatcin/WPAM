package marcin.wynikionline

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Team(
        var name: String = "",
        var id: String = "",
        var score: ArrayList<String> = arrayListOf("", "")): Parcelable {

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["name"] = name
        result["score"] = score
        return result
    }
}