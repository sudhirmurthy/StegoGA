package stegoga;
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
 * Private key crytography implementation.
 * Uses AES Algorithm to encrypt and decrpyt the 
 * message with a chosen key. It implements the
 * IStegoCrypt interface to add implementation
 * to the Encrypt() and Decrypt() methods.
 */
public class StegoCrypt implements IStegoCrypt {
	private byte[] messageBytes;
	private byte[] cipherBytes;
	private String pass; //store hash in future.
	private Key myKey;
	
	public StegoCrypt(){
		this.messageBytes = null;
		this.cipherBytes = null;
		this.pass = null;
	}
	
	public boolean getPassword(String password){
		try{
			if(password!=null){
				this.pass = password;
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	public boolean getPlainText(String txtmessage){
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
	
	public void Encrypt(){
		try{
			System.out.println("in Encrypt..Starting process..");
			this.buildKey(this.pass);
			Cipher cipher  = Cipher.getInstance("AES");
			System.out.println("initialized cipher..");
			cipher.init(Cipher.ENCRYPT_MODE,this.myKey);
			this.cipherBytes = cipher.doFinal(this.messageBytes);
			System.out.println("encpytion finished..");
			System.out.println(new String(this.cipherBytes,"UTF8"));
		}catch(Exception e){
			//throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
			System.out.println("Exception whilst encoding..");
			e.printStackTrace();
		}
	}
	
	 private void buildKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		 	byte[] KeyData = this.pass.getBytes();
		    SecretKeySpec KS = new SecretKeySpec(KeyData, "AES");
		    this.myKey = KS;
	}

	public void Decrypt(){
		try{
			System.out.println("in Decrypt..Starting process..");
			Cipher cipher  = Cipher.getInstance("AES");
			System.out.println("initialized cipher..");
			cipher.init(Cipher.DECRYPT_MODE,this.myKey);
			this.messageBytes = cipher.doFinal(this.cipherBytes);
			System.out.println("decryption finished..");
			System.out.println(new String(this.messageBytes,"UTF8"));
		}catch(Exception e){
			//throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
			System.out.println("Exception in Decrypt function...");
		}
	}

}
