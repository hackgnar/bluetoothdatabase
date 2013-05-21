//
//  BTSscan.h
//  BlueToothScan
//
//  Created by ripper on 5/3/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BluetoothManager.h"
#import "BluetoothDevice.h"
#import "BTSCurrentLocation.h"
#import "BTSRestTransport.h"

@interface BTSscan : NSObject

@property BluetoothManager *btManager;
@property NSString *btData;
@property NSMutableArray *deviceList, *allDevices;
@property BTSCurrentLocation *locationHandler;
@property BTSRestTransport *restTransport;
@property NSNumber *deviceCount;

+(BTSscan *)sharedInstance;
-(void)subscibeToDiscovery;
-(void)scan;
-(void)dd:(NSNotification *)x;
-(void)resendAll;
-(void)sendToServer:(NSDictionary *)deviceAsDict;
@end
