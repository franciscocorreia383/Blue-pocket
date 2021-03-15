package com.example.bluepocket.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.bluepocket.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mEmail: TextView
    private lateinit var mPassword: TextView
    private lateinit var mRegisterNewUser: TextView
    private lateinit var mForgetPassword: TextView
    private lateinit var mLoginSign: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (supportActionBar != null) supportActionBar!!.hide()

        mEmail = findViewById(R.id.login_edit_text_email)
        mPassword = findViewById(R.id.login_edit_text_password)
        mRegisterNewUser = findViewById(R.id.login_textview_create_user)
        mForgetPassword = findViewById(R.id.login_textview_forget_password)
        mLoginSign = findViewById(R.id.login_button_login)

        mLoginSign.setOnClickListener(this)
        mRegisterNewUser.setOnClickListener(this)
        mForgetPassword.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.login_button_login -> {
                validation()
            }
            R.id.login_textview_create_user -> {
                register()
            }
            R.id.login_textview_forget_password -> {
                forgetPassword()
            }
        }
    }

    private fun forgetPassword() {
        startActivity(Intent(this,
            ForgetPasswordActivity::class.java))
        finish()
    }

    private fun register() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    private fun validation() {
        var validate = true

        if (mEmail.text.isEmpty()) {
            mEmail.error = "Este campo deve ser preenchido!"
            validate = false
        }
        if (mPassword.text.isEmpty()) {
            mPassword.error = "Este campo deve ser preenchido!"
            validate = false
        }

        if (validate) login()
    }

    private fun login() {
        val email = mEmail.text.toString()
        val password = mPassword.text.toString()

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)

        progressDialog.show()

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                progressDialog.dismiss()
                val dialog = AlertDialog.Builder(this@LoginActivity)
                    .setMessage("Endereço de email ou senha são inválidos!")
                    .setTitle("Droid TODO")
                    .setCancelable(false)
                    .setNeutralButton("OK") { dialog, id ->
                        dialog.dismiss()
                        finish()
                    }.create()
                dialog.show()
            }
        }
    }
}