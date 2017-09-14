package com.laucherish.purestatelayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by laucherish on 2017/9/14.
 */

public class PureStateLayout extends FrameLayout{

    View loadingView, errorView, emptyView, contentView;

    public static final int STATE_LOADING = 0x1;
    public static final int STATE_ERROR = 0x2;
    public static final int STATE_EMPTY = 0x3;
    public static final int STATE_CONTENT = 0x4;
    int displayState = -1;

    int loadingLayoutId, errorLayoutId, emptyLayoutId, contentLayoutId;

    static final int RES_NONE = -1;

    OnStateChangeListener stateChangeListener;

    public PureStateLayout(@NonNull Context context) {
        this(context, null);
    }

    public PureStateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PureStateLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttrs(context, attrs);
    }

    private void setupAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PureStateLayout);
        loadingLayoutId = typedArray.getResourceId(R.styleable.PureStateLayout_p_loadingLayoutId, R.layout.view_pure_loading);
        errorLayoutId = typedArray.getResourceId(R.styleable.PureStateLayout_p_errorLayoutId, R.layout.view_pure_error);
        emptyLayoutId = typedArray.getResourceId(R.styleable.PureStateLayout_p_emptyLayoutId, R.layout.view_pure_empty);
        contentLayoutId = typedArray.getResourceId(R.styleable.PureStateLayout_p_contentLayoutId, RES_NONE);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount > 1) {
            throw new IllegalStateException("PureStateLayout can only host 1 elements");
        } else {
            if (loadingLayoutId != RES_NONE) {
                loadingView = inflate(getContext(), loadingLayoutId, null);
                addView(loadingView);
            }
            if (errorLayoutId != RES_NONE) {
                errorView = inflate(getContext(), errorLayoutId, null);
                addView(errorView);
            }
            if (emptyLayoutId != RES_NONE) {
                emptyView = inflate(getContext(), emptyLayoutId, null);
                addView(emptyView);
            }
            if (contentLayoutId != RES_NONE) {
                contentView = inflate(getContext(), contentLayoutId, null);
                addView(contentView);
            }

            if (contentView == null) {
                if (childCount == 1) {
                    contentView = getChildAt(0);
                }
            }
            if (contentView == null) {
                throw new IllegalStateException("contentView can not be null");
            }

            for (int index = 0; index < getChildCount(); index++) {
                getChildAt(index).setVisibility(GONE);
            }

            if (loadingView != null) {
                setDisplayState(STATE_LOADING);
            } else {
                setDisplayState(STATE_CONTENT);
            }
        }

    }


    public void setDisplayState(int newState) {
        int oldState = displayState;

        if (newState != oldState) {

            switch (newState) {
                case STATE_LOADING:
                    notifyStateChange(oldState, newState, loadingView);
                    break;
                case STATE_ERROR:
                    notifyStateChange(oldState, newState, errorView);
                    break;
                case STATE_EMPTY:
                    notifyStateChange(oldState, newState, emptyView);
                    break;
                case STATE_CONTENT:
                    notifyStateChange(oldState, newState, contentView);
                    break;
            }

        }
    }

    private View getDisplayView(int oldState) {
        if (oldState == STATE_LOADING) return loadingView;
        if (oldState == STATE_ERROR) return errorView;
        if (oldState == STATE_EMPTY) return emptyView;
        return contentView;
    }


    private void notifyStateChange(int oldState, int newState, View enterView) {
        if (enterView != null) {

            displayState = newState;

            if (oldState != -1) {
                getStateChangeListener().onStateChange(oldState, newState);
                getStateChangeListener().animationState(getDisplayView(oldState), enterView);
            } else {
                enterView.setVisibility(VISIBLE);
                enterView.setAlpha(1);
            }
            if (newState == STATE_ERROR) {
                this.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getStateChangeListener().onRetryClick();
                    }
                });
            }
        }
    }


    public int getState() {
        return displayState;
    }

    public void showContent() {
        setDisplayState(STATE_CONTENT);
    }

    public void showEmpty() {
        setDisplayState(STATE_EMPTY);
    }

    public void showError() {
        setDisplayState(STATE_ERROR);
    }

    public void showLoading() {
        setDisplayState(STATE_LOADING);
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public View getErrorView() {
        return errorView;
    }

    public View getContentView() {
        return contentView;
    }

    public PureStateLayout loadingView(View loadingView) {
        if (this.loadingView != null) {
            removeView(this.loadingView);
        }
        this.loadingView = loadingView;
        addView(this.loadingView);
        this.loadingView.setVisibility(GONE);
        return this;
    }

    public PureStateLayout errorView(View errorView) {
        if (this.errorView != null) {
            removeView(this.errorView);
        }
        this.errorView = errorView;
        addView(this.errorView);
        this.errorView.setVisibility(GONE);
        return this;
    }

    public PureStateLayout emptyView(View emptyView) {
        if (this.emptyView != null) {
            removeView(this.emptyView);
        }
        this.emptyView = emptyView;
        addView(this.emptyView);
        this.emptyView.setVisibility(GONE);
        return this;
    }

    public PureStateLayout contentView(View contentView) {
        if (this.contentView != null) {
            removeView(this.contentView);
        }
        this.contentView = contentView;
        addView(this.contentView);
        this.contentView.setVisibility(GONE);
        return this;
    }

    public void setStateChangeListener(OnStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public OnStateChangeListener getStateChangeListener() {
        if (stateChangeListener == null) {
            stateChangeListener = getDefaultStateChangeListener();
        }
        return stateChangeListener;
    }

    private OnStateChangeListener getDefaultStateChangeListener() {
        return new SimpleStateChangeListener();
    }

    public interface OnStateChangeListener {
        void onStateChange(int oldState, int newState);

        void animationState(View exitView, View enterView);

        void onRetryClick();
    }

    public static class SimpleStateChangeListener implements OnStateChangeListener {

        @Override
        public void onStateChange(int oldState, int newState) {

        }

        @Override
        public void animationState(final View exitView, final View enterView) {
            AnimatorSet set = new AnimatorSet();
            final ObjectAnimator enter = ObjectAnimator.ofFloat(enterView, View.ALPHA, 1f);
            ObjectAnimator exit = ObjectAnimator.ofFloat(exitView, View.ALPHA, 0f);
            set.playTogether(enter, exit);
            set.setDuration(300);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    enterView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    exitView.setAlpha(1);
                    exitView.setVisibility(GONE);
                    checkView(enterView);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            set.start();
        }

        private void checkView(View enterView) {
            int visibleChild = 0;
            FrameLayout parent = (FrameLayout) enterView.getParent();
            int childCount = parent.getChildCount();
            for (int index = 0; index < childCount; index++) {
                if (parent.getChildAt(index).getVisibility() == VISIBLE) {
                    visibleChild++;
                }
            }
            if (visibleChild < 1) {
                enterView.setVisibility(VISIBLE);
                enterView.setAlpha(1);
            }
        }

        @Override
        public void onRetryClick() {

        }
    }
}
