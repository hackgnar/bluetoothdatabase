//
//  BTSViewController.m
//  BlueToothScan
//
//  Created by ripper on 5/3/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import "BTSViewController.h"
#import "BTSscan.h"


@interface BTSViewController ()
@end

@implementation BTSViewController
@synthesize btData, btScanner;

void myCallBack(CFNotificationCenterRef center, void *observer, CFStringRef name, const void *object, CFDictionaryRef userInfo)
{
    NSLog(@"CFN Name:%@ Data:%@", name, userInfo);
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.dataLabel.text = @"starting...";
	// Do any additional setup after loading the view, typically from a nib.
    btScanner = [BTSscan sharedInstance];
    
    //CFNotificationCenterAddObserver(CFNotificationCenterGetLocalCenter(), NULL, myCallBack, NULL, NULL, CFNotificationSuspensionBehaviorDeliverImmediately);
    
    [NSTimer scheduledTimerWithTimeInterval:2
                                     target:self
                                   selector:@selector(updateLabel)
                                   userInfo:nil repeats:YES];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)resend:(UIButton *)sender {
    [btScanner resendAll];
}

- (IBAction)exitApp:(UIButton *)sender {
    exit(0);
}

-(void)updateLabel{
    self.dataLabel.text = [btScanner btData];
    [self updateSessionCount];
}
-(void)updateSessionCount{
    self.sessionCount.text = [[btScanner deviceCount] stringValue];
}

@end
