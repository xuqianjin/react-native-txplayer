//
//  RNTXplayerManager.m
//  NewsApp
//
//  Created by 宋族运 on 2020/9/11.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "RNTXplayerManager.h"

@implementation RNTXplayerManager

RCT_EXPORT_MODULE()
//暴露属性
RCT_EXPORT_VIEW_PROPERTY(source, NSString)
RCT_EXPORT_VIEW_PROPERTY(setAutoPlay, BOOL)
RCT_EXPORT_VIEW_PROPERTY(setLoop, BOOL)
RCT_EXPORT_VIEW_PROPERTY(setMute, BOOL)
RCT_EXPORT_VIEW_PROPERTY(enableHardwareDecoder, BOOL)
RCT_EXPORT_VIEW_PROPERTY(setSpeed, float)
RCT_EXPORT_VIEW_PROPERTY(setMirrorMode, BOOL)
RCT_EXPORT_VIEW_PROPERTY(setRotateMode, int)
RCT_EXPORT_VIEW_PROPERTY(setRenderMode, int)
RCT_EXPORT_VIEW_PROPERTY(configHeader, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(selectBitrateIndex, int)
RCT_EXPORT_VIEW_PROPERTY(setSmoothSwitchBitrate, BOOL)

//暴露方法（原生调用，js回调）
RCT_EXPORT_VIEW_PROPERTY(onTXVodLoading, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTXVodLoadingEnd, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTXVodProgress, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTXVodEnd, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTXVodError, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTXVodBegin, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTXVodPrepare, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTXVodBitrateReady, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onTXVodBitrateChange, RCTBubblingEventBlock)

//暴露方法（js调用，原生回调）
RCT_EXPORT_METHOD(startPlay:(nonnull NSNumber *) reactTag){
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
        RNTXplayer * player  = (RNTXplayer *) viewRegistry[reactTag];
        [player startPlay];
    }];
}
RCT_EXPORT_METHOD(pausePlay:(nonnull NSNumber *) reactTag){
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
        RNTXplayer * player  = (RNTXplayer *) viewRegistry[reactTag];
        [player pausePlay];
    }];
}
RCT_EXPORT_METHOD(stopPlay:(nonnull NSNumber *) reactTag){
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
         RNTXplayer * player  = (RNTXplayer *) viewRegistry[reactTag];
         [player stopPlay];
     }];
}
RCT_EXPORT_METHOD(reloadPlay:(nonnull NSNumber *) reactTag){
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
         RNTXplayer * player  = (RNTXplayer *) viewRegistry[reactTag];
         [player reloadPlay];
     }];
}
RCT_EXPORT_METHOD(restartPlay:(nonnull NSNumber *) reactTag){
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
         RNTXplayer * player  = (RNTXplayer *) viewRegistry[reactTag];
         [player restartPlay];
     }];
}
RCT_EXPORT_METHOD(destroyPlay:(nonnull NSNumber *) reactTag){
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
         RNTXplayer * player  = (RNTXplayer *) viewRegistry[reactTag];
         [player destroyPlay];
     }];
}
RCT_EXPORT_METHOD(seekTo:(nonnull NSNumber *) reactTag andPosition:(nonnull NSNumber *) positon){
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
         RNTXplayer * player  = (RNTXplayer *) viewRegistry[reactTag];
         [player seekTo: [positon intValue]];
     }];
}

//重写这个方法，返回将要提供给RN使用的视图
- (UIView *)view {
    RNTXplayer * player = [[RNTXplayer alloc] init];
    return player;
}


@end
