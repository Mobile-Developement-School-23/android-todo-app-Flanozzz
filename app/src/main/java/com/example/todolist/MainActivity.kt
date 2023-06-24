package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.todolist.Model.ListElement
import com.example.todolist.Model.ToDoItem
import com.example.todolist.RetorfitTest.ListRequest
import com.example.todolist.Utils.getDeviceId
import com.example.todolist.api.RetrofitInstance
import com.example.todolist.api.ToDoApi
import com.example.todolist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
//    private lateinit var api: ToDoApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        api = RetrofitInstance.api
//
//        val devId = getDeviceId(this)
//
//        val items = listOf(
//            ListElement("1e", "Купить что-то1", "important", 1691836648, false, null,0, 0, devId),
//            ListElement("2r", "Купить что-то", "low", 0, false, null,0, 0, devId),
//            ListElement("3t", "Купить что-то, где-то, зачем-то, но зачем не очень понятно", "low", 0, false, null,0, 0, devId),
//            ListElement("6y", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", "low", 1691836648, true, null,0, 0, devId),
//            ListElement("7", "Купить что-то", "basic", 0, false, null,0, 0, devId),
//            ListElement("8", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", "low", 0, true, null,0, 0, devId),
//            ListElement("9", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", "low", 0, false, null,0, 0, devId),
//            ListElement("q", "Купить что-то", "basic", 0, true, null,0, 0, devId),
//            ListElement("w", "Купить что-то", "basic", 0, false, null,0, 0, devId),
//            ListElement("e", "Купить что-то", "important", 0, true, null,0, 0, devId),
//            ListElement("r", "Купить что-то", "basic", 0, false, null,0, 0, devId),
//            ListElement("t", "Купить что-то", "important", 0, true, null,0, 0, devId),
//            ListElement("y", "Купить что-то", "basic", 0, false, null,0, 0, devId),
//            ListElement("u", "Купить что-то", "basic", 0, true, null,0, 0, devId),
//            ListElement("i", "Купить что-то", "low", 0, false, null,0, 0, devId),
//            ListElement("o", "Купить что-то", "basic", 0, true, null,0, 0, devId),
//        )
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val r = api.updateList(5, ListRequest(items, 5))
//            Log.w("AAA", r.toString())
//            val responseGet = api.getList()
//            if(responseGet.isSuccessful){
//                Log.w("AAA", responseGet.body().toString())
//            }
//        }
    }
}