package com.example.bluepocket.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.bluepocket.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var mForgetEmail: TextView
    private lateinit var mSendButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        if (supportActionBar!=null)supportActionBar!!.hide()

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mForgetEmail = findViewById(R.id.forgetpassword_edittext_email)
        mSendButton = findViewById(R.id.forgetpassword_button_send)

        mSendButton.setOnClickListener{
            validate()
        }
    }

    private fun validate() {
        if (mForgetEmail.text.isEmpty()){
            mForgetEmail.error = "Este campo deve ser preenchido!"
        }else{
            send()
        }
    }

    private fun send() {
        val auth = FirebaseAuth.getInstance()
        val emailAddress = mForgetEmail.text.toString()

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("DroidTODO", "Email sent.")
                    val dialog = AlertDialog.Builder(this@ForgetPasswordActivity)
                        .setMessage("Email enviado com sucesso!")
                        .setTitle("Droid TODO")
                        .setCancelable(false)
                        .setNeutralButton("OK") { dialog, id ->
                            dialog.dismiss()
                            startActivity(Intent(this,
                                LoginActivity::class.java))
                            finish()
                        }.create()
                    dialog.show()
                }
            }

    }


}