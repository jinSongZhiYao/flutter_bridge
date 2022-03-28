#import "FlutterBridgePlugin.h"

static NSString *const kChannelName = @"flutterBridge/core";

@interface FlutterBridgePlugin ()

@property (nonatomic, strong, readwrite) NSMutableDictionary<NSString *, id<FlutterBridgeMethodProtocol>> *methodMap;
@property (nonatomic, strong) FlutterMethodChannel *channel;

@end

@implementation FlutterBridgePlugin

+ (instancetype)shareInstance {
    static FlutterBridgePlugin *instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[FlutterBridgePlugin alloc] init];
    });
    return instance;
}

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:kChannelName
                                     binaryMessenger:[registrar messenger]];
    FlutterBridgePlugin* instance = [FlutterBridgePlugin shareInstance];
    instance.channel = channel;
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        self.methodMap = [NSMutableDictionary dictionary];

    }
    
    return self;
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSString *methodName = call.method;
    NSDictionary *params = call.arguments;
    id<FlutterBridgeMethodProtocol> classHandler = [self.methodMap objectForKey:methodName];
    if (!classHandler || ![classHandler respondsToSelector:@selector(onMethodCall:params:result:)]) {
        return;
    }
    id returnResult = [classHandler onMethodCall:methodName params:params result:result];
    if (returnResult) {
        result(returnResult);
    }
}

#pragma mark Public
- (void)registerObjectHandler:(id<FlutterBridgeMethodProtocol>_Nonnull)objectHandler {
    if (objectHandler == NULL) {
        NSAssert(NO, @"classHandler不能为空");
        return;
    }
    if (![objectHandler respondsToSelector:@selector(registerMethods)]) {
        NSAssert(NO, @"请registerMethods协议");
        return;
    }
    for (NSString *methodName in [objectHandler registerMethods]) {
        [self.methodMap setValue:objectHandler forKey:methodName];
    }
}

- (void)callFlutterMethodName:(NSString *)methodName
                       params:(NSDictionary<NSString *,id> *)params
                   callResult:(void (^ _Nullable)(id _Nullable))callResult {
    if (!methodName) {
        NSAssert(NO, @"methodName不能为空");
        return;
    }
    [self.channel invokeMethod:methodName arguments:params result:^(id  _Nullable result) {
        if (callResult) {
            callResult(result);
        }
    }];
}

- (void)removeMethod:(NSString *)methodName {
    if (methodName.length == 0 ) {
        NSAssert(NO, @"methodName不能为空");
        return;
    }
    if ([self.methodMap objectForKey:methodName]) {
        [self.methodMap removeObjectForKey:methodName];
    }
}

@end
