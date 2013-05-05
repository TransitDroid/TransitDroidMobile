package com.transitdroid.transitdroid_emulator;

/**
 * Some standard transfer byte protocols used in NFC
 * @author 
 * Modified by Austin Takam
 *
 */
public class ISO7816 {
	
		public static final int MAX_EXPECTED_LENGTH = 256;
		public static final int MAX_EXPECTED_LENGTH_LONG = 65536;

		public static final byte INS_ERASE_BINARY = (byte) 0x0E;
		public static final byte INS_VERIFY =  (byte) 0x20;
		public static final byte INS_MANAGE_CHANNEL =  (byte) 0x70;
		public static final byte INS_EXTERNAL_AUTHENTICATE =  (byte) 0x82;
		public static final byte INS_GET_CHALLENGE =  (byte) 0x84;
		public static final byte INS_INTERNAL_AUTHENTICATE =  (byte) 0x88;
		public static final byte INS_INTERNAL_AUTHENTICATE_ACS =  (byte) 0x86;
		public static final byte INS_SELECT_FILE =  (byte) 0xA4;
		//public static final byte INS_READ_BINARY =  (byte) 0xB0;
		public static final byte INS_READ_RECORDS =  (byte) 0xB2;
		public static final byte INS_GET_RESPONSE =  (byte) 0xC0;
		public static final byte INS_ENVELOPE =  (byte) 0xC2;
		public static final byte INS_GET_DATA =  (byte) 0xCA;
		public static final byte INS_WRITE_BINARY =  (byte) 0xD0;
		public static final byte INS_WRITE_RECORD =  (byte) 0xD2;
		public static final byte INS_UPDATE_BINARY =  (byte) 0xD6;
		public static final byte INS_PUT_DATA =  (byte) 0xDA;
		public static final byte INS_UPDATE_DATA =  (byte) 0xDC;
		public static final byte INS_APPEND_RECORD =  (byte) 0xE2;

		public static final int CLS_PTS = 0xFF; // Class for PTS
		

	    public static final int OFFSET_CLA = 0;
	    public static final int OFFSET_INS = 1;
	    public static final int OFFSET_P1 = 2;
	    public static final int OFFSET_P2 = 3;
	    public static final int OFFSET_LC = 4;
	    public static final int OFFSET_CDATA = 5;
	    public static final int OFFSET_EXT_CDATA = 7;
	    
	    public static final short SW_SUCCESS = (short) 0x9000;
	    
	    public static final short SW_WRONG_LENGTH = 0x6700;	    
	    public static final short SW_SECURITY_STATUS_NOT_SATISFIED = 0x6982;
	    public static final short SW_DATA_INVALID = 0x6984;
	    public static final short SW_CONDITIONS_NOT_SATISFIED = 0x6985;
	    public static final short SW_COMMAND_NOT_ALLOWED = 0x6986;

	    public static final short SW_WRONG_DATA = 0x6A80;
	    public static final short FILE_NOT_FOUND = 0x6A82;
	    public static final short SW_INCORRECT_P1P2 = 0x6A86;
	    
	    public static final short SW_WRONG_P1P2 = 0x6B00;
	    public static final short SW_INS_NOT_SUPPORTED = 0x6D00;
	    public static final short SW_CLA_NOT_SUPPORTED = 0x6E00;
	    public static final short SW_UNKNOWN = 0x6F00;


	    // Added for Convenience
	    public static final byte INS_COMPLETE = (byte) 0xD0;
	    
	    public static final byte INS_READ_PHONE_ID_BINARY = (byte)0xB0;
	    
		public static final byte INS_READ_KEY_1_BINARY = (byte)0xB1;
		
		public static final byte INS_READ_KEY_2_BINARY = (byte)0xB2;
		
		public static final byte INS_READ_KEY_3_BINARY = (byte)0xB3;
		
		public static final byte INS_READ_KEY_4_BINARY = (byte)0xB4;
		
		public static final byte INS_READ_PASS_CODE_BINARY = (byte)0xB5;
		
		public static final byte INS_UPDATE_PASSCODE_BINARY =  (byte) 0xD5;
		public static final byte INS_UPDATE_KEY1_BINARY =  (byte) 0xD6;
		public static final byte INS_UPDATE_KEY2_BINARY =  (byte) 0xD7;
		public static final byte INS_UPDATE_KEY3_BINARY =  (byte) 0xD8;
		public static final byte INS_UPDATE_KEY4_BINARY =  (byte) 0xD9;
}
