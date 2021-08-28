package my.edu.tarc.hotayiwarehousesystem

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        emailEt = findViewById(R.id.email_login_et)
        passwordEt = findViewById(R.id.password_login)

        txtBtnSignUpAc.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            finish()
        }

        btnLoginAc.setOnClickListener {
            doLogin()
        }

        txtBtnForgetPassAc.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
            finish()
        }
    }

    private fun doLogin(){
        var email: String = emailEt.text.toString()
        var password: String = passwordEt.text.toString()

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
        }

        else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign In Successfully", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        updateUI(user)
                    }

                    else {
                        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser!=null){
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    //Add onStart method to auto sign in next time when app is launched
    public override fun onStart() {
        super.onStart()
        val intent = intent
        intent.extras
        if(intent.hasExtra("from")) {
            updateUI(null)
        }
        else{
            val currentUser = auth.currentUser
            updateUI(currentUser)
        }
    }
}