package com.cloudtime.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import com.cloudtime.R
import com.cloudtime.dto.Timer
import com.cloudtime.service.TheService
import com.cloudtime.ui.common.BaseActivity
import android.widget.Toast
import com.cloudtime.service.TheService
import com.parse.ParseQueryAdapter
import com.parse.ParseUser
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

public class MainActivity : BaseActivity() {

    private var subscription: Subscription by Delegates.notNull()
    private var listView: RecyclerView by Delegates.notNull()
    private val adapter: TimersAdapter = TimersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.main_toolbar) as Toolbar)
        initEditText()
        initListView()
    }

    private fun initEditText() {
        val editText = findViewById(R.id.main_edit) as EditText
        editText.setOnEditorActionListener { v, actionId, event ->
            if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
                addTimer(15, TimeUnit.MINUTES, editText.getText().toString())
            }
            true
        }
    }

    private fun initListView() {
        listView = findViewById(R.id.main_list) as RecyclerView
        listView.setLayoutManager(LinearLayoutManager(this))
        listView.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        loadTimers()
    }

    override fun onPause() {
        super.onPause()
        cancelLoading()
    }

    private fun addTimer(duration: Long, unit: TimeUnit, title: String) {
        TheService().addTimer(duration, unit, title)
    }

    private fun loadTimers() {
        subscription = TheService().loadTimers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("tag", "size: ${it.size()}")
                    updateAdapter(it)
                }, {
                    Log.e("tag", "error", it)
                })
    }

    private fun updateAdapter(timers: List<Timer>) {
        adapter.update(timers)
    }

    private fun cancelLoading() {
        subscription.unsubscribe()
    }
}
