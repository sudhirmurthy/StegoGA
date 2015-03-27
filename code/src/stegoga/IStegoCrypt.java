package stegoga;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *IStegoCrypt Interface
 *Desc:  Interface to define the encrpytion and decryption method
 **/
public interface IStegoCrypt {
	public void Encrypt();
	public void Decrypt();
}
