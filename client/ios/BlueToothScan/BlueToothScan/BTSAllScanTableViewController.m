//
//  BTSAllScanTableViewController.m
//  BlueToothScan
//
//  Created by ripper on 5/26/13.
//  Copyright (c) 2013 Hackgnar. All rights reserved.
//

#import "BTSAllScanTableViewController.h"

@implementation BTSAllScanTableViewController
//-(id)initWithStyle:(UITableViewStyle)style{
//    NSLog(@"init with style");
//    return [super initWithStyle:style];
//}
//-(id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
//    NSLog(@"init with nib name");
//    return [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
//}
//
//
//-(id)init{
//    self = [super init];
//    NSLog(@"init method");
//    if (self != nil){
//        [self setBs:[BTSscan sharedInstance]];
//        NSLog(@"setting shared instance");
//    }
//    return self;
//}

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
    return [[[self bs] allDevices] count];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = (UITableViewCell *) [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil){
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    [[cell textLabel] setBackgroundColor:[UIColor clearColor]];
    [[cell detailTextLabel] setBackgroundColor:[UIColor clearColor]];
    
    NSInteger foo = [[[self bs] allDevices] count] - 1  - indexPath.row;
    
    //cell.textLabel.text = [[[[self bs] allDevices] objectAtIndex:indexPath.row] valueForKey:@"bluetoothName"];
    //cell.detailTextLabel.text = [[[[self bs] allDevices] objectAtIndex:indexPath.row] valueForKey:@"bluetoothAddress"];
    cell.textLabel.text = [[[[self bs] allDevices] objectAtIndex:foo] valueForKey:@"bluetoothName"];
    cell.detailTextLabel.text = [[[[self bs] allDevices] objectAtIndex:foo] valueForKey:@"bluetoothAddress"];
    //cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    return cell;
}

-(void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath{
    
}

- (void)getNewData{
    //    self.isAscending = !self.isAscending;
    //    NSSortDescriptor* sortOrder = [NSSortDescriptor sortDescriptorWithKey: @"self"
    //                                                                ascending:self.isAscending];
    //    NSArray *sortedArray = [self.letterData sortedArrayUsingDescriptors:
    //                            [NSArray arrayWithObject: sortOrder]];
    //
    //    NSUInteger index = 0;
    //
    //    for (NSString *s in sortedArray)
    //    {
    //        self.letterData[index] = s;
    //        index++;
    //    }
    //
    [self.tableView reloadData];
    [self.refreshControl endRefreshing];
}


@end
