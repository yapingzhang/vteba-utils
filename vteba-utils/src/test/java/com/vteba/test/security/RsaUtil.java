package com.vteba.test.security;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.util.encoders.Hex;

/**
 * RSA工具类
 * 
 * @author david.turing
 * @copyright GuangZhou BEA Usergroup
 * @version 0.7
 * @modifyTime 22:30:34
 */
public class RsaUtil {

	int keylength = 1024;
	int certainty = 20;
	RSAKeyGenerationParameters keyparam;
	AsymmetricBlockCipher eng = null;
	RSAKeyPairGenerator pGen = null;
	AsymmetricCipherKeyPair pair = null;

	public RsaUtil() {

	}

	public String getName() {
		return "RSA";
	}

	/**
	 * 设置RSA的密钥长度
	 * 
	 * @param rsakeylength
	 */
	public void setKeyLength(int rsakeylength) {
		if (rsakeylength == 512 || rsakeylength == 768 || rsakeylength == 1024
				|| rsakeylength == 2048)
			keylength = rsakeylength;
	}

	/**
	 * 设置RSA Key Pair的素数产生经度，该数值越大，理论产生的RSA Key安全性越高
	 * 
	 * @param certaintyofprime
	 */
	public void setCertaintyOfPrime(int certaintyofprime) {
		certainty = certaintyofprime;
	}

	/**
	 * 生成RSA Keypair，如果你不是通过ImportPublicKey和ImportPrivateKey 来导入密钥对，则可通过此方法随机生成。
	 * 
	 * @return RSAKeyGenerationParameters
	 */
	public void initRSAKeyPair() {
		/**
		 * 注意， 第一个参数被写死了，它是publicExponent, 即e e通常选3，17，65537 X.509建议使用65537
		 * PEM建议使用3 PKCS#1建议使用3或65537 在本算法中，它使用了3，即0x3
		 */
		RSAKeyGenerationParameters rsaparam = new RSAKeyGenerationParameters(
				BigInteger.valueOf(0x3), new SecureRandom(), this.keylength,
				this.certainty);
		this.keyparam = rsaparam;
		// RSA Keypair的生成依赖于rsaparam
		RSAKeyPairGenerator pGen = new RSAKeyPairGenerator();
		pGen.init(keyparam);
		pair = pGen.generateKeyPair();
		pair.getPublic();
	}

	/**
	 * 设置RSA密钥对，此方法用于从外部文件导入RSA密钥对
	 * 
	 * @see ImportPublicKey(String)
	 * @see ImportPrivateKey(String)
	 * @param pubparam
	 *            公钥
	 * @param privparam
	 *            私钥
	 */
	public void setRSAKeyPair(RSAKeyParameters pubparam,
			RSAPrivateCrtKeyParameters privparam) {
		AsymmetricCipherKeyPair newpair = new AsymmetricCipherKeyPair(pubparam,
				privparam);
		pair = newpair;

	}

	/**
	 * 该函数返回公钥
	 * 
	 * @return
	 */
	public RSAKeyParameters getPublicKey() {
		return (RSAKeyParameters) pair.getPublic();
	}

	/**
	 * 该函数返回私钥 注意，RSAPrivateCrtKeyParameters其实继承了RSAKeyParameters
	 * 
	 * @see getPublicKey()
	 * @return
	 */
	public RSAPrivateCrtKeyParameters getPrivateKey() {
		return (RSAPrivateCrtKeyParameters) pair.getPrivate();
	}

	/**
	 * RSA的padding模式，安全性依次递增 mode=1 RAW RSA 安全性最差 mode=2 PKCS1 mode=3 OAEP
	 * 
	 * @param mode
	 */
	public void setRSAMode(int mode) {
		eng = new RSAEngine(); // 默认就是RAW模式, 安全性问题，已不再使用
		if (mode == 2)
			eng = new PKCS1Encoding(eng);
		else
			eng = new OAEPEncoding(eng); // mode==3
	}

	/**
	 * 该RSAEngine的每次处理输入数据，以Block为单位，是32Bytes 因此，本函数的输入要严格控制在32Byte，即256bit数据
	 * 超出该长度会抛出异常。 本函数要求输入必须为16进制字符0-F。
	 * 
	 * @see Decrypt(String input)
	 * @param input
	 * @return
	 */
	public String encrypt(String input) {

		byte[] inputdata = Hex.decode(input);

		// 用公钥加密
		eng.init(true, pair.getPublic());

		System.out.println(">>>加密参数");
		System.out.println(">>>明文字节数：" + inputdata.length);
		System.out.println(">>>RSA Engine Input Block Size="
				+ this.eng.getInputBlockSize());
		System.out.println(">>>RSA Engine Output Block Size="
				+ this.eng.getOutputBlockSize());

		try {
			inputdata = eng.processBlock(inputdata, 0, inputdata.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(Hex.encode(inputdata));
	}

	/**
	 * 该函数输入为字节，并规定其长度为32字节 超出该长度会抛出异常
	 * 
	 * @see Decrypt(byte[] inputdata)
	 * @param inputdata
	 * @return
	 */
	public byte[] encrypt(byte[] inputdata) {
		byte[] outputdata = null;

		// 用公钥加密
		eng.init(true, pair.getPublic());

		try {
			inputdata = eng.processBlock(inputdata, 0, inputdata.length);
			outputdata = new byte[eng.getOutputBlockSize()];
			outputdata = inputdata;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputdata;
	}

	/**
	 * 加密ByteArrayInputStream流，在该方法中，会对input做分段处理，每段都将会
	 * 以inBlockSize来划分明文，密文同样会设置换行，因此，将来在加密过程中，密文
	 * 需要分行读入，在明文、密文，唯一对应的是inBlock和outBlock，它们是换行对应的。
	 * 
	 * 本函数已经处理结尾Block问题
	 * 
	 * @param input
	 * @param Key
	 * @return
	 */
	public byte[] encryptPro(byte[] inputload) {
		ByteArrayInputStream inputstream = new ByteArrayInputStream(inputload);
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();

		// 用公钥加密
		eng.init(true, pair.getPublic());

		int inBlockSize = this.eng.getInputBlockSize();
		int outBlockSize = this.eng.getOutputBlockSize();

		try {
			System.out.println("加密的 InBlockSize=" + inBlockSize);
			System.out.println("加密的outBlockSize=" + outBlockSize);

			encryptPro(inputstream, outputstream);

		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] outputload = outputstream.toByteArray();

		System.out.println("###[1]明文大小：" + inputload.length);
		System.out.println("###[1]密文大小：" + outputload.length);

		return outputload;
	}

	/**
	 * 加密BufferedInputStream流，在该方法中，会对input做分段处理，每段都将会
	 * 以inBlockSize来划分明文，密文同样会设置换行，因此，将来在加密过程中，密文
	 * 需要分行读入，在明文、密文，唯一对应的是inBlock和outBlock，它们是换行对应的。
	 * 
	 * 此函数未处理padding，如果你不想自己调整padding，请用encryptPro
	 *
	 * @see encryptPro(BufferedInputStream inputstream,BufferedOutputStream
	 *      outputstream)
	 * 
	 * @param input
	 * @param Key
	 * @return
	 */
	public void encrypt(BufferedInputStream inputstream,
			BufferedOutputStream outputstream) {
		// 用公钥加密
		eng.init(true, pair.getPublic());

		int inBlockSize = this.eng.getInputBlockSize();
		int outBlockSize = this.eng.getOutputBlockSize();

		byte[] inblock = new byte[inBlockSize];
		byte[] outblock = new byte[outBlockSize];

		byte[] rv = null;
		try {
			while (inputstream.read(inblock, 0, inBlockSize) > 0) {
				outblock = eng.processBlock(inblock, 0, inBlockSize);
				rv = Hex.encode(outblock, 0, outBlockSize);
				outputstream.write(rv, 0, rv.length);
				outputstream.write('\n');

			}

		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 加密BufferedInputStream流，在该方法中，会对input做分段处理，每段都将会
	 * 以inBlockSize来划分明文，密文同样会设置换行，因此，将来在加密过程中，密文
	 * 需要分行读入，在明文、密文，唯一对应的是inBlock和outBlock，它们是换行对应的。
	 * 
	 * 该函数会在加密文件的头部注明padding_size padding_size即padding_size即明文流按照inBlockSize划分后
	 * 最后一个block的未占满的大小（字节数）
	 * 
	 * @param input
	 * @param Key
	 * @return
	 */
	public void encryptPro(InputStream inputstream, OutputStream outputstream) {
		// 用公钥加密
		eng.init(true, pair.getPublic());

		int inBlockSize = this.eng.getInputBlockSize();
		int outBlockSize = this.eng.getOutputBlockSize();

		byte[] inblock = new byte[inBlockSize];
		byte[] outblock = new byte[outBlockSize];

		byte[] rv = null;

		try {

			// System.out.println("stream length="+inputstream.available());
			int padding_size = inBlockSize
					- (inputstream.available() % inBlockSize);
			// System.out.println("padding_size="+padding_size);

			/*
			 * 写入padding_size，处理最后一个Block不够inBlockSize的情况
			 * 记住不要自己修改inBlockSize，因为RSA共有三种模式，每种模式
			 * 对Padding的处理方式都不一样，所以，不要修改对processBlock
			 * 的加密解密方式。总之，不要修改RSA的inBlock！
			 */
			outputstream.write((padding_size + "").getBytes());
			outputstream.write('\n');

			while (inputstream.read(inblock, 0, inBlockSize) > 0) {
				outblock = eng.processBlock(inblock, 0, inBlockSize);
				rv = Hex.encode(outblock, 0, outBlockSize);

				// System.out.println("Hex len="+rv.length);
				outputstream.write(rv, 0, rv.length);

				outputstream.write('\n');
			}

		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解密字符串
	 * 
	 * @param input
	 * @return
	 */
	public String decrypt(String input) {

		byte[] inputdata = Hex.decode(input);

		eng.init(false, pair.getPrivate());
		System.out.println(">>>加密参数");
		System.out.println(">>>RSA Engine Input Block Size="
				+ this.eng.getInputBlockSize());
		System.out.println(">>>RSA Engine Output Block Size="
				+ this.eng.getOutputBlockSize());

		try {
			inputdata = eng.processBlock(inputdata, 0, inputdata.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(Hex.encode(inputdata));

	}

	/**
	 * 解密字节数组
	 * 
	 * @param inputdata
	 * @return
	 */
	public byte[] decrypt(byte[] inputdata) {
		byte[] outputdata = null;

		// 用公钥加密
		eng.init(false, pair.getPrivate());

		try {
			inputdata = eng.processBlock(inputdata, 0, inputdata.length);
			outputdata = new byte[eng.getOutputBlockSize()];
			outputdata = inputdata;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputdata;
	}

	/**
	 * 解密流
	 * 
	 * 本函数已经处理结尾Block问题
	 * 
	 * @see encryptPro(byte[] inputload)
	 * 
	 * @param inputstream
	 *            被加密过的流
	 * @param outputstream
	 *            解密输出的流
	 */
	public byte[] decryptPro(byte[] inputload) {

		ByteArrayInputStream inputstream = new ByteArrayInputStream(inputload);
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();

		// 设置引擎
		eng.init(false, pair.getPrivate());

		int inBlockSize = this.eng.getInputBlockSize();
		int outBlockSize = this.eng.getOutputBlockSize();

		try {
			System.out.println("解密的In BlockSize=" + inBlockSize);
			System.out.println("解密的out BlockSize=" + outBlockSize);

			this.decryptPro(inputstream, outputstream);
		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("解密。。。outputload="+new String(outputload));

		// byte[] outputload=new byte[outputstream.size()];
		byte[] outputload = outputstream.toByteArray();

		System.out.println("###[2]密文大小：" + inputload.length);
		System.out.println("###[2]明文大小：" + outputload.length);

		return outputload;
	}

	/**
	 * 解密RSA流，首先先读取RSA的流的padding_size（第一行）
	 * 
	 * @param inputstream
	 *            被加密过的流
	 * @param outputstream
	 *            解密输出的流
	 */
	public void decryptPro(InputStream inputstream, OutputStream outputstream) {
		// 设置引擎
		eng.init(false, pair.getPrivate());

		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputstream));

		int inBlockSize = this.eng.getInputBlockSize();
		int outBlockSize = this.eng.getOutputBlockSize();

		int lines;

		byte[] outblock = new byte[outBlockSize];

		String rv = null;
		int inL = 0;
		byte[] last = null;

		try {
			int amout = inputstream.available();
			lines = amout / (inBlockSize * 2);
			// System.out.println("lines="+lines);
			rv = br.readLine();

			// System.out.println("#########padding size="+rv);
			int padding_size = Integer.parseInt(rv);

			while ((rv = br.readLine()) != null) {
				lines--;
				/*
				 * 要注意，Hex处理密文是将每个byte用2个16进制表示 一个Hex码其实只需4bit来表示，一个byte有8bit，因此
				 * 需要2个Hex码表示，所以，一个字符经Hex Encode会 变成两个Hex字符。
				 */
				inL = rv.length() / 2;
				last = new byte[inL];
				last = Hex.decode(rv);

				outblock = eng.processBlock(last, 0, inBlockSize);

				if (lines > 0) {
					outputstream.write(outblock, 0, outBlockSize);
				} else
					outputstream
							.write(outblock, 0, outBlockSize - padding_size);

			}

		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 输出公钥到文件
	 * 
	 * @param Filename
	 */
	public void ExportPublicKey(String Filename) {
		String outfile = Filename;
		BufferedOutputStream outstream = null;

		try {
			outstream = new BufferedOutputStream(new FileOutputStream(outfile));
		} catch (IOException fnf) {
			System.err.println("无法创建公钥文件 [" + outfile + "]");
			System.exit(1);
		}

		RSAKeyParameters mypubkey = this.getPublicKey();
		BigInteger mypubkey_modulus = mypubkey.getModulus();
		BigInteger mypubkey_exponent = mypubkey.getExponent();
		System.out.println("[ExportPublicKey]mypubkey_modulus="
				+ mypubkey_modulus.toString());
		System.out.println("[ExportPublicKey]mypubkey_exponent="
				+ mypubkey_exponent);

		try {
			outstream.write(mypubkey_modulus.toString().getBytes());
			outstream.write('\n');
			outstream.write(mypubkey_exponent.toString().getBytes());
			outstream.flush();
			outstream.close();
		} catch (IOException closing) {
			closing.printStackTrace();
		}
		System.out.println("公钥输出到文件:" + Filename);

	}

	/**
	 * 从文件中导入公钥到内存
	 * 
	 * @param Filename
	 * @return
	 */
	public RSAKeyParameters ImportPublicKey(String Filename) {
		String infile = Filename;
		BufferedInputStream instream = null;
		RSAKeyParameters RSAParam = null;
		try {
			instream = new BufferedInputStream(new FileInputStream(infile));
		} catch (FileNotFoundException fnf) {
			System.err.println("公钥文件没有找到 [" + infile + "]");
			System.exit(1);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(instream));
		BigInteger mypubkey_modulus = null;
		BigInteger mypubkey_exponent = null;
		String readstr = null;

		try {

			readstr = br.readLine();

			mypubkey_modulus = new BigInteger(readstr);
			System.out.println("[ImportPublicKey]mypubkey_modulus="
					+ mypubkey_modulus.toString());

			readstr = br.readLine();
			mypubkey_exponent = new BigInteger(readstr);
			System.out.println("[ImportPublicKey]mypubkey_exponent="
					+ mypubkey_exponent);

			RSAParam = new RSAKeyParameters(false, mypubkey_modulus,
					mypubkey_exponent);

		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RSAParam;

	}

	/**
	 * 输出私钥到指定的文件
	 * 
	 * @param Filename
	 */
	public void ExportPrivateKey(String Filename) {

		String outfile = Filename;
		BufferedOutputStream outstream = null;

		try {
			outstream = new BufferedOutputStream(new FileOutputStream(outfile));
		} catch (IOException fnf) {
			System.err.println("输出文件无法创建 [" + outfile + "]");
			System.exit(1);
		}

		RSAPrivateCrtKeyParameters myprivkey = this.getPrivateKey();

		BigInteger myprivkey_modulus = myprivkey.getModulus();
		BigInteger myprivkey_exponent = myprivkey.getExponent();
		BigInteger e = myprivkey.getPublicExponent(); // e is public
		BigInteger dP = myprivkey.getDP();
		BigInteger dQ = myprivkey.getDQ();
		BigInteger p = myprivkey.getP();
		BigInteger q = myprivkey.getQ();
		BigInteger qInv = myprivkey.getQInv();

		try {
			outstream.write(myprivkey_modulus.toString().getBytes());
			outstream.write('\n');
			outstream.write(e.toString().getBytes());
			outstream.write('\n');
			outstream.write(myprivkey_exponent.toString().getBytes());
			outstream.write('\n');
			outstream.write(p.toString().getBytes());
			outstream.write('\n');
			outstream.write(q.toString().getBytes());
			outstream.write('\n');
			outstream.write(dP.toString().getBytes());
			outstream.write('\n');
			outstream.write(dQ.toString().getBytes());
			outstream.write('\n');
			outstream.write(qInv.toString().getBytes());
			outstream.write('\n');

			outstream.flush();
			outstream.close();
		} catch (IOException closing) {
			closing.printStackTrace();
		}
		System.out.println("私钥输出到文件:" + Filename);

	}

	/**
	 * 输出私钥到指定的文件，私钥经过密码加密 强烈建议在生产环境中使用此方法而不要使用 ExportPrivateKey(String
	 * Filename)
	 * 
	 * @param Filename
	 *            私钥文件
	 * @param Password
	 *            私钥的保护密码
	 */
	public void ExportPrivateKeyWithPass(String Filename, String Password) {

		String outfile = Filename;

		ByteArrayOutputStream outstream = null;
		BufferedOutputStream keyoutstream = null;
		// 借助BlockCipherTool来加密私钥
		BlockCipherTool cipherTool = new BlockCipherTool();

		// 暂时使用DES加密私钥
		// cipherTool.setEngine(new AESEngine());
		// cipherTool.setEngine(new IDEAEngine());
		cipherTool.setEngine(new DESEngine());
		// cipherTool.setEngine(new BlowfishEngine());

		// cipherTool.setKeyLength(64); //AES　32(Hex)*4=128bit
		// String keyStr="123456789012345678901234567890FF"; //16进制 128bit AES
		// Key
		// String
		// keyStr="123456789012345678901234567890FF123456789012345678901234567890FF";
		// //16进制 256bit AES Key
		// String keyStr="123456789012345678901234567890FF"; //16进制 128bit IDEA
		// Key
		// String keyStr="0123456789abcdef"; //16进制 64bit(56) DES Key
		// String keyStr="0123456789ABCDEF"; //16进制 64bit(56) Blowfish Key

		outstream = new ByteArrayOutputStream();
		try {
			keyoutstream = new BufferedOutputStream(new FileOutputStream(
					outfile));

		} catch (Exception fnf) {
			System.err.println("输出文件无法创建 [" + outfile + "]");
			System.exit(1);
		}

		RSAPrivateCrtKeyParameters myprivkey = this.getPrivateKey();

		BigInteger myprivkey_modulus = myprivkey.getModulus();
		BigInteger myprivkey_exponent = myprivkey.getExponent();
		BigInteger e = myprivkey.getPublicExponent(); // e is public
		BigInteger dP = myprivkey.getDP();
		BigInteger dQ = myprivkey.getDQ();
		BigInteger p = myprivkey.getP();
		BigInteger q = myprivkey.getQ();
		BigInteger qInv = myprivkey.getQInv();

		try {
			// 产生正确的私钥流
			outstream.write(myprivkey_modulus.toString().getBytes());
			outstream.write('\n');
			outstream.write(e.toString().getBytes());
			outstream.write('\n');
			outstream.write(myprivkey_exponent.toString().getBytes());
			outstream.write('\n');
			outstream.write(p.toString().getBytes());
			outstream.write('\n');
			outstream.write(q.toString().getBytes());
			outstream.write('\n');
			outstream.write(dP.toString().getBytes());
			outstream.write('\n');
			outstream.write(dQ.toString().getBytes());
			outstream.write('\n');
			outstream.write(qInv.toString().getBytes());
			outstream.write('\n');

			byte[] privatekey_withtoutpass = outstream.toByteArray();

			ByteArrayInputStream keyinstream = new ByteArrayInputStream(
					privatekey_withtoutpass);

			// 加密私钥
			cipherTool.init(true, Password);
			// 将outstream转型成keyinstream，将keyinstream执行DES加密
			cipherTool.Encrypt(keyinstream, keyoutstream);

			keyinstream.close();
			keyoutstream.flush();
			keyoutstream.close();

		} catch (IOException closing) {
			closing.printStackTrace();
		}

		System.out.println("私钥经过加密并输出到文件:" + Filename);

	}

	/**
	 * 从某个文件中导入私钥，假定私钥未被加密
	 * 
	 * @see ExportPrivateKey(String Filename)
	 * @param Filename
	 * @return
	 */
	public RSAPrivateCrtKeyParameters ImportPrivateKey(String Filename) {
		String infile = Filename;
		BufferedInputStream instream = null;
		RSAPrivateCrtKeyParameters RSAPrivParam = null;

		try {
			instream = new BufferedInputStream(new FileInputStream(infile));
		} catch (FileNotFoundException fnf) {
			System.err.println("私钥文件没有找到 [" + infile + "]");
			System.exit(1);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(instream));

		BigInteger myprivkey_modulus = null;
		BigInteger myprivkey_exponent = null;
		BigInteger e = null;
		BigInteger p = null;
		BigInteger q = null;
		BigInteger dP = null;
		BigInteger dQ = null;
		BigInteger qInv = null;

		String readstr = null;
		try {

			readstr = br.readLine();
			myprivkey_modulus = new BigInteger(readstr);
			readstr = br.readLine();
			e = new BigInteger(readstr);
			readstr = br.readLine();
			myprivkey_exponent = new BigInteger(readstr);
			readstr = br.readLine();
			p = new BigInteger(readstr);
			readstr = br.readLine();
			q = new BigInteger(readstr);
			readstr = br.readLine();
			dP = new BigInteger(readstr);
			readstr = br.readLine();
			dQ = new BigInteger(readstr);
			readstr = br.readLine();
			qInv = new BigInteger(readstr);

			RSAPrivParam = new RSAPrivateCrtKeyParameters(myprivkey_modulus,
					myprivkey_exponent, e, p, q, dP, dQ, qInv);

		} catch (DataLengthException ex) {
			ex.printStackTrace();
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return RSAPrivParam;

	}

	/**
	 * 从私钥文件中导入私钥，并用保护密码通过DES解密该私钥 放入内存，该方法跟ExportPrivateKeyWithPass 对应
	 * 
	 * @see ExportPrivateKeyWithPass(String Filename, String Password)
	 * @param Filename
	 * @param Password
	 * @return
	 */
	public RSAPrivateCrtKeyParameters ImportPrivateKeyWithPass(String Filename,
			String Password) {
		String infile = Filename;
		InputStream instream = null;

		ByteArrayInputStream keyinstream = null;
		ByteArrayOutputStream keyoutstream = new ByteArrayOutputStream();

		// 借助BlockCipherTool来加密私钥
		BlockCipherTool cipherTool = new BlockCipherTool();

		// 暂时使用DES加密私钥
		// cipherTool.setEngine(new AESEngine());
		// cipherTool.setEngine(new IDEAEngine());
		cipherTool.setEngine(new DESEngine());
		// cipherTool.setEngine(new BlowfishEngine());

		// cipherTool.setKeyLength(64); //AES　32(Hex)*4=128bit
		// String keyStr="123456789012345678901234567890FF"; //16进制 128bit AES
		// Key
		// String
		// keyStr="123456789012345678901234567890FF123456789012345678901234567890FF";
		// //16进制 256bit AES Key
		// String keyStr="123456789012345678901234567890FF"; //16进制 128bit IDEA
		// Key
		// String keyStr="0123456789abcdef"; //16进制 64bit(56) DES Key
		// String keyStr="0123456789ABCDEF"; //16进制 64bit(56) Blowfish Key

		RSAPrivateCrtKeyParameters RSAPrivParam = null;
		try {
			instream = new BufferedInputStream(new FileInputStream(infile));
		} catch (FileNotFoundException fnf) {
			System.err.println("私钥文件没有找到 [" + infile + "]");
			System.exit(1);
		}

		cipherTool.init(false, Password);

		// keyinstream-->ByteArrayOutputStream，将keyinstream执行DES加密
		cipherTool.Decrypt(instream, keyoutstream);

		byte[] privatekey_withtoutpass = keyoutstream.toByteArray();

		keyinstream = new ByteArrayInputStream(privatekey_withtoutpass);

		BigInteger myprivkey_modulus = null;
		BigInteger myprivkey_exponent = null;
		BigInteger e = null;
		BigInteger p = null;
		BigInteger q = null;
		BigInteger dP = null;
		BigInteger dQ = null;
		BigInteger qInv = null;

		String readstr = null;
		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(
					keyinstream));

			readstr = br.readLine();
			myprivkey_modulus = new BigInteger(readstr);
			readstr = br.readLine();
			e = new BigInteger(readstr);
			readstr = br.readLine();
			myprivkey_exponent = new BigInteger(readstr);
			readstr = br.readLine();
			p = new BigInteger(readstr);
			readstr = br.readLine();
			q = new BigInteger(readstr);
			readstr = br.readLine();
			dP = new BigInteger(readstr);
			readstr = br.readLine();
			dQ = new BigInteger(readstr);
			readstr = br.readLine();
			qInv = new BigInteger(readstr);

			RSAPrivParam = new RSAPrivateCrtKeyParameters(myprivkey_modulus,
					myprivkey_exponent, e, p, q, dP, dQ, qInv);

			keyinstream.close();
			keyoutstream.flush();
			keyoutstream.close();

		} catch (DataLengthException ex) {
			ex.printStackTrace();
		} catch (IllegalStateException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return RSAPrivParam;

	}

	/**
	 * 为一个Block清0
	 * 
	 * @param block
	 */
	public void reset(byte[] block) {
		for (int i = 0; i < block.length; i++)
			block[i] = (byte) 0;
	}

	/**
	 * 将某个Block自off后的字节清0
	 * 
	 * @param block
	 * @param off
	 */
	public void padding(byte[] block, int off) {
		for (int i = off; i < block.length; i++)
			block[i] = (byte) 0;
	}

}