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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.bluetooth.BluetoothStateException;

/**
 * 
 * @author Joseph Paul Cohen
 *
 */

public class Main {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args1) throws IOException, InterruptedException {
		
		//String[] newargs = {"services"};
		
		//args = newargs;
		try{
			
			List<String> arg = new LinkedList<String>(Arrays.asList(args1));
			
			boolean verbose = false;
			if (arg.remove("-v"))
				verbose = true;
			
			boolean keepalive = false;
			if (arg.remove("-k"))
				keepalive = true;
			
			boolean zip = false;
			if (arg.remove("-z"))
				zip = true;
			
			
			String exec = "";
			if (arg.contains("-e")){
				int index = arg.indexOf("-e");
				
				// concat all args together till the end of line
				while (index != arg.size()-1){
					exec += arg.get(index+1) + " ";
					arg.remove(index+1);
				}
				// remove the -e
				arg.remove(index);
			}
				
			
			if (arg.size() == 0)
				printUsage();
				
			if ("devices".equalsIgnoreCase(arg.get(0))){
				RemoteDeviceDiscovery.runDiscovery(verbose);
				System.exit(0);
			}
			
			if ("doctor".equalsIgnoreCase(arg.get(0))){
				BluCatUtil.doctorDevice();
				System.exit(0);
			}
			
			if ("services".equalsIgnoreCase(arg.get(0))){
				ListServices.listServices(verbose);
				System.exit(0);
			}
			
			if (arg.size() == 1 && "-l".equalsIgnoreCase(arg.get(0))){
				RFCOMMServer.startServerRFCOMM(exec,verbose,keepalive, zip);
				System.exit(0);
			}
			
			if (arg.size() == 2 && "scan".equalsIgnoreCase(arg.get(0))){
				ScanServices.scanDevice(arg.get(1));
				System.exit(0);
			}
			
			if (arg.size() == 2 && "-l".equalsIgnoreCase(arg.get(0))){
				RFCOMMServer.startServerChannel(exec,arg.get(1), verbose,keepalive, zip);
				System.exit(0);
			}
			
			if (arg.size() == 2 && "-uuid".equalsIgnoreCase(arg.get(0))){
				RFCOMMServer.startServerUuid(exec,arg.get(1), verbose,keepalive, zip);
				System.exit(0);
			}
			
			if (arg.size() == 2 && "-url".equalsIgnoreCase(arg.get(0))){
				RFCOMMClient.startClient(arg.get(1), verbose, zip);
				System.exit(0);
			}
			
			
			printUsage();
			
		}catch (BluetoothStateException e){
			
			String msg = e.getMessage();
			
			System.err.println("Error: " + msg);
			
			if (msg.contains("Bluetooth Device is not available"))
				System.err.println("!!Is there a device plugged in?");
			
			BluCatUtil.doctorDevice();
			
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	
	private static void printUsage(){
		
		System.out.println(
				"blucat - by Joseph Paul Cohen 2012 - josephpcohen.com\n" +
		
						
				"-_-_-_-_-_-_-_,------,\n" +
				"_-_-_-_-_-_-_-|   /\\_/\\\n" +
				"-_-_-_-_-_-_-~|__( ^ .^)\n" +
				"              \"\"  \"\"\n" +
				
				
				
				"Usage:\n" +
				"  blucat devices : Lists devices \n" +
				"  blucat services : Lists all RFCOMM services \n" +
				"  blucat services <device> : List RFCOMM services for one device\n" +
				"  blucat scan <device> : Scan all RFCOMM channels\n" +
				"  blucat -l : Listen for RFCOMM connection \n" +
				"  blucat -l <port> : Listen for RFCOMM connection on port\n" +
				"  blucat -uuid <uuid> : Listen for UUID and attempt RFCOMM\n" +
				"  blucat -l <port> -e <command>: Listen for RFCOMM connection, execute <command> when connection\n" +
				"  blucat <server args> -k : Keep the connection alive\n" +
				"  blucat -url <url> : Connect to RFCOMM URL \n" + 
				"  blucat doctor : Run this if it's not working \n");
		System.exit(0);
	}
}
