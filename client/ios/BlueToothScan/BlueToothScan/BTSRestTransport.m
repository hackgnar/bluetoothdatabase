//
//  BTSRestTransport.m
//  BlueToothScan
//
//  Created by ripper on 5/5/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import "BTSRestTransport.h"

@implementation BTSRestTransport
@synthesize baseurl, connections, request;

-(id)init{
    self = [super self];
    if (self){
        [self setBaseurl:@"http://bluetoothdatabase.com/datacollection/"];
        NSURL *dataUrl = [[NSURL alloc]initWithString:[self baseurl]];
        [self setRequest:[[NSMutableURLRequest alloc]initWithURL:dataUrl]];
        [[self request] setHTTPMethod:@"POST"];
        [[self request] setValue:@"application/json" forHTTPHeaderField:@"Content-type"];
    }
    return self;
}

-(void)sendRequest:(NSString *)msg{
    [[self request] setValue:[NSString stringWithFormat:@"%ld", (unsigned long)[msg length]]forHTTPHeaderField:@"Content-length"];
    [[self request] setHTTPBody:[msg dataUsingEncoding:NSUTF8StringEncoding]];
    __unused NSURLConnection *internalConnection = [[NSURLConnection alloc]
                                                    initWithRequest:[self request]
                                                    delegate:self
                                                    startImmediately:YES];
}

@end
