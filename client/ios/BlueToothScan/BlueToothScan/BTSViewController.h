//
//  BTSViewController.h
//  BlueToothScan
//
//  Created by ripper on 5/3/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BTSscan.h"

@interface BTSViewController : UIViewController
@property BTSscan *btScanner;
@property (weak, nonatomic) IBOutlet UILabel *uniqCount;
@property (weak, nonatomic) NSString *btData;
@property (weak, nonatomic) IBOutlet UILabel *dataLabel;
@property (weak, nonatomic) IBOutlet UILabel *sessionCount;
- (IBAction)resend:(UIButton *)sender;
- (IBAction)exitApp:(UIButton *)sender;

-(void)updateLabel;

@end
