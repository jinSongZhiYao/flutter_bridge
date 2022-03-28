//
//  FlutterBridgeTest.m
//  Runner
//
//  Created by jinru.lu on 2022/3/28.
//

#import "FlutterBridgeTest.h"
#import "TestViewController.h"

@implementation FlutterBridgeTest

- (NSArray *)registerMethods {
    return [NSArray arrayWithObjects:@"startEnterActivity", @"callNativeReturnMap", @"getSDKVersion", @"callNativeNoReturn", @"requestHttp", nil];
}

- (id)onMethodCall:(NSString *)methodName
            params:(NSDictionary<NSString *,id> *)params
            result:(void (^)(id _Nullable))result {
    if([methodName isEqualToString:@"startEnterActivity"]) {
        TestViewController *testVC = [[TestViewController alloc] init];
        testVC.modalPresentationStyle = UIModalPresentationFullScreen;
        [UIApplication.sharedApplication.keyWindow.rootViewController presentViewController:testVC animated:YES completion:nil];
    } else if ([methodName isEqualToString:@"callNativeReturnMap"]) {
        return [NSDictionary dictionaryWithObjects:@[@"aa", @(1), @[@1, @2, @3], @(true)] forKeys:@[@"aa", @"cc", @"dd", @"ee"]];
    } else if([methodName isEqualToString:@"getSDKVersion"]) {
        return @"V1.1.0";
    } else if([methodName isEqualToString:@"requestHttp"]) {
        NSThread  *newThread= [[NSThread alloc]initWithBlock:^{
            result(@"我是异步方法");
        }];
        [newThread start];
    } else if([methodName isEqualToString:@"callNativeNoReturn"]) {
        
    }
    return nil;
}

@end
