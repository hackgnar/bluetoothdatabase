//
//  BTSscan.m
//  BlueToothScan
//
//  Created by ripper on 5/3/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import "BTSscan.h"
//#import "BTSCurrentLocation.h"

@implementation BTSscan
@synthesize btManager, btData, deviceList, locationHandler,deviceCount, allDevices, uniqDevices;

+ (BTSscan *)sharedInstance
{
    static BTSscan *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[BTSscan alloc] init];
        // Do any other initialisation stuff here
    });
    return sharedInstance;
}

-(id)init{
    self = [super init];
    if (self){
        [self setBtManager:[BluetoothManager sharedInstance]];
    }
    [self setBtData:@"nothing yet..."];
    [self setDeviceList:[[NSMutableArray alloc]init]];
    locationHandler = [[BTSCurrentLocation alloc]init];
    [self setRestTransport:[[BTSRestTransport alloc]init]];
    [self setDeviceCount:0];
    [self setAllDevices:[[NSMutableArray alloc]init]];
    [self setUniqDevices:[[NSMutableDictionary alloc]init]];
    
    return self;
}

-(void)subscibeToDiscovery{
    [[NSNotificationCenter defaultCenter]
     addObserver:self
     selector:@selector(dd:)
     name:@"BluetoothDeviceDiscoveredNotification" object:nil];
    
    [NSTimer scheduledTimerWithTimeInterval:20
                                     target:self
                                   selector:@selector(scan)
                                   userInfo:nil repeats:YES];
}

//rename this to something more appropriate and rename x to device
-(void)dd:(NSNotification *)x{
    // counter value... Should be able to delete this if I store all devices seen this session
    int value = [[self deviceCount] intValue];
    [self setDeviceCount:[NSNumber numberWithInt:value + 1]];
    
    //extract bt device from notification arg
    BluetoothDevice *bt = [x object];
    
    //create list of strings name/address and stick 35 in a list and create s string from them
    int maxSize=35;
    NSString *foo = [[NSString alloc]initWithFormat:@"%@:%@", bt.name, bt.address];
    [[self deviceList] addObject:foo];
    if ([deviceList count] > maxSize) {
        NSRange r;
        r.location = 0;
        r.length = [deviceList count] - maxSize;
        [deviceList removeObjectsInRange:r];
    }
    [self setBtData:[deviceList componentsJoinedByString:@"\n"]];
    
    
    //create a json object from the latest bt object
    CLLocationCoordinate2D bar = [[locationHandler currentLocation] coordinate];
    NSDictionary *dic = [[NSDictionary alloc] initWithObjectsAndKeys:
                         @"iOS", @"clientType",
                         @"2.0", @"clientVersion",
                         bt.name, @"bluetoothName", 
                         bt.address, @"bluetoothAddress",
                         [[NSString alloc]initWithFormat:@"%f", bar.latitude],@"latatude", 
                         [[NSString alloc]initWithFormat:@"%f", bar.longitude],@"longitude", 
                         [[NSString alloc]initWithFormat:@"%f", [[[locationHandler currentLocation] timestamp] timeIntervalSince1970]],@"timestamp",
                         [[NSNumber alloc]initWithInt:bt.type], @"type",
                         [[NSNumber alloc]initWithUnsignedInt:bt.majorClass], @"deviceMajor",
                         [[NSNumber alloc]initWithUnsignedInt:bt.minorClass], @"deviceMinor",
                         nil];
    
    [[self allDevices] addObject:dic];
    [[self uniqDevices] setValue:dic forKey:[dic valueForKey:@"bluetoothAddress"]];
    
    //NSLog(@"%@", [self allDevices]);
    
    //NSLog(@"%@", dic);
    [self sendToServer:dic];
}

-(void)scan{
    NSLog(@"scanning now... Scanning enabled: %d", [[self btManager] deviceScanningEnabled]);
    if ([[self btManager] deviceScanningEnabled] != 1){
        [[self btManager] setDeviceScanningEnabled:YES];
        //[[self btManager] scanForConnectableDevices:1];
        [[self btManager] scanForServices:0xFFFFFFFF];
    }
    //NSLog(@"%d", [[self btManager] deviceScanningEnabled]);
    
    //[NSNotificationCenter defaultCenter];
    //NSNotification* notification = [NSNotification notificationWithName:@"MyNotification" object:self];
    //[[NSNotificationCenter defaultCenter] postNotification:notification];
}

-(void)sendToServer:(NSDictionary *)deviceAsDict{
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:deviceAsDict
                                                       options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                         error:&error];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    [[self restTransport]sendRequest:jsonString];
}

-(void)resendAll{
    for (int i=0; i < [[self allDevices]count]; i++) {
        [self sendToServer:[[self allDevices] objectAtIndex:i]];
    }
}

@end
