package com.yan.statewrappertest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_data_main.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var dataList: ArrayList<String>
    private lateinit var stateWrapper: GStateWrapper
    private lateinit var adapter: RecyclerView.Adapter<*>

    private var stateIndex: Int = 0
    private var onRefreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        when (stateIndex++ % 3) {
            0 -> {
                stateWrapper.setState(1)
                srlRefresh.isRefreshing = false
            }
            1 -> {
                stateWrapper.setState(2)
                srlRefresh.isRefreshing = false
            }
            2 -> {
                srlRefresh.isRefreshing = false
                stateWrapper.setShowStateView(false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        dataList = ArrayList<String>()
        initLMR()
        dataInit()
        initSRL()
    }

    private fun initSRL() {
        srlRefresh.setOnRefreshListener(onRefreshListener)
    }

    private fun dataInit() {
        rvData.postDelayed({
            dataList.clear()
            for ((index, _) in (0 until 8).withIndex()) {
                dataList.add(index.toString())
            }
            adapter.notifyDataSetChanged()
        }, 200)
    }

    private fun initLMR() {
        rvData.layoutManager = LinearLayoutManager(baseContext)
        adapter = getAdapter()
        stateWrapper = GStateWrapper(baseContext, adapter)
        stateWrapper.setOnBtnClickListener(View.OnClickListener {
            srlRefresh.isRefreshing = true
            onRefreshListener.onRefresh()
        })
        rvData.adapter = stateWrapper
    }

    private fun getAdapter(): RecyclerView.Adapter<Holder> {
        return object : RecyclerView.Adapter<Holder>() {
            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder
                    = Holder(layoutInflater.inflate(R.layout.item_data_main, parent, false))

            override fun getItemCount(): Int = dataList.size

            override fun onBindViewHolder(holder: Holder, position: Int) {
                holder.itemView.tvData.text = dataList[position]
            }
        }
    }

    private class Holder(item: View) : RecyclerView.ViewHolder(item)
}
