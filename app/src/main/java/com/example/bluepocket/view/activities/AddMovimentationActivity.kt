package com.example.bluepocket.view.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.bluepocket.R
import com.example.bluepocket.data.DatabaseType
import com.example.bluepocket.model.Movimentation
import com.example.bluepocket.model.Type
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.add_type.view.*
import java.text.SimpleDateFormat
import java.util.*

class AddMovimentationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mButtonAddType: ImageButton
    private lateinit var mButtonAddMovimentation: Button
    private lateinit var mName: TextView
    private lateinit var mOptionSpinner: Spinner
    private lateinit var mValue: TextView
    private lateinit var mReceita: RadioButton
    private lateinit var mDespesa: RadioButton
    private lateinit var mButtonDate: Button
    private lateinit var mDate: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    private var mDatabaseType = DatabaseType()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movimentation)

        mButtonAddMovimentation = findViewById(R.id.add_button_addmovimentation)
        mOptionSpinner = findViewById(R.id.add_spinner_type)
        mButtonAddType = findViewById(R.id.add_button_addtype)
        mName = findViewById(R.id.add_edittext_name)
        mValue = findViewById(R.id.add_edittext_value)
        mReceita = findViewById(R.id.add_radio_receita)
        mDespesa = findViewById(R.id.add_radio_despesa)
        mButtonDate = findViewById(R.id.add_button_date)
        mDate = findViewById(R.id.add_textview_datetv)

        loadType()

        mButtonAddType.setOnClickListener(this)
        mButtonAddMovimentation.setOnClickListener(this)
        mButtonDate.setOnClickListener(this)

    }

    override fun onRestart() {
        super.onRestart()
        loadType()
    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        loadType()

    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.add_button_addtype -> {
                addType()
            }
            R.id.add_button_addmovimentation -> {
                validationMovimentation()
            }
            R.id.add_button_date -> {
                datePickerSelect()
            }

        }

    }

    private fun datePickerSelect() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDayOfMonth ->
                mDate.setText("" + mDayOfMonth + "/" + mMonth + "/" + mYear)

            },
            year,
            month,
            day
        )

        datePicker.show()


    }

    private fun addMovimentation() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)

        progressDialog.show()
        val name = mName.text.toString()
        val type = mOptionSpinner.selectedItem.toString()
        var date = mDate.text.toString()

        val despesa = mDespesa.isChecked
        val valor = mValue.text.toString().toDouble()
        val userID = mAuth.currentUser!!.uid

        val movimentation = Movimentation(
            name = name,
            type = type,
            date = date,
            expense = despesa,
            value = valor,
            userID = userID
        )

        //salva no DB
        val valueRef = mDatabase.getReference("movimentation")
        val valueId = valueRef.push().key

        valueRef.child(valueId!!).setValue(movimentation).addOnSuccessListener {

            progressDialog.dismiss()

            Toast.makeText(
                this@AddMovimentationActivity,
                "salvo com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
            .addOnFailureListener {

                progressDialog.dismiss()

                Toast.makeText(
                    this@AddMovimentationActivity,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()

            }


    }

    private fun validationMovimentation() {

        var validate = true

        if (mName.text.isEmpty()) {
            mName.error = "Este campo deve ser preenchido!"
            validate = false
        }
        if (mValue.text.isEmpty()) {
            mValue.error = "Este campo deve ser preenchido!"
            validate = false
        }
        if (mDate.text.isEmpty()) {
            mDate.error = "Este campo deve ser preenchido!"
        }
        if (!mReceita.isChecked && !mDespesa.isChecked) {
            mReceita.error = "Click"
            mDespesa.error = "Click"
            validate = false
        }

        if (validate) addMovimentation()
    }

    private fun addType() {
        val layout = LayoutInflater.from(this).inflate(R.layout.add_type, null, false)
        val inputType = layout.input_type


        val dialog = AlertDialog.Builder(this).apply {
            setView(layout)
            setNegativeButton("Cancelar", null)
            setPositiveButton("Salvar") { d, i ->

                if (inputType.text.toString() == "") {
                    inputType.error = "Este campo deve ser preenchido!"
                } else {
                    val userID = mAuth.currentUser!!.uid
                    val type = Type(name = inputType.text.toString(), userID = userID)

                    //salva no DB
                    val typeRef = mDatabase.getReference("type")
                    val typeId = typeRef.push().key

                    typeRef.child(typeId!!).setValue(type).addOnSuccessListener {

                        Toast.makeText(
                            this@AddMovimentationActivity,
                            "salvo com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadType()
                    }
                        .addOnFailureListener {

                            Toast.makeText(
                                this@AddMovimentationActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                }

            }

        }
        dialog.create().show()
    }

    private fun loadType() {

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        var userID = mAuth.currentUser!!.uid.toString()


        val categoryRef = mDatabase.getReference("type")
        val query = categoryRef.orderByChild("userID").equalTo(userID)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("BluePocker", p0.message)
                    Log.e("BluePocket", p0.details)
                    Toast.makeText(
                        this@AddMovimentationActivity, "Não foi possivel " +
                                "carregar o banco de dados", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val type = it.getValue(Type::class.java)
                        mDatabaseType.setTypeList(type!!)
                    }
                }

            })

        loadSpinner()

    }

    private fun loadSpinner() {

        val listType = mDatabaseType.getAll()

        val arrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listType)

        mOptionSpinner.adapter = arrayAdapter
        mOptionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //IMPLEMENTAR
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

        }


    }

}