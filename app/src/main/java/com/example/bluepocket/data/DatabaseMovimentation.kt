package com.example.bluepocket.data

import com.example.bluepocket.model.Movimentation
import java.util.*
import kotlin.collections.ArrayList

class DatabaseMovimentation {

    var movimentationList = arrayListOf<Movimentation>()

    fun addMovimentation(movimentation: Movimentation){
        movimentationList.add(movimentation)
    }

    fun getAll(): ArrayList<Movimentation> {

        return movimentationList

    }

    fun getDespesas(): List<Movimentation> {

       return  movimentationList.filter {
            it.expense
        }
    }

    fun getReceitasDate(date : String): List<Movimentation> {

        var list = movimentationList.filter {
            it.date.contains(date,false)
        }
        Collections.sort(list.map {
            it.date
        })
        Collections.reverse(list)

        return list
    }


}