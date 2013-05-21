//
//  BTSRestTransport.h
//  BlueToothScan
//
//  Created by ripper on 5/5/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BTSRestTransport : NSObject <NSURLConnectionDelegate, NSURLConnectionDataDelegate>
@property NSString *baseurl;
@property NSMutableArray *connections;
@property NSMutableURLRequest *request;

-(void)sendRequest:(NSString *)msg;

@end
