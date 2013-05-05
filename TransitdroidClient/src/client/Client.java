package client;
/*
 * Client Code which communicates with the NFC external reader.
 * This class is the central point of communication between the reader and the phone
 * THis class refers to other external classes in order to communicate with the server
 */
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import confirm.ConfirmUI;


/**
 * Class Client
 * @author Austin Takam
 *
 */
public class Client {
	

	/*
	 * Commands or bytes used within the client which are compatible to those used on the phone
	 */
    private static final short SW_SUCCESS = (short) 0x9000;
    
    private static final short SW_OK = (short) 0x4F4B;
    
    private static final byte INS_UPDATE_PASSCODE_BINARY =  (byte) 0xD5;
    
    private static final byte INS_UPDATE_KEY1_BINARY =  (byte) 0xD6;
    
    private static final byte INS_UPDATE_KEY2_BINARY =  (byte) 0xD7;
    
    private static final byte INS_UPDATE_KEY3_BINARY =  (byte) 0xD8;
    
    private static final byte INS_UPDATE_KEY4_BINARY =  (byte) 0xD9;

	private static final int CLS_PTS = 0xFF; // Head for real physical card
	
	private static final int HD_PHONE = 0x80; // Head for phone

	private static final int INS_READ_PHONE_ID_BINARY = 0xB0;
	
	private static final int INS_READ_KEY_1_BINARY = 0xB1;
	
	private static final int INS_READ_KEY_2_BINARY = 0xB2;
	
	private static final int INS_READ_KEY_3_BINARY = 0xB3;
	
	private static final int INS_READ_KEY_4_BINARY = 0xB4;
	
	private static final int INS_READ_PASS_CODE_BINARY = 0xB5;
	
	private static final int INS_COMPLETE = 0xD0;
	
	private static final int MAX_PAGES = 10;
	
	private static byte[] responseBytes = new byte[]{};
	
	static int i=0;
	
	
	/**
	 * Main Method and display is done in a cosole
	 * @param args
	 */
	public static void main(String[] args) {
        try {
        	startTerminal();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	//Checking the validity of the response
	private static void checkSW(ResponseAPDU response) {
		if ((byte)response.getSW() == (byte)SW_OK){
			//Do nothing for now, response is good
		}
		else if (response.getSW() != (SW_SUCCESS & 0xffff)) {
	        System.err.printf("Received error status: %02X. Exiting.\n",response.getSW());
	        //LOG ERROR MESSAGE
	    }
	}
	
	//Starting the terminal
	public static void startTerminal() throws CardException{
		
		// Getting the default terminal, and selecting the cardTerminal
		TerminalFactory factory = TerminalFactory.getDefault();
	    CardTerminals terminals = factory.terminals();
	    
	    // In case the external NFC reader (terminal) is not connected or not recognized
	    if (terminals.list().isEmpty()) {
	        System.err.println("No smart card reders found. Connect reader and try again.");
	        //LOG ERROR MESSAGE HERE
	        System.exit(1);
	    }
	
	    // Console display if reader is found, then waiting for the reader to read some data
	    System.out.println("Place phone/card on reader to start");
	    Card card = waitForCard(terminals);
	    
	    // Trying to read data from the phone, and then writing to the phone
	    // Then disconnecting the card, 
	    try {
	        CardChannel channel = card.getBasicChannel();
			readPages(0,MAX_PAGES,channel);
	    } finally {
	        card.disconnect(false);
	        // LOG TERMINATION
	    }
	}
	
	//Reading the 'pages' of data from the phone
	private static void readPages(int startPage, int pagesToRead, CardChannel channel) throws CardException
	{
		CommandAPDU cmd0 = new CommandAPDU(HD_PHONE, INS_READ_PHONE_ID_BINARY, 0x00, 0, 5);
		ResponseAPDU response0 = transmit(channel,cmd0);
		byte[] phoneId = response0.getData();
				
		CommandAPDU cmd1 = new CommandAPDU(HD_PHONE, INS_READ_KEY_1_BINARY, 0x00, 0, 5);
		ResponseAPDU response1 = transmit(channel,cmd1);
		byte[] key1 = response1.getData();
		
		CommandAPDU cmd2 = new CommandAPDU(HD_PHONE, INS_READ_KEY_2_BINARY, 0x00, 0, 5);
		ResponseAPDU response2 = transmit(channel,cmd2);
		byte[] key2 = response1.getData();
		
		CommandAPDU cmd3 = new CommandAPDU(HD_PHONE, INS_READ_KEY_3_BINARY, 0x00, 0, 5);
		ResponseAPDU response3 = transmit(channel,cmd3);
		byte[] key3 = response1.getData();
		
		CommandAPDU cmd4 = new CommandAPDU(HD_PHONE, INS_READ_KEY_4_BINARY, 0x00, 0, 5);
		ResponseAPDU response4 = transmit(channel,cmd4);
		byte[] key4 = response1.getData();
	
		CommandAPDU cmd5 = new CommandAPDU(HD_PHONE, INS_READ_PASS_CODE_BINARY, 0x00, 0, 5);
		ResponseAPDU response5 = transmit(channel,cmd5);
		byte[] key5 = response1.getData();
		
		/* ******CALL TO WEB SERVICE HERE TO SEND PHONE ID, PASSCODE AND KEYS******* */
		 
		//Send the new pass code and keys to the phone
		byte[] newPassCode = ("NEW PASSCODE" + i++).getBytes();
		byte[] newKey1 = ("NEW KEY1" + i++).getBytes();
		byte[] newKey2 = ("NEW KEY2" + i++).getBytes();
		byte[] newKey3 = ("NEW KEY3" + i++).getBytes();
		byte[] newKey4 = ("NEW KEY4" + i++).getBytes();
		for (int i=0; i<newPassCode.length; i++){
			CommandAPDU cmd = new CommandAPDU(new byte[]{((byte) HD_PHONE), INS_UPDATE_PASSCODE_BINARY, 0x00, newPassCode[i], (byte) (newPassCode.length-i)});
			ResponseAPDU response = transmit(channel,cmd);
			checkSW(response);
		}
		for (int i=0; i<newKey1.length; i++){
			CommandAPDU cmd = new CommandAPDU(new byte[]{((byte) HD_PHONE), INS_UPDATE_KEY1_BINARY, 0x00, newKey1[i], (byte) (newKey1.length-i)});
			ResponseAPDU response = transmit(channel,cmd);
			checkSW(response);
		}
		for (int i=0; i<newKey2.length; i++){
			CommandAPDU cmd = new CommandAPDU(new byte[]{((byte) HD_PHONE), INS_UPDATE_KEY2_BINARY, 0x00, newKey2[i], (byte) (newKey2.length-i)});
			ResponseAPDU response = transmit(channel,cmd);
			checkSW(response);
		}
		for (int i=0; i<newKey3.length; i++){
			CommandAPDU cmd = new CommandAPDU(new byte[]{((byte) HD_PHONE), INS_UPDATE_KEY3_BINARY, 0x00, newKey3[i], (byte) (newKey3.length-i)});
			ResponseAPDU response = transmit(channel,cmd);
			checkSW(response);
		}
		for (int i=0; i<newKey4.length; i++){
			CommandAPDU cmd = new CommandAPDU(new byte[]{((byte) HD_PHONE), INS_UPDATE_KEY4_BINARY, 0x00, newKey4[i], (byte) (newKey4.length-i)});
			ResponseAPDU response = transmit(channel,cmd);
			checkSW(response);
		}
		// Send complete message to the phone and signaling success
		CommandAPDU cmd = new CommandAPDU(new byte[]{((byte) HD_PHONE), (byte) INS_COMPLETE, 0x00, 0x01, (byte) (newPassCode.length)});
		ResponseAPDU response = transmit(channel,cmd);
		//checkSW(response);
		  
		new ConfirmUI();
	}
	
	private static ResponseAPDU transmit(CardChannel channel, CommandAPDU cmd) throws CardException {
	    log(cmd);
	    ResponseAPDU response = channel.transmit(cmd);
	    log(response);
	    
	    return response;
	}
	
	private static void log(CommandAPDU cmd) {
	    System.out.printf("--> %s\n", toHex(cmd.getBytes()),
	            cmd.getBytes().length);
	    
	}
	
	private static void log(ResponseAPDU response) {
	    String swStr = String.format("%02X", response.getSW());
	    byte[] data = response.getData();
	    responseBytes = mergeByteArrays(responseBytes, data);
	    if (data.length > 0) {
	        System.out.printf("<-- %s %s (%d)\n", toHex(data), swStr,
	                data.length);
	        System.out.println("<-- to String: "+ new String(data).trim()+
	                data.length);
	    } else {
	        System.out.printf("<-- %s\n", swStr);
	    }
	}
	
	public static byte[] mergeByteArrays(byte[] a, byte[] b){
		byte[] c = new byte[a.length + b.length];
		for (int i=0; i<a.length; i++){
			c[i] = a[i];
		}
		int j = a.length;
		for (int i=0; i<b.length; i++){
			c[j] = b[i];
			j++;
		}
		return c;
	}
	
	private static Card waitForCard(CardTerminals terminals)
	        throws CardException {
			while (true) {
	        for (CardTerminal ct : terminals.list(CardTerminals.State.CARD_INSERTION)) {
	            return ct.connect("*");
	        }
	        terminals.waitForChange();
	    }
	}
	
	public static String toHex(byte[] bytes) {
	    StringBuilder buff = new StringBuilder();
	    for (byte b : bytes) {
	        buff.append(String.format("%02X", b));
	    }
	
	    return buff.toString();
	}

}
