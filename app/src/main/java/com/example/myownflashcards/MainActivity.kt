package com.example.myownflashcards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

//トップレベルでの宣言<static>
var intBackground = 0

class MainActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//    QUIZを押下した際の処理
//    EDITを押下した際の処理
    edit.setOnClickListener {
        val intent = Intent(this@MainActivity, WordListActivity::class.java)
        startActivity(intent)
    }

//    背景ボタンを押下した際の処理
//    画面背景をボタンに合わせて設定

    button01.setOnClickListener {
        intBackground = R.drawable.mo7t
        constrainMain.setBackgroundResource(intBackground)
    }
    button02.setOnClickListener {
        intBackground = R.drawable.new_game_292
        constrainMain.setBackgroundResource(intBackground)
    }
    button03.setOnClickListener {
        intBackground = R.drawable.new_game_301
        constrainMain.setBackgroundResource(intBackground)
    }
    button04.setOnClickListener {
        intBackground = R.drawable.sdfhs
        constrainMain.setBackgroundResource(intBackground)
    }
    button05.setOnClickListener {
        intBackground = R.drawable.shg
        constrainMain.setBackgroundResource(intBackground)
    }
    button06.setOnClickListener {
        intBackground = R.drawable.new_game_317
        constrainMain.setBackgroundResource(intBackground)
    }
        //下記の記述方法もあるが、上記のように変数を用いないと、違うクラスに引き渡せない。
//    button02.setOnClickListener { constrainMain.setBackgroundResource(R.drawable.new_game_292) }
//    button03.setOnClickListener { constrainMain.setBackgroundResource(R.drawable.new_game_301) }
//    button04.setOnClickListener { constrainMain.setBackgroundResource(R.drawable.sdfhs) }
//    button05.setOnClickListener { constrainMain.setBackgroundResource(R.drawable.shg) }
//    button06.setOnClickListener { constrainMain.setBackgroundResource(R.drawable.new_game_317) }

        //Todo 確認テストボタンを押した際
        //テスト画面へ推移
        //選択したテスト条件も反映する
        buttonQuiz.setOnClickListener {
            val intetnt = Intent(this@MainActivity,TestActivity::class.java)
            when(radioGroup2.checkedRadioButtonId){
                //暗記済み単語を含ませない（y/n）
                //y
                R.id.radioButton -> intent.putExtra(getString(R.string.intent_key_memory_flag), true)
                //n
                R.id.radioButton2 -> intent.putExtra(getString(R.string.intent_key_memory_flag), false)
            }
        }
    }



}
