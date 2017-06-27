package com.yan.statewrappertest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_data_state.view.*

/**
 * Created by yan on 2017/6/23.
 */

class StateView(context: Context) : FrameLayout(context) {
    private val STATE_NO_DATA_TRY_AGAIN: String = "no data click to try again"
    private val STATE_NO_NET: String = "no net"

    private var onBtnClickListener: View.OnClickListener? = null

    fun setOnBtnClickListener(onBtnClickListener: View.OnClickListener) {
        this.onBtnClickListener = onBtnClickListener
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.view_data_state, this, true)
        tvState.setOnClickListener {
            when (tvState.text) {
                STATE_NO_DATA_TRY_AGAIN -> onBtnClickListener?.onClick(tvState)
            }
        }
    }

    fun setState(state: Int) {
        when (state) {
            1 -> tvState.text = STATE_NO_DATA_TRY_AGAIN
            2 -> tvState.text = STATE_NO_NET
        }
    }

    init {
        init()
    }

}
