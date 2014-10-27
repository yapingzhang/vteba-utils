package com.vteba.test.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.util.Date;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public class CertSign {
	public static void main(String args[]) throws Exception {
		char[] storepass = "wshn.ut".toCharArray();
		char[] cakeypass = "wshn.ut".toCharArray();
		String alias = "mytest";
		String name = "mykeystore";

		// Cert of CA从密钥库读取CA的证书
		// 这里的name的值为“mykeystore”,alias的值为“mytest”
		FileInputStream in = new FileInputStream(name);
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(in, storepass);
		java.security.cert.Certificate cl = ks.getCertificate(alias);

		// 从密钥库读取CA的私钥
		// 执行KeyStore对象的getKey()方法，获取其参数对应的条目的私钥，保护私钥的口令也通过方法的参数传入
		PrivateKey caprk = (PrivateKey) ks.getKey(alias, cakeypass);
		in.close();

		// 从CA的证书中提取签发者的信息
		// 首先提取CA证书的编码，然后用该编码创建X509CerImpl类型的对象，通过该对象的get()方法获取X509CerInfo类型的对象，该对象封装的全部内容，最后通过该对象的get()方法获得X509Name类型的签发者信息
		byte[] encoal = cl.getEncoded();
		X509CertImpl cimpl = new X509CertImpl(encoal);
		X509CertInfo cinfol = (X509CertInfo) cimpl.get(X509CertImpl.NAME + "."
				+ X509CertImpl.INFO);
		X500Name issuer = (X500Name) cinfol.get(X509CertInfo.SUBJECT + "."
				+ CertificateIssuerName.DN_NAME);
		
		// 获取待签发的证书
		CertificateFactory of = CertificateFactory.getInstance("X.509");
		FileInputStream in2 = new FileInputStream(args[0]);
		java.security.cert.Certificate c2 = of.generateCertificate(in2);

		// 从待签发的数字证书提取证书的信息
		// 先提取待签发者的证书编码，然后创建X509CertImpl类型的对象，最后通过该对象的get()方法获取X509CertInfo类型的对象，以后就可以使用该对象创建新的证书了
		byte[] encod2 = c2.getEncoded();
		X509CertImpl cimp2 = new X509CertImpl(encod2);
		X509CertInfo cinfo2 = (X509CertInfo) cimp2.get(X509CertImpl.NAME + "."
				+ X509CertImpl.INFO);

		// 设置新证书有效期
		// 新证书的开始生效时间是从签发之时开始，因此首先使用new Date()获取但是时间。
		// 证书有效时间为15分钟
		// 同过两个日期创建CertificateValidity类型的对象，并且把它作为参数传递给上一步得到X509CertInfo对象的set()方法已设置有效期
		Date begindate = new Date();
		Date enddate = new Date(begindate.getTime() + 15 * 60 * 1000L);
		CertificateValidity cv = new CertificateValidity(begindate, enddate);
		cinfo2.set(X509CertInfo.VALIDITY, cv);

		// 设置新证书序列号。每个证书都有唯一的一个序列号。这里以当前时间（以秒为单位）为序列号，创建CertificateSerialNumber对象，并作为参数传递给X509CertInfo对象的set()方法以设置序列号
		int sn = (int) (begindate.getTime() / 1000);
		CertificateSerialNumber csn = new CertificateSerialNumber(sn);
		cinfo2.set(X509CertInfo.SERIAL_NUMBER, csn);

		// 设置新证书的签发者。执行X509CertInfo对象的set()方法设置签发者，传入参数即从CA的证书中提取的签发者信息
		cinfo2.set(X509CertInfo.ISSUER + "." + CertificateIssuerName.DN_NAME,
				issuer);

		// 设置新证书的签名算法信息
		// 首先生成AlgorithmId类型的对象，在其构造器中指定CA签名该证书所使用的算法为md5WithRSA，然后将其作为参数传递给X509CertInfo对象的set()方法以设置签名算法信息
		AlgorithmId alogorithm = new AlgorithmId(
				AlgorithmId.md2WithRSAEncryption_oid);
		cinfo2.set(CertificateAlgorithmId.NAME + "."
				+ CertificateAlgorithmId.ALGORITHM, "MD5WithRSA");

		// 创建证书并使用CA的私钥对其签名
		// X509CertImpl是X509证书的底层实现，将待签发的证书信息传递给其构造器，将得到新的证书，执行其sign()方法，将使用CA
		X509CertImpl newcert = new X509CertImpl(cinfo2);
		newcert.sign(caprk, "MDSWithRSA");

		// 将新的证书存入密钥库
		ks.setCertificateEntry("if_signed", newcert);
		/*
		 * snivateKey prk=(Frivatekey)ks.getkey("Lf","wishnout".toCharArray);
		 * java.security.cert.Certificate[] conai=(new cert);
		 * ks.setKeyEntry("if_signed,prk","newpass",oCharArray(),conain);
		 */
		FileOutputStream out = new FileOutputStream("newstore");
		ks.store(out, "newpass".toCharArray());
		out.close();
	};
}
