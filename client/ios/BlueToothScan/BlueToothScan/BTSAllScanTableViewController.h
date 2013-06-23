//
//  BTSAllScanTableViewController.h
//  BlueToothScan
//
//  Created by ripper on 5/26/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "BTSscan.h"

@interface BTSAllScanTableViewController : UITableViewController <UITableViewDataSource, UITableViewDelegate>
@property BTSscan *bs;

@end
