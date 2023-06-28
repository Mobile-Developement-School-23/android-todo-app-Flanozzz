package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.Model.ToDoItem
import com.example.todolist.Utils.getDeviceId
import com.example.todolist.api.RetrofitInstance
import com.example.todolist.api.ToDoApi
import com.example.todolist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var api: ToDoApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = RetrofitInstance.api

        val devId = getDeviceId(this)

//        val items = listOf(
//            ElementRequest("1e", "Купить что-то1", "important", 1691836648, false, null,0, 0, devId),
//            ElementRequest("2r", "Купить что-то", "low", 0, false, null,0, 0, devId),
//            ElementRequest("3t", "Купить что-то, где-то, зачем-то, но зачем не очень понятно", "low", 0, false, null,0, 0, devId),
//            ElementRequest("6y", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", "low", 1691836648, true, null,0, 0, devId),
//            ElementRequest("7", "Купить что-то", "basic", 0, false, null,0, 0, devId),
//            ElementRequest("8", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", "low", 0, true, null,0, 0, devId),
//            ElementRequest("9", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрррррррррррррррррррррррррр", "low", 0, false, null,0, 0, devId),
//            ElementRequest("q", "Купить что-то", "basic", 0, true, null,0, 0, devId),
//            ElementRequest("w", "Купить что-то", "basic", 0, false, null,0, 0, devId),
//            ElementRequest("e", "Купить что-то", "important", 0, true, null,0, 0, devId),
//            ElementRequest("r", "Купить что-то", "basic", 0, false, null,0, 0, devId),
//            ElementRequest("t", "Купить что-то", "important", 0, true, null,0, 0, devId),
//            ElementRequest("y", "Купить что-то", "basic", 0, false, null,0, 0, devId),
//            ElementRequest("u", "Купить что-то", "basic", 0, true, null,0, 0, devId),
//            ElementRequest("i", "Купить что-то", "low", 0, false, null,0, 0, devId),
//            ElementRequest("o", "Купить что-то", "basic", 0, true, null,0, 0, devId),
//        )

        val el = ToDoItem(UUID.randomUUID().toString(), "Купить что-то1", ToDoItem.Importance.HIGH, 1691836648, false, null,0, 0, devId)

        CoroutineScope(Dispatchers.IO).launch {
            //val r = api.addElement(85, ElementRequest(el))
            //val r = api.updateElement(75, ElementRequest(el.copy(text="yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy")), "259da886-1596-4eac-9226-048b5224ab48")
            //val r = api.getList()
            //val r = api.getElement("259da886-1596-4eac-9226-048b5224ab48")
            //val r = api.updateList(77, ListRequest(listOf(ElementRequest(el))))
            //val r = api.deleteElement(getLastKnownRevision(), "af2a3c8c-7182-4ba2-baf1-62c05b6c242e")
            //Log.e("AAA", r.body().toString())
        }
    }
}