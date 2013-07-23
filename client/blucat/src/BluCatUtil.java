import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;


public class BluCatUtil {

	
	public static void doctorDevice() throws BluetoothStateException{
		

		if(System.getProperty("os.name").contains("Linux")){

			System.err.println("Is libbluetooth3 and libbluetooth-dev installed?");
			
			System.err.println("run: sudo apt-get install libbluetooth3 libbluetooth-dev");
			
		}
		
		
		
		if (!LocalDevice.isPowerOn()){
			
			System.out.println("There is no Bluetooth Adaptor powered on");
			return;
		}
		
		
		//TODO look for more problems using lsusb and command line debug tools
		
		System.out.println("I don't see anything wrong");
		
	}
	
	
	
	public static String clean(String str){
		
		if (str != null)
			return str.replace("\"", "''")
					.replace("\n", " ");
		else
			return str;
		
	}
	
	
}
