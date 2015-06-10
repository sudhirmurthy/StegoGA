package stegoga;

import java.awt.TextArea;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.*;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * Private key cryptography implementation.
 * Uses AES Algorithm to encrypt and decrypt the 
 * message with a chosen key. It implements the
 * IStegoCrypt interface to add implementation
 * to the Encrypt() and Decrypt() methods.
 */
public class StegoCrypt implements IStegoCrypt {
		
	// the plain text byte array
	private byte[] messageBytes;
	
	// the cipher text byte array
	private byte[] cipherBytes;
	
	// the plain text password
	private String pass; 
	
	// the cipher password
	private Key myKey;
		
	//our logger
	private TextArea logger;
	
	/*
	 * Default Constructor for StegoCrypt 
	 */
	public StegoCrypt(){
		this.messageBytes = null;
		this.cipherBytes = null;
		this.pass = null;
		this.logger = null;
	}
	
	public StegoCrypt(TextArea logger){
		this.messageBytes = null;
		this.cipherBytes = null;
		this.pass = null;
		this.logger = logger;
	}
		
	/*
	 * Sets the plain text password used for 
	 * encoding and decoding..Note: The default
	 * key-length for AES is 16 bytes.
	 */
	public void setPassword(String password){
		if(password!=null){
			this.pass = password;
		}		
	}
	
		
	/*
	 * Set's the plain text message for encoding.
	 */
	public boolean setPlainText(String txtmessage){
		try{
			if (txtmessage!=null){
				this.messageBytes = txtmessage.getBytes("UTF8");
			}
			else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}	
		
	
	public boolean setImageBytes(byte[] message){
		this.messageBytes = message;
		return true;
	}
	
	
	/*
	 * Returns the cipher bytes of the plain text message. 
	 */
	public byte[] getCipherBytes(){
		return this.cipherBytes;
	}
	
	
	/*
	 * Sets the cipher bytes for decoding..
	 */
	public void setCipherBytes(byte[] cipherBytes){
		this.cipherBytes = new byte[cipherBytes.length];
		for(int i=0;i<cipherBytes.length;i++){
			this.cipherBytes[i] = cipherBytes[i];
		}
	}
	
	/*
	 * Returns the byte array of the plain text message.
	 */
	public byte[] getMessageBytes(){
		return this.messageBytes;
	}
	
	
	/*
	 * Returns an -127 to + 128 integer encoded array of the cipher bytes.
	 * This is because anything and everything in Java is signed integers.
	 */
	public int[] getSignedIntegerEncodedBytes(byte[] byteArray){
		int[] iBytes = new int[byteArray.length];
		try{
			for(int i=0;i<byteArray.length;i++){
				iBytes[i] = byteArray[i];
			}
		}catch(Exception e){
			return null;
		}
		return iBytes;
	} 
	
	
	/*
	 * Convert a signed java (byte) integer to an unsigned integer(0-255).
	 *  eg -128 is represented as 128
	 *    - 41 is represented as 215  
	 *      -1 is represented as 255
	 */
	public int[] getUnsignedIntegerBytes(int[] solutionArray){
		for(int i=0;i<solutionArray.length;i++){
			solutionArray[i] = (solutionArray[i] & 0xff);
		}
		return solutionArray;
	}
	
	
	/*
	 * Convert a array of unsigned integer byte value to an array of bytes. 
	 */
	public byte[] getSignedBytesFromInt(int[] unsignedInts){
		byte[] iBytes = new byte[unsignedInts.length];
		for(int i=0;i<unsignedInts.length;i++){
			iBytes[i] =(byte) unsignedInts[i];
		}
		return iBytes;
	}

	/*
	 * Build secret key form the given plain text password 
	 * using 'AES'
	 */
	private void buildKey(String password) throws NoSuchAlgorithmException,
												  UnsupportedEncodingException {
		try{
			byte[] KeyData = this.pass.getBytes();
			SecretKeySpec KS = new SecretKeySpec(KeyData, "AES");
			this.myKey = KS;
		}catch(Exception e){
			System.out.println("\nException whilst generating cipher key..");
		}
	}
    
	
	/*
	 * The Encrypt function generates the cipher bytes by building a pass key (cipher key) from
	 * the given plain text password and using it to encode the message bytes (plain text message)
	 * uses AES algorithm
	 */
	public void Encrypt(){
		try{
				
				System.out.println("\nin Encrypt..Starting process..");
				this.buildKey(this.pass);
				Cipher cipher  = Cipher.getInstance("AES");
				System.out.println("\ninitialized cipher..");
				cipher.init(Cipher.ENCRYPT_MODE,this.myKey);
				this.cipherBytes = cipher.doFinal(this.messageBytes);
				System.out.println("\nencpytion finished..");
				System.out.println(new String(this.cipherBytes,"UTF8"));
			}catch(Exception e){
				//throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
				System.out.println("\nException whilst encoding..");
				e.printStackTrace();
		}
	}
	
	
	/*
	 * The Decrypt function generates the message bytes by using the cipher key    
	 * on the cipher text. uses AES algorithm.
	 */
	public void Decrypt(){
		try{
				if(this.logger==null){
					System.out.println("\nin Decrypt..Starting process..");
				}
				else{
					this.logger.append("\nin Decrypt..Starting process..");
				}
				this.buildKey(this.pass);
				Cipher cipher  = Cipher.getInstance("AES");
				if(this.logger==null){
					System.out.println("\ninitialized cipher..");
				}
				else{
					this.logger.append("\ninitialized cipher..");
				}
				cipher.init(Cipher.DECRYPT_MODE,this.myKey);
				this.messageBytes = cipher.doFinal(this.cipherBytes);
				
				if(this.logger==null){
					System.out.println("\ndecryption finished..");
					System.out.println(new String(this.messageBytes,"UTF8"));
				}
				else{
					this.logger.append("\ndecryption finished..");
					this.logger.append("\nDecoded text is..");
					this.logger.append("\n***************************************************");
					this.logger.append("\n");
					this.logger.append(new String(this.messageBytes,"UTF8"));
					this.logger.append("\n***************************************************");
				}
		}catch(Exception e){
			//throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
			System.out.println("\nException in Decrypt function..." + e.getMessage());
		}		
	}
	
}
