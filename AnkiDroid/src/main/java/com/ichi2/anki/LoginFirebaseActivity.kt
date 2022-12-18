package com.ichi2.anki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class LoginFirebaseActivity : AppCompatActivity() {
    private lateinit var ivBack: ImageView
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnSignIn: Button
    private lateinit var resetPass: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_firebase)
        auth = FirebaseAuth.getInstance()
        initViews()
        initBinding()
    }

    private fun initViews() {
        ivBack = findViewById(R.id.iv_back)
        etEmail = findViewById(R.id.edit_text_email)
        etPassword = findViewById(R.id.edit_text_password)
        btnSignIn = findViewById(R.id.btn_login)
        resetPass = findViewById(R.id.tv_reset_pass)
    }

    private fun initBinding() {
        ivBack.setOnClickListener {
        }
        btnSignIn.setOnClickListener {
            if (hasEmptyFields())
                return@setOnClickListener
            signInUser(etEmail.text?.toString() ?: "", etPassword.text?.toString() ?: "")
        }

        resetPass.setOnClickListener {
            val email = etEmail.text?.toString()
            if (email.isNullOrEmpty())
                Toast.makeText(this, "Please enter your email address!", Toast.LENGTH_SHORT).show()
            else
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset password link sent to email address", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithEmail:success")
                    startActivity(Intent(this, DeckPicker::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "${task.exception?.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                    Timber.d("signInWithEmail:failure", task.exception)
                }
            }
    }

    private fun hasEmptyFields(): Boolean {
        var hasEmptyFields = false
        val email = etEmail.text?.toString()
        val password = etPassword.text?.toString()
        if (email.isNullOrEmpty() || password.isNullOrEmpty())
            hasEmptyFields = true
        return hasEmptyFields
    }
}
