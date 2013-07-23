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
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.intel.bluetooth.BlueCoveConfigProperties;
import com.intel.bluetooth.BlueCoveImpl;

/**
 * 
 * @author Joseph Paul Cohen
 *
 */
public class ScanServices {

	
	public static void scanDevice(String server) throws IOException{
		
		
		String baseurl = "btspp://" + server + ":";
		
		System.out.println("Scanning RFCOMM Channels 1-30");
		
		for (int i = 1; i <= 30; i++)
			testUrl(baseurl + i);
	
	}
	
	
	
	public static boolean testUrl(String url) throws IOException{
	
		
		try{
			BlueCoveImpl.setConfigProperty(BlueCoveConfigProperties.PROPERTY_CONNECT_TIMEOUT,"7000");
			StreamConnection con = (StreamConnection) Connector.open(url, 0, true);
			System.out.println(url + " -> Open Channel!!! " + con.getClass().getSimpleName());
		}catch(Exception e){
			System.out.println(url + " -\\> " + e.getMessage());
			
			return false;
		}
		
		return true;
		
	}
	
	
	
	
	
public static void startClient(String server, String uuid, String port) throws IOException{
		
		//uuid = String.format("%s", uuid);
		
		//0x1101
		
		System.out.println("Opening connection to " + server + " at " + uuid + " on " + port);
		
		//String url = "btspp://0123456789AB:3";

		//String url = "btspp://" + server + ":" + port;
		
		String url = "btspp://002608D6F03F:4;authenticate=false;encrypt=false;master=true";
		
		
		startClient(url);
		
	}
	
	
	public static void startClient(String url) throws IOException{
		
		System.out.println("Waiting for connection");
		StreamConnection con = (StreamConnection) Connector.open(url);

		
		
		//ServiceRecord rec = LocalDevice.getLocalDevice().getRecord(con);
        
        //System.out.println(rec.toString());
        //System.out.println(rec.getConnectionURL(0, false));

		
		
		
        
		//StreamConnection con = conNot.acceptAndOpen(); 
		System.out.println("Got connection");

		
		
		
		
		
        final OutputStream os = con.openDataOutputStream();
        final InputStream is = con.openDataInputStream();

		
		
		
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				//System.out.println("inr");
				try {
					byte[] buffer = new byte[1024]; // Adjust if you want
				    int bytesRead;
				    while ((bytesRead = is.read(buffer)) != -1){
				    	//System.out.println("in");
							System.out.write(buffer, 0, bytesRead);
							
				    }
				    
				    //System.out.println("ine");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
		

				//System.out.println("outr");
				try {
				    
				    byte[] buffer = new byte[1024]; // Adjust if you want
				    int bytesRead;
				    while ((bytesRead = System.in.read(buffer)) != -1){
				    	//System.out.println("in");
							os.write(buffer, 0, bytesRead);
							os.flush();
							
				    }
				    //System.out.println("oute");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}

		
		
		
		

        
        
        
        
        
        
        
        
        
//        String greeting = "JSR-82 RFCOMM client says hello";
//        os.write( greeting.getBytes() );
//
//        byte buffer[] = new byte[80];
//        int bytes_read = is.read( buffer );
//        String received = new String(buffer, 0, bytes_read);
//        System.out.println("received: " + received);
        
       // con.close();
	}
	
}
