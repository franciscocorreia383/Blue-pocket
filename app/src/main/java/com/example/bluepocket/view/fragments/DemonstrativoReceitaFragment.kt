package com.example.bluepocket.view.fragments

import ReceitasMovimentationAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.model.Movimentation
import com.example.bluepocket.view.adapter.TopFiveReceitasAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class DemonstrativoReceitaFragment : Fragment(), View.OnClickListener {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mReceita: TextView
    private lateinit var mSpinnerMes: Spinner
    private lateinit var mSpinnerAno: Spinner
    private lateinit var mButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_demonstrativo_receitas, container, false)

        mButton = view.findViewById(R.id.demonstrativo_button_select)
        mSpinnerAno = view.findViewById(R.id.demonstrativo_spinner_ano)
        mSpinnerMes = view.findViewById(R.id.demonstrativo_spinner_mes)
        mReceita = view.findViewById(R.id.demonstrativo_textview_receita)
        mRecyclerView = view.findViewById(R.id.demonstrativo_recycleview_receita)
        mButton.setOnClickListener(this)

        loadSpinners()

        return view
    }


    private fun loadSpinners() {

        val meses = arrayListOf<String>()

        val ano = arrayListOf<String>()

        for (i in 1..12) {
            meses.add("$i")
        }

        for (i in 2010..2020) {
            ano.add("$i")
        }

        val arrayAdapterMes =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, meses)
        val arrayAdapterAno =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, ano)

        mSpinnerMes.adapter = arrayAdapterMes
        mSpinnerAno.adapter = arrayAdapterAno

        mSpinnerAno.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

        }

        mSpinnerMes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

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

    override fun onStart() {
        super.onStart()
        //load()
    }

    override fun onResume() {
        super.onResume()
        //load()
    }

    override fun onClick(v: View?) {
        load()
    }

    private fun load() {
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        var userID = mAuth.currentUser!!.uid

        var listMovimentation = mutableListOf<Movimentation>()


        val layoutManager = LinearLayoutManager(context)
        val adapter = TopFiveReceitasAdapter(
            requireContext(),
            listMovimentation as ArrayList<Movimentation>
        )

        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        var value:Double = 0.0
        val movimentationRef = mDatabase.getReference("movimentation")
        val query = movimentationRef.orderByChild("userID").equalTo(userID)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("BluePocket", p0.message)
                    Log.e("BluePocket", p0.details)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val movimentation = it.getValue(Movimentation::class.java)

                        if (!movimentation!!.expense) {
                            if (movimentation.date.contains("/" + mSpinnerMes.selectedItem.toString() + "/" + mSpinnerAno.selectedItem.toString())){
                            listMovimentation.add(movimentation)
                                value += movimentation.value
                                mReceita.text = value.toString()
                                listMovimentation.sortBy { it.value }
                                listMovimentation.reverse()
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }

            })


     //   mReceita.text = value.toString()

    }


}


