package com.example.myownflashcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_list_edit.*

lateinit var realm : Realm



class ListEditActivity : AppCompatActivity() {

    var strQuestion: String = ""//問題
    var strAnswer: String = ""//答え
    var intPosition: Int = 0//行番号

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_edit)

//        Todo 画面が開いた時
//        WordListActivityから渡されたintentの受け取り
        val bundle = intent.extras
        //なんでgetstringを二回記載する必要があるのかをわすれた
        //解決=>int aa = getInt("")のgetIntと、strings.xmlから持ってくるgetString
        val stringStatus = bundle.getString(getString(R.string.intent_key_status))
        textViewStatus.text = stringStatus

        //単語修正の場合は問題と答えを表示する
        if (stringStatus == getString(R.string.status_change)){
            strQuestion = bundle.getString(getString(R.string.intent_key_question))// 問題
            strAnswer = bundle.getString(getString(R.string.intent_key_answer))// 答え
            editText.setText(strQuestion)
            editText2.setText(strAnswer)

            intPosition = bundle.getInt(getString(R.string.intent_key_position))

            //Todo 修正の場合は問題が修正できないようにする

        }

//        Todo 前画面で設定した背景色を設定
        ConstraintLayoutWordEdit.setBackgroundResource(intBackground)


//        Todo 登録ボタンを押したとき
        register.setOnClickListener {

            if (stringStatus == getString(R.string.status_add)){
//                新しい単語の追加の場合
//                単語の登録処理
                addword()
            }else {
//                登録した単語の修正
//                単語の修正処理
                changeword()

            }
//        Todo 新しい単語追加の場合
            addword()
//        Todo 登録済み単語の修正の場合
            changeword()
        }


//        Todo 戻るボタンを押した時
            back2.setOnClickListener {
//          現在の画面を閉じ、単語一覧画面へ推移
                finish()
            }
    }

    override fun onResume() {
        super.onResume()

        //realmインスタンスの取得
        realm = Realm.getDefaultInstance()
    }

    override fun onPause() {
        super.onPause()
        //終了の処理
        realm.close()
    }

    private fun changeword() {

        //Todo 修正(change word)確認ダイアログ
        //Todo 1 単語(主キー)の重複チェック
        //重複していない場合
        //(主キー・暗記済みフラグ設定に伴う変更)
        //入力文字を削除/完了メッセージ

        //重複している場合
        //登録不可のメッセージを表示(トーストで)


//        単語の修正処理
//        選択s他行番号のレコードをDBから取得
        val results = realm.where(WordDataBase::class.java).findAll().sort(getString(R.string.db_field_question))
        val selectedDB = results.get(intPosition)
//        入力した問題と答えを取得したレコードへの更新に使用
        realm.beginTransaction()
        selectedDB.strQuestion = editText.text.toString()
        selectedDB.strAnswer = editText2.text.toString()
        realm.commitTransaction()
//        入力した文字を削除
        editText.setText("")//
        editText2.setText("")
//        修正完了のメッセージを表示
        Toast.makeText(this@ListEditActivity, "修正が完了したよ", Toast.LENGTH_SHORT).show()
//        今の画面を閉じて単語一覧画面へ戻る
        finish()
    }

    private fun addword() {

        //Todo 登録処理(add word)確認ダイアログ
        //Todo 1 問題(主キー)の重複チェック
        //重複していない場合
        //(主キー・暗記済みフラグ設定に伴う変更)
        //入力文字を削除/完了メッセージ

        //重複している場合
        //登録不可のメッセージを表示(トーストで)

//        新しい単語登録処理
//        入力した問題と答えをDBに登録
        realm.beginTransaction()//開始処理
        val wordDB = realm.createObject(WordDataBase::class.java)
        wordDB.strQuestion = editText.text.toString()
        wordDB.strAnswer = editText2.text.toString()
        realm.commitTransaction()//終了処理


//        入力した文字を入力欄から削除
        editText.setText("")//
        editText2.setText("")
//        登録完了のメッセージを表示
        Toast.makeText(this@ListEditActivity, "登録が完了したよ", Toast.LENGTH_SHORT).show()

    }
}
