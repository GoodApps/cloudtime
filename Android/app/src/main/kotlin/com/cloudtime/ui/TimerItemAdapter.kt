package com.cloudtime.ui

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.cloudtime.R
import com.cloudtime.dto.Timer
import com.cloudtime.ui.common.BaseViewHolder
import com.cloudtime.ui.common.ItemAdapter
import java.text.SimpleDateFormat

public class TimerItemAdapter(
        private val timer: Timer,
        private val activity: Activity)
: ItemAdapter {

    override fun getLayoutId(): Int {
        return R.layout.timer_item
    }

    override fun onCreateViewHolder(itemView: View): BaseViewHolder {
        return Holder(itemView)
    }

    override fun onBind(holder: BaseViewHolder) {
        val h = holder as Holder

        val startTimeText = if (timer.startedAt == null) ""
                else SimpleDateFormat("h:mm a").format(timer.startedAt)
        h.textView.setText("${timer.durationInSeconds / 60}m; ${timer.title}\n" +
                "${startTimeText}")

        activity.registerForContextMenu(h.itemView)
        h.itemView.setTag(timer)
    }

    class Holder(itemView: View) : BaseViewHolder(itemView) {

        val textView = find(R.id.timer_item_text) as TextView
    }
}
