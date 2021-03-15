package com.example.bluepocket.data

import com.example.bluepocket.model.Type

class DatabaseType {

    var typeList = mutableListOf<Type>(

        Type(name = "Faculdade", userID = "0"),
        Type(name = "Alimentação", userID = "0"),
        Type(name = "Veículo", userID = "0")

    )

    fun getAll(): List<String> {

        return typeList.map {
            it.name
        }

    }

    fun setTypeList(item: Type){

        typeList.add(item)

    }

}