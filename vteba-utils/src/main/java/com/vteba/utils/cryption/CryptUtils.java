package com.vteba.utils.cryption;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

import com.vteba.utils.serialize.SerializerUtils;

/**
 * 各种格式的编码解码码工具类。
 * 集成Commons-Codec，Commons-Lang及JDK提供的编解码方法。
 * @author yinlei
 * date 2012-9-20
 */
public final class CryptUtils {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * Hex编码，将字节数组编码为16进制的字符串
	 * @param input 要被编码的字节数组
	 */
	public static String hexEncode(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * Hex解码，将16进制编码的字符串解码为字节数组
	 * @param input 被hex编码过的字符串
	 */
	public static byte[] hexDecode(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码，将字节数组编码为base64的字符串
	 * @param input 字节数组
	 */
	public static String base64Encode(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548)
	 */
	public static String base64UrlSafeEncode(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64解码，将base64编码的字符创解码为字节数组
	 * @param input 要解码的base64字符串
	 */
	public static byte[] base64Decode(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * URL 编码, Encode默认为UTF-8
	 */
	public static String urlEncode(String input) {
		try {
			return URLEncoder.encode(input, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8
	 */
	public static String urlDecode(String input) {
		try {
			return URLDecoder.decode(input, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * Html 转码
	 */
	public static String htmlEscape(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码
	 */
	public static String htmlUnescape(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码
	 */
	public static String xmlEscape(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码
	 */
	public static String xmlUnescape(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}
	
	/**
	 * 将对象序列化后，进行HEX编码，然后进行SHA1摘要，最后再转换为HEX字符串
	 * @param o 要被转换的对象
	 * @return 对象转换后的字符串
	 * @author yinlei
	 * date 2013-4-6 下午5:15:12
	 */
	public static String toHexString(Object o) {
		return sha1Hex(hexEncode(SerializerUtils.serialize(o)));
	}
	
	/**
	 * 将data进行MD5摘要之后，再转化为HEX String
	 * @param data 要被转换的String
	 * @return 转换后的String
	 * @author yinlei
	 * date 2013-4-6 下午5:08:34
	 */
    public static String md5Hex(String data) {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        byte[] bytes = digest("MD5", data);
        return toHexString(bytes);
    }

    /**
     * 将data进行SHA1摘要之后，再转化为HEX String
     * @param data 要被转换的String
     * @return 转换后String
     * @author yinlei
     * date 2013-4-6 下午5:06:46
     */
    public static String sha1Hex(String data) {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        byte[] bytes = digest("SHA1", data);
        return toHexString(bytes);
    }

    /**
     * 将字节数组转换为Hex String
     * @param bytes 要转换的字节数组
     * @return 转换后的String
     * @author yinlei
     * date 2013-4-6 下午5:04:46
     */
    private static String toHexString(byte[] bytes) {
        int l = bytes.length;
        char[] out = new char[l << 1];

        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & bytes[i]) >>> 4];
            out[j++] = DIGITS[0x0F & bytes[i]];
        }
        return new String(out);
    }

    /**
     * 使用指定算法将string转换为byte[]
     * @param algorithm 算法名
     * @param data 要转换的string
     * @return
     * @author yinlei
     * date 2013-4-6 下午5:02:52
     */
    private static byte[] digest(String algorithm, String data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return digest.digest(data.getBytes());
    }
    
}
