package marcin.wynikionline

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list_item_team.view.*

class DatabaseEvent(private val context: Context,
                    private val adapter: TeamAdapter,
                    private val teamsList: ArrayList<Team>,
                    event: Event) {
    private val firestoreDB = FirebaseFirestore.getInstance()
    private val eventsCollectionRef = firestoreDB.collection("Events")
    private val teamsCollectionRef = eventsCollectionRef.document(event.id).collection("Team")

    init {
        loadTeamsList()
    }

    fun addTeam(team: Team) {
        teamsCollectionRef
                .add(team.toMap())
                .addOnSuccessListener { doc ->
                    team.id = doc.id
                    teamsList.add(team)
                    adapter.notifyDataSetChanged()
                    toast("Dodano drużynę " + team.name + ".")
                }
                .addOnFailureListener{
                    toast("Błąd przy dodawaniu drużyny.")
                }
    }

    fun removeTeam(team: Team) {
        teamsCollectionRef.document(team.id)
                .delete()
                .addOnSuccessListener {
                    teamsList.remove(team)
                    adapter.notifyDataSetChanged()
                    toast("Usunięto drużynę " + team.name + ".")
                }
                .addOnFailureListener{
                    toast("Błąd przy usuwaniu drużyny.")
                }
    }

    private fun loadTeamsList() {
        teamsCollectionRef
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result) {
                            val team = doc.toObject(Team::class.java)
                            team.id = doc.id
                            teamsList.add(team)

                        }
                        adapter.notifyDataSetChanged()
                    }
                }
    }

    fun updateTeam(team: Team, view: View) {
        teamsCollectionRef.document(team.id)
                .update(team.toMap())
                .addOnSuccessListener {
                    view.race1textView.text = "Przejazd nr 1:  " + team.score[0]
                    view.race2textView.text = "Przejazd nr 2:  " + team.score[1]
                    toast("Poprawnie zapisano wyniki.")
                }
                .addOnFailureListener{
                    toast("Błąd zapisu.")
                }
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}