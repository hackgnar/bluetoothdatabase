//
//  BTSCurrentLocation.h
//  BlueToothScan
//
//  Created by ripper on 5/5/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

@interface BTSCurrentLocation : NSObject <CLLocationManagerDelegate>
@property CLLocation *currentLocation;

@end
