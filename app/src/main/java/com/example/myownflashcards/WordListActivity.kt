package com.example.myownflashcards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_word_list.*

class WordListActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    lateinit var realm: Realm
    lateinit var results: RealmResults<WordDataBase>
    lateinit var word_list: ArrayList<String>
    lateinit var adapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)

        //TOP画面で設定した背景画像を引き継ぐ
        ConstraintLayoutWordList.setBackgroundResource(intBackground)

        //新しい単語追加ボタンを押した場合
        //edit activityへ移動(ステータスをインテントで渡す)
        add.setOnClickListener {
            val intent = Intent (this@WordListActivity, ListEditActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_add))
            startActivity(intent)
        }
        //backボタンのクリック処理
        back.setOnClickListener {
            finish()
        }

        //Todo 「暗記済みは下」ボタンを押下した際の処理
        //暗記フラグが記録されている単語を下にソート
        buttonSort.setOnClickListener{
            results = realm.where<WordDataBase>(WordDataBase::class.java).findAll().sort(getString(R.string.db_field_memory_flag))
            //ソート前に表示されている表を一度まっさらにする
            word_list.clear()
            results.forEach{
                if (it.boolMemoryFlag){
                    word_list.add(it.strAnswer + " : " + it.strQuestion + "暗記済だよ")
                } else {
                    word_list.add(it.strAnswer + " : " + it.strQuestion)
                }
            }
            //まっさらな表にソート後のデータを書き加える
            listview.adapter = adapter
        }

        //リストのクリックリスナー
        listview.setOnItemClickListener(this)
        listview.setOnItemLongClickListener(this)



    }

    override fun onResume() {

        super.onResume()

        realm = Realm.getDefaultInstance()
        //DBに登録されている単語を一覧で表示
        results = realm.where(WordDataBase::class.java).findAll().sort(getString(R.string.db_field_answer))

        // 暗記済みのモノを「暗記済み」と表示

        word_list = ArrayList()
        val length = results.size
//        for (i in 0..length -1 ){
//            if (results[i].boolMemoryFlag){
//                word_list.add(results[i].strAnswer + ":" + results[i].strQuestion + "暗記済だよ")
//            } else{
//                word_list.add(results[i].strAnswer + ":" + results[i].strQuestion)
//            }
//        }

        results.forEach{
            //itがresults[i]を指す
            if (it.boolMemoryFlag){
                word_list.add(it.strAnswer + " : " + it.strQuestion + "暗記済だよ")
            }else {
                word_list.add(it.strAnswer + " : " + it.strQuestion)
            }
            word_list.add(it.strAnswer + " : " + it.strQuestion)
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, word_list)
        listview.adapter = adapter

    }

    override fun onPause() {
        super.onPause()
        realm.close()

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        //リスト内の単語をタップした場合
        //タップした項目をDBから取得
        val selectedDB = results[position]
        val strSelectedQuestion = selectedDB.strQuestion
        val strSelectedAnswer = selectedDB.strAnswer
        //list_editを開く
        val intent = Intent(this@WordListActivity, ListEditActivity::class.java)
        intent.putExtra(getString(R.string.intent_key_question), strSelectedQuestion) //問題
        intent.putExtra(getString(R.string.intent_key_answer),strSelectedAnswer) //答え
        intent.putExtra(getString(R.string.intent_key_position), position) //行番号
        intent.putExtra(getString(R.string.intent_key_status), getString(R.string.status_change)) //ステータス
        startActivity(intent)

    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {

        //Todo 単語削除前に確認メッセージ表示の処理

        //リスト内の単語を長押しした場合
        //長押しした項目をDBから取得
        val selectedDB = results.get(position)

        val dialog = AlertDialog.Builder(this@WordListActivity).apply{
            setTitle(selectedDB.strAnswer + "の削除")
            setMessage("削除していいの？")
            setPositiveButton("いいよ！"){ dialog, which ->
                //取得した内容をDBから削除
                realm.beginTransaction()
                selectedDB.deleteFromRealm()
                realm.commitTransaction()
                //取得した内容を一覧からも削除
                word_list.removeAt(position)
                //DBから単語帳データを再取得して表示
                listview.adapter = adapter
            }
            setNegativeButton("アカン！"){dialog, which -> }
            show()
        }

        //長押し処理が一度終われば、再度タップしないと別の処理をしない
        return true
    }


    //もどるボタンをタップした場合
    //今の画面を閉じてTOP画面へ移動
}
