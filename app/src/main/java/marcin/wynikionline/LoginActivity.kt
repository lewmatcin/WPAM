package marcin.wynikionline

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view: View) {
        if (nameEditText.text.isEmpty() or passwordEditText.text.isEmpty()) {
            toast("Uzupełnij brakujące pola.")
        } else submit("LOGIN")
    }

    fun register(view: View) {
        if (nameEditText.text.isEmpty() or passwordEditText.text.isEmpty() or passwordRepEditText.text.isEmpty()) {
            toast("Uzupełnij brakujące pola.")
        } else if (passwordEditText.text.toString() != passwordRepEditText.text.toString()) {
            toast("Podano różne hasła.")
        } else submit("REGISTER")
    }

    private fun submit(action: String) {
        val data = Intent()
        data.putExtra("Action", action)

        val user = User()
        user.name = nameEditText.text.toString()
        user.password = passwordEditText.text.toString()

        data.putExtra("User", user)

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    fun newAccount(view: View) {
        loginButton.visibility = View.INVISIBLE
        textView.visibility = View.INVISIBLE
        passwordRepEditText.visibility = View.VISIBLE
        registerButton.visibility = View.VISIBLE
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}
