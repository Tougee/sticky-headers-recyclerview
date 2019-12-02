package com.timehop.stickyheadersrecyclerview;

import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StickyRecyclerHeadersTouchListener implements RecyclerView.OnItemTouchListener {
  private final RecyclerView mRecyclerView;
  private final StickyRecyclerHeadersDecoration mDecor;
  private OnHeaderClickListener mOnHeaderClickListener;

  public interface OnHeaderClickListener {
    void onHeaderClick(View header, int position, long headerId, MotionEvent e);
  }

  public StickyRecyclerHeadersTouchListener(final RecyclerView recyclerView,
                                            final StickyRecyclerHeadersDecoration decor) {
    mRecyclerView = recyclerView;
    mDecor = decor;
  }

  public StickyRecyclerHeadersAdapter getAdapter() {
    if (mRecyclerView.getAdapter() instanceof StickyRecyclerHeadersAdapter) {
      return (StickyRecyclerHeadersAdapter) mRecyclerView.getAdapter();
    } else {
      throw new IllegalStateException("A RecyclerView with " +
          StickyRecyclerHeadersTouchListener.class.getSimpleName() +
          " requires a " + StickyRecyclerHeadersAdapter.class.getSimpleName());
    }
  }


  public void setOnHeaderClickListener(OnHeaderClickListener listener) {
    mOnHeaderClickListener = listener;
  }

  @Override
  public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent e) {
    if (this.mOnHeaderClickListener != null) {
      if (e.getAction() == MotionEvent.ACTION_DOWN) {
        int position = mDecor.findHeaderPositionUnder((int)e.getX(), (int)e.getY());
        if (position != -1) {
          View headerView = mDecor.getHeaderView(mRecyclerView, position);
          long headerId = getAdapter().getHeaderId(position);
          mOnHeaderClickListener.onHeaderClick(headerView, position, headerId, e);
          mRecyclerView.playSoundEffect(SoundEffectConstants.CLICK);
          headerView.onTouchEvent(e);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent e) { /* do nothing? */ }

  @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    // do nothing
  }
}
