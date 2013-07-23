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
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.intel.bluetooth.BluetoothConsts;

/**
 * 
 * @author Joseph Paul Cohen
 *
 */
public class RFCOMMServer {

	
	public static void startServerRFCOMM(String exec, boolean verbose, boolean keepalive, boolean zip) throws IOException{
		
		String url = "btspp://localhost:" + BluetoothConsts.RFCOMM_PROTOCOL_UUID + 
				";name=BlueCatPipe;authenticate=false;encrypt=false;master=true";
		
		if (verbose) System.err.println("#" + "Creating RFCOMM server");
		

		
        StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open(url);

        
        ServiceRecord rec = LocalDevice.getLocalDevice().getRecord(service);
        
        //System.out.println(rec.toString());
        String remoteUrl = rec.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        remoteUrl = remoteUrl.substring(0, remoteUrl.indexOf(";"));

        if (verbose) System.err.println("#" + new Date() + " - Listening at " + remoteUrl);
        
        handleStreamConnection(service, exec, verbose, keepalive, zip);
		
	}
	
	
	public static void startServerUuid(String exec, String uuidValue, boolean verbose, boolean keepalive, boolean zip) throws IOException{
		
		uuidValue = uuidValue.replace("-", "");
		
		UUID uuid = new UUID(uuidValue, false);
		
		String url = "btspp://localhost:" + uuid.toString() + 
				";name=BlueCatPipe"; //;authenticate=false;authorize=false;encrypt=false;master=false";
		
		if (verbose) System.err.println("Creating server with UUID " + uuid);
		

		
        StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open(url);

        
        ServiceRecord rec = LocalDevice.getLocalDevice().getRecord(service);
        
        //System.out.println(rec.toString());
        String remoteUrl = rec.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        remoteUrl = remoteUrl.substring(0, remoteUrl.indexOf(";"));

        if (verbose) System.err.println("#" + new Date() + " - Listening at " + remoteUrl);
        
        handleStreamConnection(service, exec, verbose, keepalive, zip);

	}
	
	
	public static void startServerChannel(String exec, String port, boolean verbose, boolean keepalive, boolean zip) throws IOException{
		
		String url = "btspp://localhost:" + port + 
				";name=BlueCatPipe";
		
		if (verbose) System.err.println("#" + "Creating server on channel " + port);
		

		
        StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open(url);

        
        ServiceRecord rec = LocalDevice.getLocalDevice().getRecord(service);
        
        //System.out.println(rec.toString());
        String remoteUrl = rec.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        remoteUrl = remoteUrl.substring(0, remoteUrl.indexOf(";"));

        if (verbose) System.err.println("#" + new Date() + " - Listening at " + remoteUrl);
        
        handleStreamConnection(service, exec, verbose, keepalive, zip);

	}
	
	/**
	 * Handle the keepalive and pick which method we want to handle the stream
	 * 
	 * @param service
	 * @param exec
	 * @param verbose
	 * @param keepalive
	 * @throws IOException
	 */
	private static void handleStreamConnection(StreamConnectionNotifier service, String exec, boolean verbose, boolean keepalive, boolean zip) throws IOException{
		

		do{
		
			StreamConnection con =  (StreamConnection) service.acceptAndOpen();
	        
	        if (verbose) System.err.println("#" + new Date() + " - Connection Received!");
			
			if (!"".equals(exec)){
				handleStreamConnectionProcess(con, exec, verbose, zip);
			}else{
				
				handleStreamConnectionConsole(con, verbose, zip);
			}
		}while (keepalive);
	}
	
	/**
	 * Handle the stream when we want to use std in and out as the input source
	 * 
	 * @param con
	 * @param verbose
	 * @throws IOException
	 */
	private static void handleStreamConnectionConsole(StreamConnection con, boolean verbose, boolean zip) throws IOException{
		
		if (verbose) System.err.println("#" + new Date() + " - Standard Console Connection");

		if (verbose && zip) System.err.println("#" + "Zip Mode");
		
		final OutputStream os;
		final InputStream is;

		if (zip){
			
			os = new GZIPOutputStream(con.openDataOutputStream());
			is = new GZIPInputStream(con.openDataInputStream());
		}else{
			os = con.openDataOutputStream();
			is = con.openDataInputStream();
		}
	
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						byte[] buffer = new byte[1024]; // Adjust if you want
						int bytesRead;
						while ((bytesRead = is.read(buffer)) != -1){
	
							System.out.write(buffer, 0, bytesRead);
							System.out.flush();
						}
	
					} catch (IOException e) {
	
						System.err.println("#" + "Error: " + e.getMessage());
						e.printStackTrace();
					}
	
				}
			}).start();
	
	
			try {
				byte[] buffer = new byte[1024]; // Adjust if you want
				int bytesRead;
				while ((bytesRead = System.in.read(buffer)) != -1){
	
					os.write(buffer, 0, bytesRead);
					os.flush();	
				}
			} catch (IOException e) {
	
				System.err.println("#" + "Error: " + e.getMessage());
				e.printStackTrace();
			}
	        
	        
	        con.close();
	        if (verbose) System.err.println("#" + new Date() + " - Connection Closed");
	        
			
		}
	
	
	
	/**
	 * Handle the stream when we want to pipe std in and out to a process
	 * 
	 * @param con
	 * @param exec
	 * @param verbose
	 * @throws IOException
	 */
	
	private static void handleStreamConnectionProcess(StreamConnection con, String exec, boolean verbose, boolean zip) throws IOException{
		
		if (verbose) System.err.println("#" + new Date() + " - Process Redirect Connection: " + exec);
		
		if (verbose && zip) System.err.println("#" + "Zip Mode");
		
		final Process p = Runtime.getRuntime().exec(exec);

		
		final OutputStream os;
		final InputStream is;
		final InputStream err;

		final OutputStream sos;
		final InputStream sis;
		
		
		if (zip){
			
			os = new GZIPOutputStream(p.getOutputStream());
			is = new GZIPInputStream(p.getInputStream());
			err = new GZIPInputStream(p.getErrorStream());

			sos = new GZIPOutputStream(con.openDataOutputStream());
			sis = new GZIPInputStream(con.openDataInputStream());			
			
		}else{
			
			os = p.getOutputStream();
			is = p.getInputStream();
			err = p.getErrorStream();
	
			sos = con.openDataOutputStream();
			sis = con.openDataInputStream();
		
		}
		
		/*
		 * output from process
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					byte[] buffer = new byte[1024]; // Adjust if you want
					int bytesRead;
					while ((bytesRead = is.read(buffer)) != -1){
						sos.write(buffer, 0, bytesRead);
						sos.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();

		/*
		 * Error from process
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					byte[] buffer = new byte[1024]; // Adjust if you want
					int bytesRead;
					while ((bytesRead = err.read(buffer)) != -1){
						sos.write(buffer, 0, bytesRead);
						sos.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
		
		
		/*
		 * Input from other device
		 */
		try {

			byte[] buffer = new byte[1024]; // Adjust if you want
			int bytesRead;
			while ((bytesRead = sis.read(buffer)) != -1){
				
				os.write(buffer, 0, bytesRead);
				os.flush();
			}
			//kill process
			p.destroy();
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	

}
