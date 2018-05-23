package marcin.wynikionline

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_event.*

class AddEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
    }

    fun newEvent(view: View) {
        if(passwordEditText.text.isEmpty()) {
            Toast.makeText(this, "Uzupełnij brakujące pola.", Toast.LENGTH_SHORT).show()
        } else {
            val data = Intent()
            data.putExtra("Action", "NEW_EVENT")

            val event = Event()
            event.name = nameEditText.text.toString()
            event.password = passwordEditText.text.toString()

            data.putExtra("Event", event)

            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    fun existingEvent(view: View) {
        if (idEditText.text.isEmpty()) {
            Toast.makeText(this, "Uzupełnij brakujące pola.", Toast.LENGTH_SHORT).show()
        } else {
            val data = Intent()
            data.putExtra("Action", "EXISTING_EVENT")

            val event = Event()
            event.id = idEditText.text.toString()

            data.putExtra("Event", event)

            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

}
