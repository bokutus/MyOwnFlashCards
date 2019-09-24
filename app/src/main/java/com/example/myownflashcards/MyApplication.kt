package com.example.myownflashcards

import android.app.Application
import io.realm.Realm

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //Realmの初期化 objectのシングルトンだから名前不要
        Realm.init(this)
    }
}