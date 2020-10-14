//
//  RNTXplayer.m
//  NewsApp
//
//  Created by 宋族运 on 2020/9/11.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "RNTXplayer.h"

@implementation RNTXplayer

- (TXVodPlayer *)player{
  if (!_player) {
    _player = [[TXVodPlayer alloc] init];
    _player.vodDelegate = self;
  }
  return _player;
}

- (TXVodPlayConfig *)config{
  if (!_config) {
    _config = [[TXVodPlayConfig alloc] init];
    //smoothSwitchBitrate必须是YES，否则可能播放没声音
    // _config.smoothSwitchBitrate = YES;
  }
  return _config;
}

-(void)startPlay{
  [self.player resume];
}
-(void)pausePlay{
  [self.player pause];
}
-(void)stopPlay{
  [self.player stopPlay];
}
-(void)reloadPlay{
  [self.player startPlay:self.source];
}
-(void)restartPlay{
  [self.player seek:0];
  [self.player resume];
  
}
-(void)destroyPlay{
  dispatch_async(dispatch_get_main_queue(), ^{
    [self.player removeVideoWidget];
    self.player = nil;
  });
}
- (void)seekTo:(int)position{
  [self.player seek:position];
}

//定义要暴露属性
- (void)setSource:(NSString *)source{
  _source = source;
  [self.player setupVideoWidget:self insertIndex:0];
  [self.player setConfig:self.config];
  [self.player startPlay:self.source];
}
-(void)setSetAutoPlay:(BOOL)setAutoPlay{
  _setAutoPlay = setAutoPlay;
  [self.player setIsAutoPlay:setAutoPlay];
}
-(void)setSetLoop:(BOOL)setLoop{
  _setLoop = setLoop;
  [self.player setLoop:setLoop];
}
- (void)setSetMute:(BOOL)setMute{
  _setMute = setMute;
  [self.player setMute:setMute];
}
- (void)setEnableHardwareDecoder:(BOOL)enableHardwareDecoder{
  _enableHardwareDecoder = enableHardwareDecoder;
  [self.player setEnableHWAcceleration:enableHardwareDecoder];
}
- (void)setSetSpeed:(float)setSpeed{
  _setSpeed = setSpeed;
  [self.player setRate:setSpeed];
}
- (void)setSetMirrorMode:(BOOL)setMirrorMode{
  _setMirrorMode = setMirrorMode;
  [self.player setMirror:setMirrorMode];
}
-(void)setSetRotateMode:(int)setRotateMode{
  _setRotateMode = setRotateMode;
  switch (setRotateMode) {
    case 0:
      [self.player setRenderRotation:HOME_ORIENTATION_DOWN];
      break;
    case 1:
      [self.player setRenderRotation:HOME_ORIENTATION_RIGHT];
      break;
    case 2:
      [self.player setRenderRotation:HOME_ORIENTATION_UP];
      break;
    case 3:
      [self.player setRenderRotation:HOME_ORIENTATION_LEFT];
      break;
    default:
      break;
  }
}
- (void)setSetRenderMode:(int)setRenderMode{
  _setRenderMode = setRenderMode;
  switch (setRenderMode) {
    case 0:
      [self.player setRenderMode:RENDER_MODE_FILL_EDGE];
      break;
    case 1:
      [self.player setRenderMode:RENDER_MODE_FILL_SCREEN];
      break;
    default:
      break;
  }
}

- (void)setConfigHeader:(NSDictionary *)configHeader{
  _configHeader = configHeader;
  self.config.headers = configHeader;
  [self.player setConfig:self.config];
}

- (void)setSelectBitrateIndex:(int)selectBitrateIndex{
    _selectBitrateIndex = selectBitrateIndex;
    [self.player setBitrateIndex:selectBitrateIndex];
}

- (void)setSetSmoothSwitchBitrate:(BOOL)setSmoothSwitchBitrate{
    _setSmoothSwitchBitrate = setSmoothSwitchBitrate;
    self.config.smoothSwitchBitrate =setSmoothSwitchBitrate;
    [self.player setConfig:self.config];
}

#pragma mark - TXVodPlayListener
- (void)onPlayEvent:(TXVodPlayer *)player event:(int)EvtID withParam:(NSDictionary *)param{
  dispatch_async(dispatch_get_main_queue(), ^{
    switch (EvtID) {
      case PLAY_EVT_PLAY_BEGIN:
        if (self.onTXVodBegin) {
          self.onTXVodBegin(@{@"code":@"onTXVodBegin"});
        }
        if (self.onTXVodBitrateReady) {
              NSArray * bitrateArray = [self.player supportedBitrates];
              NSMutableArray * trackArray = [NSMutableArray array];
              for (NSInteger i=0; i<bitrateArray.count; i++) {
                  TXBitrateItem * bitrate = bitrateArray[i];
                  if (bitrate.bitrate>0) {
                      [trackArray addObject:@{@"index":@(bitrate.index),
                                              @"width":@(bitrate.width),
                                              @"height":@(bitrate.height),
                                              @"bitrate":@(bitrate.bitrate)
                      }];
                  }
                  
              }
              self.onTXVodBitrateReady(@{@"bitrates":trackArray});
          }
        break;
      case PLAY_EVT_PLAY_LOADING:
        if (self.onTXVodLoading) {
          self.onTXVodLoading(@{@"code":@"onTXVodLoading"});
        }
        break;
      case EVT_VOD_PLAY_LOADING_END:
        if (self.onTXVodLoadingEnd) {
          self.onTXVodLoadingEnd(@{@"code":@"onTXVodLoadingEnd"});
        }
        break;
      case PLAY_EVT_VOD_PLAY_PREPARED:
        if (self.onTXVodPrepare) {
          self.onTXVodPrepare(@{@"code":@"onTXVodPrepare"});
        }
        break;
      case PLAY_EVT_PLAY_PROGRESS:
        if (self.onTXVodProgress) {
          NSDictionary * para =@{@"duration":param[EVT_PLAY_DURATION],
                                 @"progress":param[EVT_PLAY_PROGRESS],
                                 @"buffered":param[EVT_PLAYABLE_DURATION]};
          self.onTXVodProgress(para);
        };
        break;
      case PLAY_EVT_PLAY_END:
        if (self.onTXVodEnd) {
          self.onTXVodEnd(@{@"code":@"onTXVodEnd"});
        }
        break;
      case PLAY_EVT_CHANGE_RESOLUTION:
        if (self.onTXVodBitrateChange) {
            NSInteger bitrateIndex = self.player.bitrateIndex;
            NSArray * bitrateArray = self.player.supportedBitrates;
            if (bitrateArray.count>0) {
                TXBitrateItem * bitrate = bitrateArray[bitrateIndex];
                self.onTXVodBitrateChange(@{@"index":@(bitrateIndex),
                                            @"width":@(bitrate.width),
                                            @"height":@(bitrate.height)
                                          });
                
            }
        }
        break;
      case PLAY_ERR_NET_DISCONNECT:
        if (self.onTXVodError) {
          self.onTXVodError(@{@"message":@"網絡連接失敗"});
        }
        break;
      case PLAY_ERR_GET_RTMP_ACC_URL_FAIL:
      case PLAY_ERR_FILE_NOT_FOUND:
      case PLAY_ERR_HEVC_DECODE_FAIL:
      case PLAY_ERR_HLS_KEY:
      case PLAY_ERR_GET_PLAYINFO_FAIL:
        if (self.onTXVodError) {
          self.onTXVodError(@{@"message":@"播放異常"});
        }
        break;
      default:
        break;
    }
  });
}

- (void)onNetStatus:(TXVodPlayer *)player withParam:(NSDictionary *)param{
  
}

@end
