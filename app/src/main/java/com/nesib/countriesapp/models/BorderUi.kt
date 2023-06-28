package com.nesib.countriesapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "borders")
data class BorderUi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val key: String,
    val flag: String,
    val name: String,
)