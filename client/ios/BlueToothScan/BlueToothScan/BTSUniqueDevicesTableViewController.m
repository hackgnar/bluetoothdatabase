//
//  BTSUniqueDevicesTableViewController.m
//  BlueToothScan
//
//  Created by ripper on 5/27/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import "BTSUniqueDevicesTableViewController.h"

@implementation BTSUniqueDevicesTableViewController

-(id)initWithCoder:(NSCoder *)aDecoder{
    //NSLog(@"init with coder");
    self = [super initWithCoder:aDecoder];
    if (self != nil) {
        [self setBs:[BTSscan sharedInstance]];
        //NSLog(@"setting shared instance");
    }
    return self;
}


- (void)viewDidLoad
{
    UIRefreshControl *refreshControl = [[UIRefreshControl alloc] init];
    [refreshControl addTarget:self action:@selector(getNewData) forControlEvents:UIControlEventValueChanged];
    self.refreshControl = refreshControl;
    [NSTimer scheduledTimerWithTimeInterval:10
                                     target:self
                                   selector:@selector(getNewData)
                                   userInfo:nil repeats:YES];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    //NSLog(@"get count: %i", [[[self bs] allDevices] count]);
    //NSLog(@"%@", [[self bs] allDevices]);
    return [[[self bs] uniqDevices] count];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = (UITableViewCell *) [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil){
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    [[cell textLabel] setBackgroundColor:[UIColor clearColor]];
    [[cell detailTextLabel] setBackgroundColor:[UIColor clearColor]];
    
    NSArray *bar = [[[self bs] uniqDevices] keysSortedByValueUsingComparator:^(NSDictionary *addr1, NSDictionary *addr2) {
        return [addr1[@"bluetoothName"] compare:addr2[@"bluetoothName"]];
    }];
    
    //NSInteger foo = [[[self bs] allDevices] count] - 1  - indexPath.row;
    
    cell.textLabel.text = [[[[self bs] uniqDevices] objectForKey:[bar objectAtIndex:indexPath.row]] valueForKey:@"bluetoothName"];
    cell.detailTextLabel.text = [[[[self bs] uniqDevices] objectForKey:[bar objectAtIndex:indexPath.row]] valueForKey:@"bluetoothAddress"];
    return cell;
}

-(void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath{
    
}

- (void)getNewData{
    [self.tableView reloadData];
    [self.refreshControl endRefreshing];
}


@end
