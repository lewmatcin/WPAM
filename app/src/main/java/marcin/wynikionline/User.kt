package marcin.wynikionline

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
        var name: String = "",
        var password: String = "",
        var events: ArrayList<String> = ArrayList()): Parcelable {

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["name"] = name
        result["password"] = password
        result["events"] = events

        return result
    }
}