package marcin.wynikionline

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.*

class Database(private val context: Context,
               private val adapter: EventAdapter,
               private val eventsList: ArrayList<Event>) {

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val eventsCollectionRef = firestoreDB.collection("Events")
    private val usersCollectionRef = firestoreDB.collection("Users")
//    var firestoreListener: ListenerRegistration? = null

    var user: User? = null
    var userId: String? = null

    fun login(name: String, password: String, textView: TextView) {
        usersCollectionRef.whereEqualTo("name", name)
                .get()
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        if (task.result.isEmpty) {
                            toast("Nieprawidłowa nazwa użytkownika.")
                        } else {
                            for (doc in task.result) {
                                val userTemp = doc.toObject(User::class.java)
                                if (password == userTemp.password) {
                                    toast("Zalogowano jako " + name + ".")

                                    textView.text = name

                                    user = userTemp
                                    userId = doc.id

                                    loadEventsList()
                                } else {
                                    toast("Nieprawidłowe hasło.")
                                }
                            }
                        }
                    } else {
                        toast("Błąd logowania")
                    }
                }
    }

    fun register(user: User, textView: TextView) {
        usersCollectionRef.whereEqualTo("name", user.name)
                .get()
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        if (task.result.isEmpty) {
                            usersCollectionRef
                                    .add(user.toMap())
                                    .addOnSuccessListener {
                                        toast("Użytkownik " + user.name + " zarejestrowany prawidłowo.")
                                        login(user.name, user.password, textView)
                                    }
                                    .addOnFailureListener{
                                        toast("Błąd rejestracji użytkownika.")
                                    }
                        } else {
                            toast("Użytkownik " + user.name + " jest już zarejestrowany.")
                        }
                    }
                }
    }

    fun logout(textView: TextView) {
        toast("Wylogowano użytkownika " + user!!.name + ".")
        user = null
        userId = null
        eventsList.removeAll(eventsList)
        textView.text = ""
        adapter.notifyDataSetChanged()

    }

//    private fun setFirestoreListener() {
//        firestoreListener = eventsCollectionRef
//                .addSnapshotListener(EventListener { documentSnapshots, e ->
//                    if (e != null) {
//                        Log.e(TAG, "Listen failed!", e)
//                        return@EventListener
//                    }
//
//                    for (doc in documentSnapshots) {
//                        val event = doc.toObject(Event::class.java)
//                        eventsList.add(event)
//                    }
//                    adapter.notifyDataSetChanged()
//                })
//    }

    private fun loadEventsList() {
        eventsCollectionRef
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result) {
                            if (user!!.events.contains(doc.id)) {
                                val event = doc.toObject(Event::class.java)
                                event.id = doc.id
                                eventsList.add(event)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
    }

    fun addNewEvent(event: Event) {
        eventsCollectionRef
                .add(event.toMap())
                .addOnSuccessListener { doc ->
                    event.id = doc.id
                    addEvent(event)
                }
                .addOnFailureListener{
                    toast("Błąd przy dodawaniu wydarzenia.")
                }
    }

    fun loadExistingEvent(event: Event) {
        eventsCollectionRef.whereEqualTo(FieldPath.documentId(), event.id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.isEmpty) {
                            toast("Wczytywane wydarzenie nie istnieje.")
                        } else {
                            for (doc in task.result) {
                                if(eventsList.any{x -> x.id == doc.id}) {
                                    toast("Wczytywane wydarzenie jest już na liście.")
                                } else {

                                    val event = doc.toObject(Event::class.java)
                                    event.id = doc.id
                                    addEvent(event)
                                }
                            }
                        }
                    } else {
                        toast("Błąd przy wczytywaniu wydarzenia.")
                    }
                }
    }

    private fun addEvent(event: Event) {
        user!!.events.add(event.id)
        usersCollectionRef.document(userId!!)
                .update(user!!.toMap())
                .addOnSuccessListener {
                    eventsList.add(event)
                    adapter.notifyDataSetChanged()
                    toast("Dodano wydarzenie " + event.name + ".")
                }
                .addOnFailureListener{
                    user!!.events.remove(event.id)
                    toast("Błąd przy dodawaniu wydarzenia.")
                }
    }

    fun removeEvent(event: Event) {
        user!!.events.remove(event.id)
        usersCollectionRef.document(userId!!)
                .update(user!!.toMap())
                .addOnSuccessListener {
                    eventsList.remove(event)
                    adapter.notifyDataSetChanged()
                    toast("Usunięto wydarzenie " + event.name + ".")
                }
                .addOnFailureListener{
                    user!!.events.add(event.id)
                    toast("Błąd przy usuwaniu wydarzenia.")
                }
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}