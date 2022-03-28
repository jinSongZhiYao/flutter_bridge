//
//  TestViewController.m
//  Runner
//
//  Created by jinru.lu on 2022/3/28.
//

#import "TestViewController.h"
#import "FlutterBridgePlugin.h"

@interface TestViewController ()

@property (nonatomic, strong) UILabel *resultLabel;

@end

@implementation TestViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = [UIColor whiteColor];
    
    CGFloat marginY = 150.f;
    for (NSInteger index = 0; index < 6; index++) {
        NSString *btnStr = @"";
        if (index == 0) {
            btnStr = @"调用flutter方法-不带入参-返回String";
        } else if (index == 1) {
            btnStr = @"调用flutter方法-带入参-返回Map";
        } else if (index == 2) {
            btnStr = @"调用flutter方法-带入参-无返回值";
        } else if (index == 3) {
            btnStr = @"调用flutter方法-没有实现的情况";
        } else if (index == 4) {
            btnStr = @"调用flutter方法--非主线程";
        } else if (index == 5) {
            btnStr = @"dismiss";
        }
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.tag = index;
        [btn setTitle:btnStr forState:UIControlStateNormal];
        btn.backgroundColor = UIColor.redColor;
        [btn addTarget:self action:@selector(didClickBtn:) forControlEvents:UIControlEventTouchUpInside];
        btn.frame = CGRectMake(0, marginY, UIScreen.mainScreen.bounds.size.width, 45.f);
        [self.view addSubview:btn];
        marginY = 150 + (index + 1) * 50;
    }
    
    self.resultLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, marginY, UIScreen.mainScreen.bounds.size.width, 300.f)];
    self.resultLabel.text = @"显示区域";
    self.resultLabel.textColor = UIColor.blackColor;
    self.resultLabel.textAlignment = NSTextAlignmentCenter;
    self.resultLabel.font = [UIFont systemFontOfSize:14.f];
    [self.view addSubview:self.resultLabel];
}

- (void)didClickBtn:(UIButton *)btn {
    if (btn.tag == 5) {
        [self dismissViewControllerAnimated:YES completion:nil];
        return;
    }
    if (btn.tag == 0) {
        [FlutterBridgePlugin.shareInstance callFlutterMethodName:@"getFlutterVersion" params:nil callResult:^(id  _Nullable callResult) {
            self.resultLabel.text = callResult;
        }];
    } else if (btn.tag == 1) {
        NSDictionary *params = [NSDictionary dictionaryWithObjects:@[@"aa", @(1), @[@1, @2, @3], @(true)] forKeys:@[@"aa", @"cc", @"dd", @"ee"]];
        [FlutterBridgePlugin.shareInstance callFlutterMethodName:@"getFlutterMap" params:params callResult:^(id  _Nullable callResult) {
            self.resultLabel.text = [self dictionaryToJson:callResult];
        }];
    } else if (btn.tag == 2) {
        [FlutterBridgePlugin.shareInstance callFlutterMethodName:@"callFlutterNoReturn" params:nil callResult:^(id  _Nullable callResult) {
            self.resultLabel.text = @"调用flutter方法-带入参-无返回值";
        }];
    } else if (btn.tag == 3) {
        [FlutterBridgePlugin.shareInstance callFlutterMethodName:@"methodNotImplemented" params:nil callResult:^(id  _Nullable callResult) {
            self.resultLabel.text = @"调用flutter方法-没有实现的情况";
        }];
    } else if (btn.tag == 4) {
        NSThread  *newThread= [[NSThread alloc]initWithBlock:^{
            [FlutterBridgePlugin.shareInstance callFlutterMethodName:@"getFlutterVersion" params:nil callResult:^(id  _Nullable callResult) {
                self.resultLabel.text = callResult;
            }];
        }];
        [newThread start];
    }
}

- (NSString*)dictionaryToJson:(NSDictionary *)dic{
    NSError *parseError = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];

    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
}


@end
