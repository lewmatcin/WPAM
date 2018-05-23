package marcin.wynikionline

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Event(
        var name: String = "",
        var id: String = "",
        var password: String = ""): Parcelable {

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["name"] = name
        result["password"] = password
        return result
    }
}