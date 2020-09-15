import React, { PureComponent } from 'react';
import { UIManager, findNodeHandle, requireNativeComponent } from 'react-native';
import PropTypes from 'prop-types';

export default class TXViewPlayer extends PureComponent {
  componentWillUnmount() {
    this.stopPlay();
    this.destroyPlay();
  }

  _assignRoot = (component) => {
    this._root = component;
  };

  _dispatchCommand = (command, params = []) => {
    if (this._root) {
      UIManager.dispatchViewManagerCommand(findNodeHandle(this._root), command, params);
    }
  };

  setNativeProps = (props) => {
    if (this._root) {
      this._root.setNativeProps(props);
    }
  };

  // 开始播放。
  startPlay = () => {
    this._dispatchCommand('startPlay');
  };

  // 暂停播放
  pausePlay = () => {
    this._dispatchCommand('pausePlay');
  };

  // 停止播放
  stopPlay = () => {
    this._dispatchCommand('stopPlay');
  };

  // 重载播放
  reloadPlay = () => {
    this._dispatchCommand('reloadPlay');
  };

  // 重新播放
  restartPlay = () => {
    this._dispatchCommand('restartPlay');
  };

  // 释放。释放后播放器将不可再被使用
  destroyPlay = () => {
    this._dispatchCommand('destroyPlay');
  };

  // 跳转到指定位置,传入单位为秒
  seekTo = (position = 0) => {
    if (typeof position === 'number') {
      this._dispatchCommand('seekTo', [position]);
    }
  };

  render() {
    return <RCTVideo ref={this._assignRoot} {...this.props} />;
  }
}

TXViewPlayer.propTypes = {
  // 基础配置
  source: PropTypes.string, // 播放地址
  setAutoPlay: PropTypes.bool, // 设置自动播放
  setLoop: PropTypes.bool, // 设置循环播放
  setMute: PropTypes.bool, // 设置播放器静音
  enableHardwareDecoder: PropTypes.bool, // 开启硬解。默认开启
  setSpeed: PropTypes.number, // 设置倍速播放:支持0.5~2倍速的播放
  setMirrorMode: PropTypes.bool, // 是否镜像
  setRotateMode: PropTypes.oneOf([0, 1, 2, 3]), // 设置旋转 0:0度;1:90度;2:180度;3:270度;
  setRenderMode: PropTypes.oneOf([0, 1]), // 设置画面缩放模式 0:自适应; 1:平铺;
  configHeader: PropTypes.object, // 配置自定义header
  selectBitrateIndex: PropTypes.number, // 设置码率切换

  onTXVodLoading: PropTypes.func, // 缓冲开始。
  onTXVodLoadingEnd: PropTypes.func, // 缓冲结束
  onTXVodProgress: PropTypes.func, // 播放中
  onTXVodEnd: PropTypes.func, // 播放结束。
  onTXVodError: PropTypes.func, // 播放出错
  onTXVodBegin: PropTypes.func, // 开始播放
  onTXVodPrepare: PropTypes.func, // 准备
  onTXVodBitrateChange: PropTypes.func, // 切换码率
};

TXViewPlayer.defaultProps = {
  setAutoPlay: false,
  setRenderMode: 0,
};

const RCTVideo = requireNativeComponent('RNTXplayer');
