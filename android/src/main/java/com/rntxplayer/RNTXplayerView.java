package com.rntxplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXBitrateItem;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RNTXplayerView extends ViewGroupManager<TXSurfaceView> {
    private static final String REACT_CLASS = "RNTXplayer";
    private static final String TAG = REACT_CLASS;
    private RCTEventEmitter mEventEmitter;
    private TXVodPlayConfig mConfig;

    private enum Events {
        LOADING("onTXVodLoading"),
        LOADINGEND("onTXVodLoadingEnd"),
        PROGRESS("onTXVodProgress"),
        END("onTXVodEnd"),
        ERROR("onTXVodError"),
        PREPARE("onTXVodPrepare"),
        BEGIN("onTXVodBegin"),
        BITRATECHANGE("onTXVodBitrateChange");

        private final String mName;

        Events(final String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    @Override
    public void addView(TXSurfaceView parent, View child, int index) {
        super.addView(parent, child, parent.getChildCount());
    }

    @Override
    public void addViews(TXSurfaceView parent, List<View> views) {
        super.addViews(parent, views);
    }

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected TXSurfaceView createViewInstance(@NonNull ThemedReactContext reactContext) {
        mEventEmitter = reactContext.getJSModule(RCTEventEmitter.class);
        mConfig = new TXVodPlayConfig();
        TXSurfaceView view = new TXSurfaceView(reactContext);
        this.initConfig(view);
        this.initListener(view);
        return view;
    }

    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder builder = MapBuilder.builder();
        for (RNTXplayerView.Events event : RNTXplayerView.Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }

    @Override
    public void onDropViewInstance(TXSurfaceView view) {
        super.onDropViewInstance(view);
        Log.i(TAG, "onDropViewInstance: ");
        view.txyunVodPlayer.stopPlay(true);
    }

    @Override
    public void receiveCommand(TXSurfaceView view, String command, @Nullable ReadableArray args) {
        // This will be called whenever a command is sent from react-native.
        Log.i(TAG, "receiveCommand: " + command);
        switch (command) {
            case "startPlay":
                view.txyunVodPlayer.resume();
                break;
            case "pausePlay":
                view.txyunVodPlayer.pause();
                break;
            case "stopPlay":
                view.txyunVodPlayer.stopPlay(false);
                break;
            case "reloadPlay":
                view.reloadPlay();
                break;
            case "restartPlay":
                view.txyunVodPlayer.seek(0);
                view.txyunVodPlayer.resume();
                break;
            case "destroyPlay":
                view.txyunVodPlayer.stopPlay(true);
                break;
            case "seekTo":
                long position = args.getInt(0);
                Log.i(TAG, "receiveCommand: " + position);
                view.txyunVodPlayer.seek(position);
                break;
        }
    }

    @ReactProp(name = "source")
    public void source(TXSurfaceView view, String src) {
        view.setSource(src);
    }

    @ReactProp(name = "setAutoPlay")
    public void setAutoPlay(TXSurfaceView view, boolean auto) {
        view.txyunVodPlayer.setAutoPlay(auto);
    }

    @ReactProp(name = "setLoop")
    public void setLoop(TXSurfaceView view, boolean auto) {
        view.txyunVodPlayer.setLoop(auto);
    }

    @ReactProp(name = "setMute")
    public void setMute(TXSurfaceView view, boolean auto) {
        view.txyunVodPlayer.setMute(auto);
    }

    @ReactProp(name = "enableHardwareDecoder")
    public void enableHardwareDecoder(TXSurfaceView view, boolean auto) {
        view.txyunVodPlayer.enableHardwareDecode(auto);
    }

    @ReactProp(name = "setSpeed")
    public void setSpeed(TXSurfaceView view, float rate) {
        view.txyunVodPlayer.setRate(rate);
    }

    @ReactProp(name = "setMirrorMode")
    public void setMirrorMode(TXSurfaceView view, boolean mode) {
        view.txyunVodPlayer.setMirror(mode);
    }

    @ReactProp(name = "setRotateMode")
    public void setRotateMode(TXSurfaceView view, int mode) {
        switch (mode) {
            case 0:
                view.txyunVodPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_0);
                break;
            case 1:
                view.txyunVodPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_90);
                break;
            case 2:
                view.txyunVodPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_180);
                break;
            case 3:
                view.txyunVodPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_270);
                break;
        }
    }

    @ReactProp(name = "setRenderMode")
    public void setRenderMode(TXSurfaceView view, int mode) {
        switch (mode) {
            case 0:
                view.txyunVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                break;
            case 1:
                view.txyunVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                break;
        }
    }

    @ReactProp(name = "configHeader")
    public void configHeader(TXSurfaceView view, ReadableMap headerMap) {
        if (headerMap == null) {
            return;
        }
        Map<String, Object> map = headerMap.toHashMap();
        Map<String, String> headers = new HashMap<>();
        for (String key : map.keySet()) {
            headers.put(key, (String) map.get(key));
        }
        mConfig.setHeaders(headers);
        view.txyunVodPlayer.setConfig(mConfig);
    }

    @ReactProp(name = "selectBitrateIndex")
    public void selectBitrateIndex(TXSurfaceView view, int bitrateIndex) {
        view.txyunVodPlayer.setBitrateIndex(bitrateIndex);
    }

    private void initConfig(final TXSurfaceView view) {
        mConfig.setSmoothSwitchBitrate(true);
        view.txyunVodPlayer.setConfig(mConfig);
    }

    private void initListener(final TXSurfaceView view) {
        view.txyunVodPlayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle param) {
                WritableMap event = Arguments.createMap();
                event.putInt("code", i);
                if (i != 2005) {
                    Log.i(TAG, "onPlayEvent: " + i);
                }
                switch (i) {
                    case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                        WritableArray bitratesArray = new WritableNativeArray();
                        ArrayList<TXBitrateItem> bitrates = view.txyunVodPlayer.getSupportedBitrates(); //获取多码率数组
                        // 排序,清晰度越低越靠前
                        Collections.sort(bitrates, new Comparator<TXBitrateItem>() {
                            @Override
                            public int compare(TXBitrateItem t1, TXBitrateItem t2) {
                                return t1.bitrate - t2.bitrate;
                            }
                        });
                        for (TXBitrateItem item : bitrates) {
                            WritableMap map = new WritableNativeMap();
                            map.putInt("index", item.index);
                            map.putInt("width", item.width);
                            map.putInt("height", item.height);
                            map.putInt("bitrate", item.bitrate);
                            bitratesArray.pushMap(map);
                        }
                        // 默认播放清晰度最低的
                        if (!bitrates.isEmpty()) {
                            view.txyunVodPlayer.setBitrateIndex(bitrates.get(0).index);
                        }
                        event.putArray("bitrates", bitratesArray);
                        mEventEmitter.receiveEvent(view.getId(), RNTXplayerView.Events.BEGIN.toString(), event);
                        break;
                    case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                        mEventEmitter.receiveEvent(view.getId(), RNTXplayerView.Events.LOADING.toString(), event);
                        break;
                    case TXLiveConstants.PLAY_EVT_VOD_LOADING_END:
                        mEventEmitter.receiveEvent(view.getId(), Events.LOADINGEND.toString(), event);
                        break;
                    case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED:
                        mEventEmitter.receiveEvent(view.getId(), Events.PREPARE.toString(), event);
                        break;
                    case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                        event.putInt("duration", param.getInt(TXLiveConstants.EVT_PLAY_DURATION));
                        event.putInt("progress", param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS));
                        event.putInt("buffered", (int) (param.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION_MS) / 1000));
                        mEventEmitter.receiveEvent(view.getId(), RNTXplayerView.Events.PROGRESS.toString(), event);
                        break;
                    case TXLiveConstants.PLAY_EVT_PLAY_END:
                        mEventEmitter.receiveEvent(view.getId(), RNTXplayerView.Events.END.toString(), event);
                        break;
                    case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION:
                        Log.i(TAG, "onPlayEvent: PLAY_EVT_CHANGE_RESOLUTION");

                        event.putInt("index", view.txyunVodPlayer.getBitrateIndex());
                        event.putInt("width", view.txyunVodPlayer.getWidth());
                        event.putInt("height", view.txyunVodPlayer.getHeight());
                        mEventEmitter.receiveEvent(view.getId(), Events.BITRATECHANGE.toString(), event);
                        break;
                    case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                        event.putString("message", "网络连接失败");
                        mEventEmitter.receiveEvent(view.getId(), RNTXplayerView.Events.ERROR.toString(), event);
                        break;
                    case TXLiveConstants.PLAY_ERR_GET_RTMP_ACC_URL_FAIL:
                    case TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND:
                    case TXLiveConstants.PLAY_ERR_HEVC_DECODE_FAIL:
                    case TXLiveConstants.PLAY_ERR_HLS_KEY:
                    case TXLiveConstants.PLAY_ERR_GET_PLAYINFO_FAIL:
                        event.putString("message", "播放异常");
                        mEventEmitter.receiveEvent(view.getId(), RNTXplayerView.Events.ERROR.toString(), event);
                        break;
                }
            }

            @Override
            public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

            }
        });
    }
}
