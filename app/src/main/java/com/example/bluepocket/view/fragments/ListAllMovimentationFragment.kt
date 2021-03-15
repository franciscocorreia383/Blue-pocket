package com.example.bluepocket.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.data.DatabaseMovimentation
import com.example.bluepocket.model.Movimentation
import com.example.bluepocket.view.adapter.AllMovimentationsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class ListAllMovimentationFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseMovimentation: DatabaseMovimentation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_list_all_movimentation, container, false)

        mRecyclerView = view.findViewById(R.id.recycleview_all_movimentation)

        return view
    }


    override fun onStart() {
        super.onStart()
        load()
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    private fun load() {
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        var userID = mAuth.currentUser!!.uid.toString()

        mDatabaseMovimentation = DatabaseMovimentation()

        var listMovimentation = mDatabaseMovimentation.getAll()

        Collections.sort(listMovimentation.map {
            it.date
        })

        Collections.reverse(listMovimentation)

        val layoutManager = LinearLayoutManager(context)
        val adapter = AllMovimentationsAdapter(requireContext(), listMovimentation)

        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        val movimentationRef = mDatabase.getReference("movimentation")
        val query = movimentationRef.orderByChild("userID").equalTo(userID).
        addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("BluePocket", p0.message)
                Log.e("BluePocket", p0.details)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val movimentation = it.getValue(Movimentation::class.java)

                    listMovimentation.add(movimentation!!)
                    listMovimentation.sortBy {
                        it.date
                    }
                    listMovimentation.reverse()
                    adapter.notifyDataSetChanged()
                }
            }

        })
    }



}