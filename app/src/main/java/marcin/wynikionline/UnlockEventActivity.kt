package marcin.wynikionline

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_unlock_event.*

class UnlockEventActivity : AppCompatActivity() {

    var event: Event? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock_event)

        event = intent.getParcelableExtra("Event") as Event
        toolbar.setTitle(event?.name)
    }

    fun submitPassword(view: View) {
        if (passwordEditText.text.isEmpty()) {
            toast("Podaj hasło.")
        } else if (passwordEditText.text.toString() != event!!.password) {
            toast("Podano błędne hasło.")
        } else {
            val data = Intent()
            setResult(Activity.RESULT_OK, data)
            finish()
        }

    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}
