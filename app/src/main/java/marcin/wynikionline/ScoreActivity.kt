package marcin.wynikionline

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_score.*

class ScoreActivity : AppCompatActivity() {

    private var team: Team? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        team = intent.getParcelableExtra("Team") as Team
        toolbar.title = team?.name
        race1editText.setText(team!!.score[0])
        race2editText.setText(team!!.score[1])
    }

    fun saveScore(view: View) {
        team!!.score[0] = race1editText.text.toString()
        team!!.score[1] = race2editText.text.toString()

        val data = Intent()
        data.putExtra("Team", team)
        setResult(Activity.RESULT_OK, data)

        finish()
    }
}
