//
//  BTSUniqueDevicesTableViewController.h
//  BlueToothScan
//
//  Created by ripper on 5/27/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "BTSscan.h"

@interface BTSUniqueDevicesTableViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate>
@property BTSscan *bs;

@end
