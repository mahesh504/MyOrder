package in.arula.myorder.networkutil;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;


public class AES128EncryptImpl {
private static final String ALGO = "AES/CBC/PKCS5Padding";
    private byte[] keyValue =  null;
public AES128EncryptImpl() {
}

public byte[] getKeyValue() {
return keyValue;
}

public void setKeyValue(byte[] keyValue) {
this.keyValue = keyValue;
}

public static String getAlgo() {
return ALGO;
}

public String encrypt(String message,byte[] passphrase) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
	Key key = generateKey(passphrase);
        Cipher c = Cipher.getInstance(ALGO);
     // build the initialization vector.  This example is all zeros, but it
        // could be any value or generated using a random number generator.
        byte[] iv = { 0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c,
           0x0d, 0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c, 0x0d };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        c.init(Cipher.ENCRYPT_MODE, key,ivspec);
        byte[] encVal = c.doFinal(message.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
}

public String decrypt(String messsageBase64,byte[] passphrase) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException , InvalidAlgorithmParameterException{
Key key = generateKey(passphrase);
        Cipher c = Cipher.getInstance(ALGO);
        byte[] iv = { 0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c,
           0x0d, 0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c, 0x0d };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        c.init(Cipher.DECRYPT_MODE, key,ivspec);
        byte[] decordedValue=null;
        decordedValue = Base64.decode(messsageBase64, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
}
private static Key generateKey(byte[] passphrase){
System.out.println("key is converted :"+passphrase);
        Key key = new SecretKeySpec(passphrase, "AES");
        return key;
}
public byte[] generateKeyBytes() throws NoSuchAlgorithmException
{
// Get the KeyGenerator
KeyGenerator kgen = KeyGenerator.getInstance("AES");
kgen.init(128);
// Generate the secret key specs.
SecretKey skey = kgen.generateKey();
byte[] raw = skey.getEncoded();
return raw;
}
}