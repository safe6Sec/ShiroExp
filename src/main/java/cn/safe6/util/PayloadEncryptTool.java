package cn.safe6.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class PayloadEncryptTool {

    public static String AesGcmEncrypt(byte[] plainText,String key) throws Exception{
        byte[] k = new BASE64Decoder().decodeBuffer(key);
        AesCipherService aes = new AesCipherService();
        ByteSource initciphertext = aes.encrypt(plainText, k);
        return String.valueOf(initciphertext);
    }


    public static String AesCbcEncrypt(byte[] plainText,String key) throws Exception {
        byte[] k = new BASE64Decoder().decodeBuffer(key);
        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(k,"AES");
        cipher.init(1,keySpec,ivParameterSpec);
        byte[] payloadBytes = cipher.doFinal(plainText);
        byte[] output = new byte[ivBytes.length + payloadBytes.length];
        System.arraycopy(ivBytes, 0, output, 0, ivBytes.length);
        System.arraycopy(payloadBytes, 0, output, ivBytes.length, payloadBytes.length);
        String b64Payload = Base64.encodeToString(output);
        return b64Payload;
    }



}
