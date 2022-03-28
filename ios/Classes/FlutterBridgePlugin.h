#import <Flutter/Flutter.h>

@protocol FlutterBridgeMethodProtocol;
@interface FlutterBridgePlugin : NSObject<FlutterPlugin>

+ (instancetype _Nonnull)shareInstance;
- (void)registerObjectHandler:(id<FlutterBridgeMethodProtocol>_Nonnull)objectHandler;
- (void)callFlutterMethodName:(NSString *_Nonnull)methodName
                       params:(NSDictionary<NSString *, id> *_Nullable)params
                       callResult:(void (^_Nullable)(id _Nullable result))callResult;
- (void)removeMethod:(NSString *_Nonnull)methodName;


@end

@protocol FlutterBridgeMethodProtocol <NSObject>

@required
- (NSArray<NSString *> *_Nonnull)registerMethods;
- (id _Nullable)onMethodCall:(NSString *_Nonnull)methodName
                      params:(NSDictionary<NSString *, id> *_Nullable)params
                      result:(void (^_Nullable)(id _Nullable result))result;

@end

