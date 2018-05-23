package marcin.wynikionline

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.list_item_team.view.*

class TeamAdapter(private val context: Context,
                  private val teamsList: ArrayList<Team>,
                  event: Event) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val db = DatabaseEvent(context, this, teamsList, event)
    var locked: Boolean = true

    override fun getCount(): Int {
        return teamsList.size
    }

    override fun getItem(position: Int): Team {
        return teamsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_team, parent, false)

            view.deleteTeamFAB.setOnClickListener {
                if(locked) {
                    toast("Wydarzenie zablokowane.")
                } else {
                    db.removeTeam(getItem(position))
                }
            }

        } else {
            view = convertView
        }

        val team = getItem(position)
        view.teamNameTextView.text = team.name

        view.race1textView.text = "Przejazd nr 1:  " + team.score[0]
        view.race2textView.text = "Przejazd nr 2:  " + team.score[1]

        return view
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}