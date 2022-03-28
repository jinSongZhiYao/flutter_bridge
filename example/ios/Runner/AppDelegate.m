#import "AppDelegate.h"
#import "GeneratedPluginRegistrant.h"

#import "FlutterBridgeTest.h"
#import "FlutterBridgePlugin.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  [GeneratedPluginRegistrant registerWithRegistry:self];
  [[FlutterBridgePlugin shareInstance] registerObjectHandler:[[FlutterBridgeTest alloc] init]];
    
  return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

@end
