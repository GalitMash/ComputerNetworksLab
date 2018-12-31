
public class SocksRequest {
	private int versionNum; // VN
	private int cmdCode; // CD
	private int dstPort; // DSTPORT
	private String dstIp; // DSTIP
	
	// Constructor for extracting and setting the request parameters from the connect request
	public SocksRequest (byte[] request){
		versionNum = request[0];
		cmdCode = request[1];
		// Masking with 0xFFFF to extract the 2 least significant bytes.
		dstPort = (request[2] << 8 | request[3]) & 0xFFFF;
		dstIp = parseDstIP(request);
	}
	
	private String parseDstIP(byte[] request){
		String ip = "";
		for (int i = 4; i <= 7; i++) {
			ip += "." + (request[i] & 0xFF);
		}
		// Returning the destination IP without "." at the beginning
		return ip.substring(1);
	}
	
	private String validateRequest(){
		if (versionNum != 4){
			return "Unsupported SOCKS protocol version (got " + versionNum + ")";
		}
		if (cmdCode != 1){
			return "Unsupported command code";
		}
		// Port number 0 is reserved in TCP
		if (dstPort < 1){
			return "Invalid port number";
		}
		return "Valid";
	}
}
