package com.momotmilosz.projektbam.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var uid: Int? = null,
    @ColumnInfo(name = "user_name") var userName: String,
    @ColumnInfo(name = "password") var password: String
)
