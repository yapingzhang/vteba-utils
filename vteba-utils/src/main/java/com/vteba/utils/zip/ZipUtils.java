package com.vteba.utils.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vteba.utils.charstr.ByteUtils;
import com.vteba.utils.charstr.Char;

/**
 * 常用的压缩工具。包括GZIP ZIP ZLIB。默认编码是UTF-8
 * @author YINLEI 
 * @since 2014-1-1
 */
public class ZipUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

	/**
	 * 使用gzip进行压缩
	 * 
	 * @param data
	 *            待压缩的字符串
	 * @return 压缩后的字符串
	 */
	public static String gzips(String data) {
		byte[] result = gzip(data);
		return new String(result, Char.UTF8);
	}

	/**
	 * 使用gzip进行压缩
	 * 
	 * @param data
	 *            待压缩的字符串
	 * @return 压缩后的字节数组
	 */
	public static byte[] gzip(String data) {
		byte[] dataBytes = data.getBytes(Char.UTF8);
		return gzip(dataBytes);
	}

	/**
	 * 使用gzip进行压缩
	 * 
	 * @param data
	 *            待压缩的数据
	 * @return 压缩后的字节数组
	 */
	public static byte[] gzip(byte[] data) {
		if (data == null || data.length == 0) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(data);
		} catch (IOException e) {
			LOGGER.error("gzip压缩字符IO错误。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(gzip);
			IOUtils.closeQuietly(out);
		}
		return out.toByteArray();
	}
	
	/**
	 * 使用gzip进行解压缩
	 * 
	 * @param data
	 *            待解压的字符
	 * @return 解压后的字符
	 */
	public static String ungzip(String data) {
		if (data == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] compBytes = null;
		String result = null;
		try {
			compBytes = data.getBytes(Char.UTF8);
			in = new ByteArrayInputStream(compBytes);
			ginzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			result = out.toString("UTF-8");
		} catch (IOException e) {
			LOGGER.error("gzip解压字符IO错误。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(ginzip);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return result;
	}

	/**
	 * 使用zip进行压缩
	 * 
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String str) {
		if (str == null) {
			return null;
		}
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new String(compressed, Char.UTF8);
		} catch (IOException e) {
			compressed = null;
			LOGGER.error("zip压缩字符IO错误。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(zout);
			IOUtils.closeQuietly(out);
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压
	 * 
	 * @param data
	 *            待解压的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String data) {
		if (data == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = data.getBytes(Char.UTF8);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
			LOGGER.error("zip解压字符IO错误。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(zin);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return decompressed;
	}

	/**
	 * zlib压缩数据
	 * 
	 * @param data
	 *            待压缩数据
	 * @return byte[] 压缩后的数据
	 */
	public static byte[] zlib(byte[] data) {
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			LOGGER.error("zlib压缩数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(bos);
		}
		compresser.end();
		return output;
	}

	/**
	 * zlib压缩数据
	 * 
	 * @param data
	 *            待压缩数据
	 * @return byte[] 压缩后的数据
	 */
	public static String zlibs(byte[] data) {
		String output = null;
		Deflater compresser = new Deflater();
		byte[] dataByte = data;
		compresser.reset();
		compresser.setInput(dataByte);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(dataByte.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toString("ISO-8859-1");
		} catch (Exception e) {
			LOGGER.error("zlib压缩数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(bos);
		}
		compresser.end();
		return output;
	}
	
	/**
	 * zlib压缩数据
	 * 
	 * @param data
	 *            待压缩数据
	 * @return byte[] 压缩后的数据
	 */
	public static byte[] zlib(String data) {
		byte[] dataByte = data.getBytes(Char.UTF8);
		return zlib(dataByte);
	}
	
	/**
	 * zlib压缩数据
	 * 
	 * @param data
	 *            待压缩数据
	 * @return String 压缩后的数据
	 */
	public static String zlibs(String data) {
		String output = null;
		Deflater compresser = new Deflater();
		byte[] dataByte = data.getBytes(Char.UTF8);
		compresser.reset();
		compresser.setInput(dataByte);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(dataByte.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toString("ISO-8859-1");
		} catch (Exception e) {
			LOGGER.error("zlib压缩数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(bos);
		}
		compresser.end();
		return output;
	}
	
	/**
	 * zlib压缩数据
	 * 
	 * @param data
	 *            待压缩数据
	 * 
	 * @param os
	 *            输出流，压缩结果
	 */
	public static ByteArrayOutputStream zlibStream(byte[] data) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(os);

		try {
			dos.write(data, 0, data.length);
			dos.finish();
			dos.flush();
		} catch (IOException e) {
			LOGGER.error("zlib压缩数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(dos);
		}
		return os;
	}

	/**
	 * zlib压缩数据
	 * 
	 * @param data
	 *            待压缩数据
	 * 
	 * @param os
	 *            输出流，压缩结果
	 */
	public static ByteArrayOutputStream zlibStream(String data) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(os);
		byte[] dataByte = data.getBytes(Char.UTF8);
		try {
			dos.write(dataByte, 0, dataByte.length);
			dos.finish();
			dos.flush();
		} catch (IOException e) {
			LOGGER.error("zlib压缩数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(dos);
		}
		return os;
	}
	
	/**
	 * 压缩输入流，并返回压缩后的字节数组
	 * @param inputStream 带压缩的输入流
	 * @return 被压缩后的字节数组
	 */
	public byte[] zlib(InputStream inputStream) {
		DeflaterInputStream deflaterInputStream = new DeflaterInputStream(inputStream);
		byte[] result = copyStream(deflaterInputStream, 0);
		return result;
	}
	
	/**
	 * 压缩输入流，并返回压缩后的字符串。是ISO-8859-1编码的。异构的系统，socket通信的字节编码就是ISO-8859-1.
	 * @param inputStream 带压缩的输入流
	 * @return 被压缩后的字符串
	 */
	public String zlibs(InputStream inputStream) {
		DeflaterInputStream deflaterInputStream = new DeflaterInputStream(inputStream);
		try {
			return IOUtils.toString(deflaterInputStream, "ISO-8859-1");
		} catch (IOException e) {
			LOGGER.error("zlibs字节数组拷贝出错。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(deflaterInputStream);
		}
		return null;
	}
	
	/**
	 * zlib解压缩字节数组
	 * 
	 * @param data
	 *            待解压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] unzlib(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			LOGGER.error("zlib解压数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(o);
		}
		decompresser.end();
		return output;
	}

	/**
	 * zlib解压缩字节数组
	 * 
	 * @param data
	 *            待解压缩的数据
	 * @return string 解压缩后的数据
	 */
	public static String unzlibs(byte[] data) {
		String output = null;

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toString("UTF-8");
		} catch (Exception e) {
			LOGGER.error("zlib解压数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(o);
		}
		decompresser.end();
		return output;
	}
	
	/**
	 * zlib解压缩字节数组
	 * 
	 * @param data
	 *            待解压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] unzlib(String data) {
		byte[] output = new byte[0];

		byte[] dataByte = data.getBytes(Char.ISO88591);// 被压缩后的编码是ISO-8859-1的
		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(dataByte);

		ByteArrayOutputStream o = new ByteArrayOutputStream(dataByte.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			LOGGER.error("zlib解压数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(o);
		}
		decompresser.end();
		return output;
	}
	
	/**
	 * zlib解压缩字节数组
	 * 
	 * @param data
	 *            待解压缩的数据
	 * @return String 解压缩后的数据
	 */
	public static String unzlibs(String data) {
		String output = null;

		byte[] dataByte = data.getBytes(Char.ISO88591);// 被压缩后的编码是ISO-8859-1的
		Inflater decompresser = new Inflater();	
		decompresser.reset();
		decompresser.setInput(dataByte);

		ByteArrayOutputStream o = new ByteArrayOutputStream(dataByte.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toString("UTF-8");
		} catch (Exception e) {
			LOGGER.error("zlib解压数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(o);
		}
		decompresser.end();
		return output;
	}
	
	/**
	 * zlib解压数据流
	 * 
	 * @param is
	 *            输入流
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] unzlib(InputStream is) {
		byte[] output = new byte[0];
		InflaterInputStream iis = new InflaterInputStream(is);
		ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
		try {
			int i = 0;
			byte[] buf = new byte[1024];

			while ((i = iis.read(buf, 0, i)) > 0) {
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
			o.flush();
		} catch (IOException e) {
			LOGGER.error("zlib解压数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(o);
			IOUtils.closeQuietly(iis);
		}
		return output;
	}
	
	/**
	 * zlib解压数据流
	 * 
	 * @param is
	 *            输入流，待解压的数据
	 * @return string 解压缩后的数据
	 */
	public static String unzlibs(InputStream is) {
		String output = null;
		InflaterInputStream iis = new InflaterInputStream(is);
		ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
		try {
			int i = 0;
			byte[] buf = new byte[1024];

			while ((i = iis.read(buf, 0, i)) > 0) {
				o.write(buf, 0, i);
			}
			output = o.toString("UTF-8");
			o.flush();
		} catch (IOException e) {
			LOGGER.error("zlib解压数据异常。", e.getMessage());
		} finally {
			IOUtils.closeQuietly(o);
			IOUtils.closeQuietly(iis);
		}
		return output;
	}
	
	/**
	 * 解压输入流，去掉相应的头长度
	 * @param is 带解压的数据
	 * @param header 头长度
	 * @return 解压后的数据
	 */
	public static String unzlibs(InputStream is, int header) {
		byte[] destBytes = copyStream(is, header);
		// 解压缩
		String json = unzlibs(destBytes);
		return json;
	}

	/**
	 * 拷贝输入流到数组。可以使用IOUtils中的方法代替
	 * @param is 待拷贝的数据
	 * @param header 头长度
	 * @return 字节数组
	 */
	private static byte[] copyStream(InputStream is, int header) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int l = 0;
		byte[] sourceBytes = null;
		int total = 0;// 总的字节长度
		int length = 0;// 已经接收的字节长度
		try {
			for (;(l = is.read(buffer)) > -1;) {
				length += l;
				if (total == 0) {// 处理头
					byte[] len = new byte[header];
					System.arraycopy(buffer, 0, len, 0, header);
					total = ByteUtils.toInt(len);
					// 第一次就把头去掉了，避免后面的大字节拷贝
					byteArrayOutputStream.write(buffer, header, l - header);
				} else {
					if (length <= total) {// 中间
						byteArrayOutputStream.write(buffer, 0, l);
					} else {// 尾部
						int remain = total - (length - l) + header;
						if (remain > 0) {
							byte[] tail = new byte[remain];
							System.arraycopy(buffer, 0, tail, 0, remain);
							byteArrayOutputStream.write(tail, 0, remain);
						}
						break;
					}
				}
			}
			sourceBytes = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			LOGGER.error("读取输入流错误。", e.getMessage());
			return null;
		} finally {
			IOUtils.closeQuietly(byteArrayOutputStream);
		}
//		if (header >= 1) {// 有报文头长度，才拷贝
//			int sourceLength = sourceBytes.length;
//			if (LOGGER.isDebugEnabled()) {
//				LOGGER.debug("返回的字节数大小=[{}]", sourceLength);
//			}
//			// 数组拷贝，去掉长度大小
//			byte[] destBytes = new byte[sourceLength - header];
//			System.arraycopy(sourceBytes, header, destBytes, 0, sourceLength - header);
//			return destBytes;
//		} else {
//			return sourceBytes;
//		}
		return sourceBytes;
	}
	
	public static void main(String[] args) {
		// string--> byte--> string
		String data = "yinlei尹雷";
		byte[] bytes = zlib(data);
		System.out.println(Arrays.toString(bytes));
		data = unzlibs(bytes);
		System.out.println("string--> byte--> string " + data);
		
		// byte --> byte -->string
		byte[] dataByte = data.getBytes(Char.UTF8);
		data = unzlibs(zlib(dataByte));
		System.out.println("byte --> byte --> string " + data);
		
		// byte --> byte -->byte
		byte[] unByte = unzlib(zlib(dataByte));
		System.out.println("byte --> byte --> byte " + new String(unByte, Char.UTF8));
		
		// string --> string --> string
		data = zlibs(data);
		System.out.println(Arrays.toString(data.getBytes(Char.ISO88591)));
		data = unzlibs(data);
		System.out.println("string --> string --> string " + data);
		
		// string --> string --> byte
		data = zlibs(data);
		System.out.println(Arrays.toString(data.getBytes(Char.ISO88591)));
		byte[] datas = unzlib(data);
		System.out.println("string --> string --> byte " + new String(datas, Char.UTF8));
		
		// byte --> string --> byte
		data = "yinlei尹雷";
		String data2 = zlibs(data.getBytes(Char.UTF8));
		System.out.println(Arrays.toString(data2.getBytes(Char.ISO88591)));
		byte[] datas2 = unzlib(data2);
		System.out.println("byte --> string --> byte " + new String(datas2, Char.UTF8));
	}
}
