package com.rntxplayer;

import android.content.Context;
import android.view.Choreographer;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.HashMap;
import java.util.Map;

public class TXSurfaceView extends FrameLayout {
    public TXVodPlayer txyunVodPlayer;
    public String playSource;
    private TXCloudVideoView txyunVoidView;

    public TXSurfaceView(@NonNull Context context) {
        super(context);
        setupLayoutHack();
        txyunVodPlayer = new TXVodPlayer(context);
        txyunVoidView = new TXCloudVideoView(context);
        txyunVodPlayer.setPlayerView(txyunVoidView);
        addView(txyunVoidView);
    }

    private void setupLayoutHack() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren();
                getViewTreeObserver().dispatchOnGlobalLayout();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    private void manuallyLayoutChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    public void setSource(String src) {
        playSource = src;
        txyunVodPlayer.startPlay(src);
    }

    public void reloadPlay() {
        txyunVodPlayer.startPlay(playSource);
    }
}
