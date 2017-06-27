package com.yan.statewrapper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by yan on 2017/6/23.
 */

public abstract class StateWrapper<T extends View> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_STATE = 0x147;

    private RecyclerView.Adapter innerAdapter;
    private Context context;

    private boolean isShowStateView;

    public StateWrapper(Context context, RecyclerView.Adapter adapter) {
        this.context = context;
        innerAdapter = adapter;
        try {
            innerAdapter.registerAdapterDataObserver(adapterDataObserver);
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowStateView) {
            return ITEM_TYPE_STATE;
        }
        return innerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_STATE) {
            View view;
            if (stateView(parent.getLayoutParams()) != null) {
                onStateInit((T) (view = stateView(parent.getLayoutParams())));
            } else if (stateViewId() != -1) {
                onStateInit((T) (view = LayoutInflater.from(context).inflate(stateViewId(), parent, false)));
            } else {
                throw new RuntimeException("must override stateView() or stateViewId()");
            }
            return new RecyclerView.ViewHolder(view) {
            };
        }
        return innerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isShowStateView) {
            innerAdapter.onBindViewHolder(holder, position);
        }
    }

    public void setShowStateView(boolean showStateView) {
        if (showStateView == this.isShowStateView) {
            return;
        }
        this.isShowStateView = showStateView;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(innerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowStateView) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        innerAdapter.onViewAttachedToWindow(holder);
        if (isShowStateView) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        return isShowStateView ? 1 : innerAdapter.getItemCount();
    }


    protected void onStateInit(T container) {
    }

    protected int stateViewId() {
        return -1;
    }

    protected T stateView(ViewGroup.LayoutParams parentLayoutParams) {
        return null;
    }

    /**
     * WrapperUtils adjust the layout
     */
    private static class WrapperUtils {
        interface SpanSizeCallback {
            int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position);
        }

        private static void onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter, RecyclerView recyclerView
                , final SpanSizeCallback callback) {
            innerAdapter.onAttachedToRecyclerView(recyclerView);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                    }
                });
                gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            }
        }
    }

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (itemCount == 1) {
                notifyItemChanged(positionStart);
                return;
            }
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (itemCount == 1) {
                notifyItemInserted(positionStart);
                return;
            }
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (itemCount == 1) {
                notifyItemRemoved(positionStart);
                return;
            }
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }
    };
}