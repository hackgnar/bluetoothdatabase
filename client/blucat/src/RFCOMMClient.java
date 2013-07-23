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

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;


/**
 * 
 * @author Joseph Paul Cohen
 *
 */
public class RFCOMMClient {


	
	

	
	
	public static void startClient(String url, boolean verbose, boolean zip) throws IOException{
		
		if (verbose) System.err.println("#" + new Date() + " Waiting for connection");
	
		
		StreamConnection con = (StreamConnection) Connector.open(url);
		
		if (verbose) System.err.println("#" + new Date() + " Connected");
		
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
		
		

		os.flush();
		
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

					System.err.println("\nError: " + e.getMessage());
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

			System.err.println("\nError: " + e.getMessage());
			e.printStackTrace();
		}
		
		
		con.close();
        if (verbose) System.err.println("#" + new Date() + " - Connection Closed");
		
	}
}
