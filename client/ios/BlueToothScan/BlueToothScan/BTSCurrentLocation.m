//
//  BTSCurrentLocation.m
//  BlueToothScan
//
//  Created by ripper on 5/5/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import "BTSCurrentLocation.h"

@implementation BTSCurrentLocation {
    CLLocationManager *locationManager;
}
@synthesize currentLocation;

-(id)init{
    self = [super init];
    if (self){
        locationManager = [[CLLocationManager alloc]init];
        locationManager.delegate = self;
        locationManager.desiredAccuracy = kCLLocationAccuracyBest;
        [locationManager startUpdatingLocation];
    }
    return self;
}
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    [self setCurrentLocation:[locations lastObject]];
    //NSLog(@"didUpdateToLocation: %@", [self currentLocation]);
    
}

@end
