//
//  RNTXplayer.h
//  NewsApp
//
//  Created by 宋族运 on 2020/9/11.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>
@import TXLiteAVSDK_Professional;

NS_ASSUME_NONNULL_BEGIN

@interface RNTXplayer : UIView<TXVodPlayListener>

//播放器对象
@property(nonatomic,strong) TXVodPlayer * player;
@property(nonatomic,strong) TXVodPlayConfig * config;

//定义要暴露属性
@property(nonatomic,strong) NSString * source;
@property(nonatomic,assign) BOOL  setAutoPlay;
@property(nonatomic,assign) BOOL  setLoop;
@property(nonatomic,assign) BOOL  setMute;
@property(nonatomic,assign) BOOL  enableHardwareDecoder;
@property(nonatomic,assign) float  setSpeed;
@property(nonatomic,assign) BOOL  setMirrorMode;
@property(nonatomic,assign) int  setRotateMode;
@property(nonatomic,assign) int  setRenderMode;
@property(nonatomic,strong) NSDictionary * configHeader;
@property(nonatomic,assign) int  selectBitrateIndex;

//定义要暴露的事件
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodLoading;
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodLoadingEnd;
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodProgress;
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodEnd;
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodError;
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodBegin;
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodPrepare;
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodBitrateReady;
@property (nonatomic, copy) RCTBubblingEventBlock onTXVodBitrateChange;

-(void)startPlay;
-(void)pausePlay;
-(void)stopPlay;
-(void)reloadPlay;
-(void)restartPlay;
-(void)destroyPlay;
-(void)seekTo:(int) position;

@end

NS_ASSUME_NONNULL_END
