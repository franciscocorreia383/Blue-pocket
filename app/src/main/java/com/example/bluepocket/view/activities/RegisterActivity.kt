package com.example.bluepocket.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.bluepocket.R
import com.example.bluepocket.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(){

    private lateinit var mRegisterName: TextView
    private lateinit var mRegisterEmail: TextView
    private lateinit var mRegisterPassword: TextView
    private lateinit var mRegisterConfirmPassword: TextView
    private lateinit var mRegisterButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (supportActionBar!=null)supportActionBar!!.hide()

        mRegisterName = findViewById(R.id.register_edit_text_name)
        mRegisterEmail = findViewById(R.id.register_edit_text_email)
        mRegisterPassword = findViewById(R.id.register_edit_text_password)
        mRegisterConfirmPassword = findViewById(R.id.register_edit_text_confirm_password)
        mRegisterButton = findViewById(R.id.register_button)

        mRegisterButton.setOnClickListener{
            validate()
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
    }

    private fun validate() {
        var validate = true
        val password = register_edit_text_password.text.toString()
        val confirmPassword = register_edit_text_confirm_password.text.toString()

        if (mRegisterName.text.isEmpty()){
            mRegisterName.error = "Este campo deve ser preenchido!"
            validate = false
        }
        if (mRegisterEmail.text.isEmpty()){
            mRegisterEmail.error = "Este campo deve ser preenchido!"
            validate = false
        }
        if (mRegisterPassword.text.isEmpty()){
            mRegisterPassword.error = "Este campo deve ser preenchido!"
            validate = false
        }
        if (mRegisterConfirmPassword.text.isEmpty()){
            mRegisterConfirmPassword.error = "Este campo deve ser preenchido!"
            validate = false

        }else if (password != confirmPassword){
            mRegisterConfirmPassword.error = "As senhas ${password} e ${confirmPassword} não conferem!"
            validate = false
        }

        if (validate) cadastro()
    }

    private fun cadastro() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)

        progressDialog.show()

        val name = mRegisterName.text.toString()
        val email = mRegisterEmail.text.toString()
        val password = mRegisterPassword.text.toString()


        //cria usuario no db firebase

        mAuth.createUserWithEmailAndPassword(email, password).
        addOnCompleteListener {

            if (it.isSuccessful){

                Log.i("BluePocket",it.result.toString())

                val user = User(name = name,email = email,password = password)

                val userRef = mDatabase.getReference("users")
                userRef.child(mAuth.currentUser!!.uid).setValue(user)

                progressDialog.dismiss()

                val dialog = AlertDialog.Builder(this@RegisterActivity)
                    .setMessage("Usuario Cadastrado com sucesso!")
                    .setTitle("BluePocket")
                    .setCancelable(false)
                    .setNeutralButton("OK") { dialog, id ->
                        dialog.dismiss()
                        startActivity(Intent(this,
                            LoginActivity::class.java))
                        finish()
                    }.create()
                dialog.show()


            }else{
                progressDialog.dismiss()

                Log.i("BluePocket",it.exception.toString())

                val dialog = AlertDialog.Builder(this@RegisterActivity)
                    .setMessage(it.exception!!.message)
                    .setTitle("BluePocket")
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