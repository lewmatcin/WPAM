package marcin.wynikionline

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_event.*

class EventActivity : AppCompatActivity() {

    private val teamsList = ArrayList<Team>()
    private var adapter: TeamAdapter? = null
    private val requestCodeUnlockEvent = 64
    private val requestCodeAddTeam = 13
    private val requestCodeScore = 95
    private var event: Event? = null
    private var viewHolder: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        event = intent.getParcelableExtra("Event") as Event
        toolbar.title = event!!.name

        adapter = TeamAdapter(this, teamsList, event!!)
        teamListView.adapter = adapter
        teamListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            if (!adapter!!.locked) score(teamsList[position], view)
        }
    }

    private fun score(team: Team, view: View) {
        val intent = Intent(this, ScoreActivity::class.java)
        viewHolder = view
        intent.putExtra("Team", team)
        startActivityForResult(intent, requestCodeScore)
    }

    fun unlockFAB(view: View) {
        val intent = Intent(this, UnlockEventActivity::class.java)
        intent.putExtra("Event", event)
        startActivityForResult(intent, requestCodeUnlockEvent)
    }

    fun lockFAB(view: View) {
        adapter?.locked = true
        lockFAB.visibility = View.INVISIBLE
        unlockFAB.visibility = View.VISIBLE
        addScoreFAB.visibility = View.INVISIBLE
        toast("Wydarzenie zablokowane.")
    }

    fun addScoreFAB(view: View) {
        val intent = Intent(this, AddTeamActivity::class.java)
        intent.putExtra("Event", event)
        startActivityForResult(intent, requestCodeAddTeam)
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                requestCodeUnlockEvent -> {
                    adapter?.locked = false
                    unlockFAB.visibility = View.INVISIBLE
                    lockFAB.visibility = View.VISIBLE
                    addScoreFAB.visibility = View.VISIBLE
                    toast("Wydarzenie odblokowane.")
                }
                requestCodeAddTeam -> {
                    val team = data!!.extras.get("Team") as Team
                    adapter!!.db.addTeam(team)
                }
                requestCodeScore -> {
                    val team = data!!.extras.get("Team") as Team
                    adapter!!.db.updateTeam(team, viewHolder!!)
                }
            }

        }
    }
}
