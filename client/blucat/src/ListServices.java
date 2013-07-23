/* 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

import com.intel.bluetooth.BluetoothConsts;

/**
 * 
 * @author Joseph Paul Cohen
 *
 */
public class ListServices {

	
	public static void listServices(String target) throws IOException, InterruptedException{
		
		

		
	}
	
	
	
	public static void listServices(final boolean verbose) throws IOException, InterruptedException{
		
		System.out.println("#" + "Listing all services");
		Set<ServiceRecord> records = findViaSDP(verbose);

	}
	
	
	
	
	
	
	
	
	
	final static Object serviceSearchCompletedEvent = new Object();
	
	
	
	static Set<ServiceRecord> findViaSDP(final boolean verbose) throws IOException, InterruptedException{
		
		Set<ServiceRecord> toReturn = new HashSet<ServiceRecord>();
		
		UUID[] uuidSet ={
			BluetoothConsts.RFCOMM_PROTOCOL_UUID,
//			BluetoothConsts.L2CAP_PROTOCOL_UUID,
//			BluetoothConsts.OBEX_PROTOCOL_UUID,
//			new UUID(0x0003)

			
			
		};
		
        int[] attrIDs =  new int[] {
                0x0100 // Service name
                ,0x0003
        };
		
        
        RemoteDeviceDiscovery.findDevices();
		Set<RemoteDevice> devices  = RemoteDeviceDiscovery.getDevices();
		
		
		for (RemoteDevice remote : devices){
        
	        synchronized(serviceSearchCompletedEvent) {
	        	
	            System.out.println("#" + "Searching for services on ");
	            System.out.println("+," + RemoteDeviceDiscovery.deviceName(remote, verbose));
	        
	            LocalDevice.getLocalDevice().getDiscoveryAgent()
	            .searchServices(attrIDs, uuidSet, remote, new ServiceDiscoveryListener(toReturn, verbose));
	            
	            serviceSearchCompletedEvent.wait();
	        }
		
		}
		return toReturn;
	}
	
	
	static class ServiceDiscoveryListener implements DiscoveryListener{

		Set<ServiceRecord> toReturn;
		boolean verbose;
		
		public ServiceDiscoveryListener(Set<ServiceRecord> toReturn, boolean verbose) {
			
			this.toReturn = toReturn;
			this.verbose = verbose;
		}
		
		@Override
		public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
			//System.out.println("deviceDiscovered");
		}

		@Override
		public void inquiryCompleted(int arg0) {
			//System.out.println("done");
			
		}

		@Override
		public void serviceSearchCompleted(int arg0, int arg1) {
			
			//System.out.println("service search completed!");
            synchronized(serviceSearchCompletedEvent){
                serviceSearchCompletedEvent.notifyAll();
            }
			
		}

		@Override
		public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
			
			//System.out.println(arg1);
			for (ServiceRecord servRec : arg1) {
				printServiceRecord(servRec, verbose);
				toReturn.add(servRec);
            }
		}
	}
	
	
	private static void printServiceRecord(ServiceRecord rec, boolean verbose){
		
		try{
			String name = "";
			if (rec.getAttributeValue(0x0100) != null)
				name = "" + rec.getAttributeValue(0x0100).getValue();
			
			String desc = "";
			if (rec.getAttributeValue(0x0003) != null)
				desc = "" + rec.getAttributeValue(0x0003).getValue();
			
			
			String url = rec.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			if (url != null)
				url = url.substring(0, url.indexOf(";"));
			
			
			String remoteMac = rec.getHostDevice().getBluetoothAddress();
			String remoteName = rec.getHostDevice().getFriendlyName(false);
			
			System.out.print("-," + (new Date()).getTime() + ", \"" + BluCatUtil.clean(remoteMac) + "\", \"" + BluCatUtil.clean(remoteName) + "\", ");
			System.out.println("\"" + BluCatUtil.clean(name) + "\", \"" + BluCatUtil.clean(desc) + "\", " + BluCatUtil.clean(url));
			
			
			if (verbose){
				System.out.println("#  " + " Attributes Returned " + rec.getAttributeIDs().length );
				for (int i : rec.getAttributeIDs()){
					DataElement val = rec.getAttributeValue(i);
					String sval = val.toString();
					
					
					System.out.println("#  " + String.format("0x%04x",i) + "=" + sval);
				}
			}
			
			
			}catch(Exception e){
				
				System.out.println("#Error: " + e.getMessage());
				e.printStackTrace();
			}
		
	}
	
	

	
	
}
