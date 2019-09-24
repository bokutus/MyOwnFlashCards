package com.example.myownflashcards

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WordDataBase: RealmObject() {
    //フィールドの設定
    @PrimaryKey
    open var strQuestion : String = ""//問題
    open var strAnswer : String = ""//答え
    open var boolMemoryFlag: Boolean = false//暗記済みフラグ

    //Todo 単語帳DBに記載済みフラグフィールドと主キー設定
}