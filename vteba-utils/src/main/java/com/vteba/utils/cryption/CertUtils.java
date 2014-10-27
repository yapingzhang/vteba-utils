package com.vteba.utils.cryption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(CertUtils.class);
	
	public boolean verify(String filePath) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e1) {
			LOGGER.error("没有找到CA证书，path=[{}]", filePath, e1.getMessage());
		}
		
		Certificate certificate = null;
		try {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			certificate = factory.generateCertificate(fileInputStream);
		} catch (CertificateException e) {
			LOGGER.error("获取X.509数字证书异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(fileInputStream);
		}
		
		PublicKey publicKey = certificate.getPublicKey();
		try {
			certificate.verify(publicKey);
		} catch (Exception e) {
			LOGGER.error("验证CA数字证书异常。", e.getMessage());
		}
		return true;
	}
	
	public boolean verify(InputStream certInputStream) {
		Certificate certificate = null;
		try {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			certificate = factory.generateCertificate(certInputStream);
		} catch (CertificateException e) {
			LOGGER.error("获取X.509数字证书异常。", e.getMessage());
			return false;
		}
		
		PublicKey publicKey = certificate.getPublicKey();
		try {
			certificate.verify(publicKey);
		} catch (Exception e) {
			LOGGER.error("验证CA数字证书异常。", e.getMessage());
			return false;
		}
		return true;
	}
	
	public static void sign() {
		Certificate certificate = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			certificate = keyStore.getCertificate("alias");
			try {
				certificate.getEncoded();// 证书的二进制形式
			} catch (CertificateEncodingException e) {
				
			}
			certificate.getPublicKey();//证书中包含的公钥
			certificate.getType();// 证书类型，jks等
			certificate.toString();// 证书的string形式，重载了Object的方法
		} catch (KeyStoreException e) {
			
		}
		
		
	}
}
