package com.yan.statewrappertest

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import com.yan.statewrapper.StateWrapper


/**
 * Created by yan on 2017/6/23.
 */

class GStateWrapper(context: Context, adapter: RecyclerView.Adapter<*>) : StateWrapper<StateView>(context, adapter) {

    private var gStateView: StateView? = null
    private var onBtnClickListener: View.OnClickListener? = null

    init {
        gStateView = StateView(context)
    }

    fun setOnBtnClickListener(onBtnClickListener: View.OnClickListener) {
        this.onBtnClickListener = onBtnClickListener
    }

    override fun stateView(parent: ViewGroup.LayoutParams): StateView {
        gStateView?.layoutParams = parent
        onBtnClickListener?.let { gStateView?.setOnBtnClickListener(onBtnClickListener!!) }

        return gStateView as StateView
    }

    fun setState(state: Int) {
        gStateView?.setState(state)
        setShowStateView(true)
    }
}