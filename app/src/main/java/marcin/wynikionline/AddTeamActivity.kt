package marcin.wynikionline

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_team.*

class AddTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team)

        val event = intent.getParcelableExtra("Event") as Event
        toolbar.title = event.name
    }

    fun addTeam(view: View) {
        if(teamNameEditText.text.isEmpty()) {
            toast("Podaj nazwę drużyny.")
        } else {
            val data = Intent()

            val team = Team()
            team.name = teamNameEditText.text.toString()

            data.putExtra("Team", team)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}
