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
import javax.bluetooth.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;

/**
 * 
 * @author Joseph Paul Cohen
 *
 */
public class RemoteDeviceDiscovery {

	private static boolean debug = false;
	
    public final static Set<RemoteDevice> devicesDiscovered = new HashSet<RemoteDevice>();

    public static void runDiscovery(boolean verbose) throws Exception, IOException, InterruptedException{
    	System.out.println("#" + "Searching for devices");
    	RemoteDeviceDiscovery.findDevices();
    	for (RemoteDevice d : devicesDiscovered){
    		
    		System.out.println("-," + deviceName(d,verbose));
            try {
                RemoteDeviceDiscovery.postDevice(d);
            }
            catch (Exception ex){
            }
    	}
    	
    	System.out.println("#" + "Found " + devicesDiscovered.size() + " device(s)");
    }
    
    
    
    public static void findDevices() throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();

        devicesDiscovered.clear();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                if (debug) System.out.println("#" + "Device " + btDevice.getBluetoothAddress() + " found");
                devicesDiscovered.add(btDevice);
                try {
                	if (debug) System.out.println("#" + "     name " + btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                }
            }

            public void inquiryCompleted(int discType) {
            	if (debug) System.out.println("#" + "Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            	if (debug) System.out.println("#" + "servicesDiscovered");
            }
        };

        synchronized(inquiryCompletedEvent) {
            
        	//ServiceRecord.
        	
        	LocalDevice ld = LocalDevice.getLocalDevice();
        	//ld.getRecord()
        	
        	if (debug) System.out.println("#My Name is:" + ld.getFriendlyName());
        	
//        	boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.LIAC, listener);
//            if (started) {
//            	if (debug) System.out.println("wait for device inquiry to complete...");
//                inquiryCompletedEvent.wait();
//                if (debug) System.out.println(devicesDiscovered.size() +  " device(s) found");
//            }
        	
        	
        	boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
            	if (debug) System.out.println("#" + "wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                if (debug) System.out.println("#" + devicesDiscovered.size() +  " device(s) found");
            }
        }
        
    }

    public static Set<RemoteDevice> getDevices(){
    	
    	return devicesDiscovered;
    }
    
    public static void postDevice(RemoteDevice d) throws Exception {
        String url = "http://bluetoothdatabase.com/datacollection/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "blucat");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
        String urlParameters = deviceJson(d);
 
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
 
        int responseCode = con.getResponseCode();
 
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
 
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    public static String deviceName(RemoteDevice d, boolean verbose){
    	
    	String address = d.getBluetoothAddress();
		
		String name = "";
		try{
			name = d.getFriendlyName(false);
		}catch (IOException e){
			if (verbose) System.out.println("#Error: " + e.getMessage());
			try{
				name = d.getFriendlyName(false);
			}catch (IOException e2){
    			if (verbose) System.out.println("#Error: " + e2.getMessage());
			}
			
		}
		
		
		return 	(new Date()).getTime() + ", \"" +
				BluCatUtil.clean(address) + "\", " +
				"\"" + BluCatUtil.clean(name) + "\", " +
				"Trusted:" + d.isTrustedDevice() + ", " +
				"Encrypted:" + d.isEncrypted();
    }

    public static String deviceJson(RemoteDevice d){

        String address = d.getBluetoothAddress();
	    address = address.substring(0,10) + ":" + address.substring(10,address.length());	
	    address = address.substring(0,8) + ":" + address.substring(8,address.length());	
	    address = address.substring(0,6) + ":" + address.substring(6,address.length());	
	    address = address.substring(0,4) + ":" + address.substring(4,address.length());	
	    address = address.substring(0,2) + ":" + address.substring(2,address.length());	
		String name = "";
		try{
			name = d.getFriendlyName(false);
		}catch (IOException e){
			try{
				name = d.getFriendlyName(false);
			}catch (IOException e2){
			}
		}

		JSONObject obj=new JSONObject();
            obj.put("clientVersion","1.0");
            obj.put("clientType","bluecat");
            obj.put("timestamp",new Date().getTime());
            obj.put("bluetoothAddress",address);
            obj.put("bluetoothName",name);

		return obj.toString();
    }
    
    
}
