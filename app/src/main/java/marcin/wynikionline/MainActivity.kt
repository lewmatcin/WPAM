package marcin.wynikionline

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val eventsList = ArrayList<Event>()
    private var adapter: EventAdapter? = null
    private val requestCodeLogin = 5
    private val requestCodeAddEvent = 6



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = EventAdapter(this, eventsList)
        eventListView.adapter = adapter
        eventListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            openEvent(eventsList[position])
        }
    }

    private fun openEvent(event: Event) {
        val intent = Intent(this, EventActivity::class.java)
        intent.putExtra("Event", event)
        startActivity(intent)
    }

    fun addEventFAB(view: View) {
        if (adapter!!.db.user != null) {
            val intent = Intent(this, AddEventActivity::class.java)
            startActivityForResult(intent, requestCodeAddEvent)
        } else {
            toast("Użytkownik niezalogowany.")
        }
    }

    fun loginFAB(view: View) {
        if (adapter!!.db.user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, requestCodeLogin)
        } else {
            adapter!!.db.logout(eventNameTextView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                requestCodeAddEvent -> {
                    val event = data!!.extras.get("Event") as Event
                    when (data.extras.get("Action")) {
                        "NEW_EVENT" -> adapter!!.db.addNewEvent(event)
                        "EXISTING_EVENT" -> adapter!!.db.loadExistingEvent(event)
                    }
                }
                requestCodeLogin -> {
                    val user = data!!.extras.get("User") as User
                    when (data.extras.get("Action")) {
                        "LOGIN" -> adapter!!.db.login(user.name, user.password, eventNameTextView)
                        "REGISTER" -> adapter!!.db.register(user, eventNameTextView)
                    }
                }
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        adapter?.db?.firestoreListener?.remove()
//    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}
