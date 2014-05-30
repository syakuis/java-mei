package com.syaku.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

/**
 * 문자열 및 파일에 대한 암호화/복호화
 */
public class Crypto {

    /** 파일 처리용 버퍼 크기 */
    private static final int BUFFER_SIZE = 8192;

    /** 디폴트 비밀키 파일 */
    private static final String DEFAULT_KEY_FILE = "default.key";

    /** 비밀키 */
    private static Key key = null;

    /**
     * 비밀키 파일 생성 메소드
     *
     * @return 비밀키 파일
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static File makeKey() throws IOException, NoSuchAlgorithmException {
        return makeKey(DEFAULT_KEY_FILE);
    }

    public static File makeKey(String filename) throws IOException,
                    NoSuchAlgorithmException {
        File file = new File(filename);
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom());
        Key key = generator.generateKey();
        ObjectOutputStream out =
            new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(key);
        out.close();
        return file;
    }

    /**
     * 지정된 비밀키를 가지고 오는 메서드
     *
     * @return 비밀키
     * @throws Exception
     */
    private static Key getKey() throws Exception {
        if (key != null) {
            return key;
        } else {
            return getKey(DEFAULT_KEY_FILE);
        }
    }

    private static Key getKey(String filename) throws Exception {
        if (key == null) {
            File file = new File(filename);
            if (!file.exists()) {
                file = makeKey();
            }
            if (file.exists()) {
                ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(filename));
                key = (Key) in.readObject();
                in.close();
            } else {
                throw new Exception("암호키 객체를 생성할 수 없습니다.");
            }
        }
        return key;
    }

    /**
     * 문자열 대칭 암호화
     *
     * @param strVal 암호화할 문자열
     * @return 암호화된 문자열
     * @throws Exception
     */
    public static String encrypt(String strVal) throws Exception {
        if (strVal == null || strVal.length() == 0)
            return "";
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        String amalgam = strVal;

        byte[] inputBytes1 = amalgam.getBytes("UTF-8");
        byte[] outputBytes1 = cipher.doFinal(inputBytes1);
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String outputStr1 = encoder.encode(outputBytes1);
        return outputStr1;
    }

    /**
     * 문자열 대칭 복호화
     *
     * @param strVal 복호화할 문자열
     * @return 복호화된 문자열
     * @throws Exception
     */
    public static String decrypt(String strVal) throws Exception {
        if (strVal == null || strVal.length() == 0)
            return "";
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getKey());
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

        byte[] inputBytes1 = decoder.decodeBuffer(strVal);
        byte[] outputBytes2 = cipher.doFinal(inputBytes1);

        String strResult = new String(outputBytes2, "UTF-8");
        return strResult;
    }

    /**
     * 파일 대칭 암호화
     *
     * @param infile 암호화할 파일명
     * @param outfile 암호화된 파일명
     * @throws Exception
     */
    public static void encryptFile(String infile, String outfile)
                    throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getKey());

        FileInputStream in = new FileInputStream(infile);
        FileOutputStream fileOut = new FileOutputStream(outfile);

        CipherOutputStream out = new CipherOutputStream(fileOut, cipher);
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while ((length = in.read(buffer)) != -1)
            out.write(buffer, 0, length);
        in.close();
        out.close();
    }

    /**
     * 파일 대칭 복호화
     *
     * @param infile 복호화할 파일명
     * @param outfile 복호화된 파일명
     * @throws Exception
     */
    public static void decryptFile(String infile, String outfile)
                    throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getKey());

        FileInputStream in = new FileInputStream(infile);
        FileOutputStream fileOut = new FileOutputStream(outfile);

        CipherOutputStream out = new CipherOutputStream(fileOut, cipher);
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while ((length = in.read(buffer)) != -1)
            out.write(buffer, 0, length);
        in.close();
        out.close();
    }
}