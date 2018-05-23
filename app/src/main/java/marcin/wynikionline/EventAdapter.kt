package marcin.wynikionline

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_event.view.*


class EventAdapter(context: Context,
                   private val eventsList: ArrayList<Event>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val db = Database(context, this, eventsList)

    override fun getCount(): Int {
        return eventsList.size
    }

    override fun getItem(position: Int): Any {
        return eventsList[position]
    }

    override fun getItemId(position: Int): Long {
          return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_event, parent, false)

            view.deleteEventFAB.setOnClickListener {
                db.removeEvent(eventsList[position])
            }

        } else {
            view = convertView
        }

        val event = getItem(position) as Event
        view.eventNameTextView.text = event.name
        view.eventIdTextView.text = event.id

        Picasso.get().load("https://png.icons8.com/ios/1600/robot-3-filled.png").placeholder(R.mipmap.ic_launcher).into(view.eventImageView)

        return view
    }
}
