package com.vteba.utils.serialize;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.dyuproject.protostuff.ByteArrayInput;
import com.dyuproject.protostuff.CodedInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.ProtobufException;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * 基于Protostuff（基于Google Protobuf）的序列化工具
 * 
 * @author yinlei
 * @date 2014-8-24
 */
public final class Protos {
	private static final ConcurrentMap<Class<?>, Schema<?>> schemaMap = new ConcurrentHashMap<Class<?>, Schema<?>>();

	private Protos() {
	}

	/**
	 * 使用字节数组创建一个protostuff pipe。
	 * @param data 字节数组
	 */
	public static Pipe newPipe(byte[] data) {
		return newPipe(data, 0, data.length);
	}

	/**
	 * 使用字节数组创建一个protostuff pipe。
	 * @param data 字节数组
	 * @param offset 数组偏移量
	 * @param len 使用字节数组的长度，从偏移量开始算起
	 */
	public static Pipe newPipe(byte[] data, int offset, int len) {
		final ByteArrayInput byteArrayInput = new ByteArrayInput(data, offset,
				len, true);
		return new Pipe() {
			protected Input begin(Pipe.Schema<?> pipeSchema) throws IOException {
				return byteArrayInput;
			}

			protected void end(Pipe.Schema<?> pipeSchema, Input input,
					boolean cleanupOnly) throws IOException {
				if (cleanupOnly)
					return;

				assert input == byteArrayInput;
			}
		};
	}

	/**
	 * 使用输入流 {@link InputStream} 创建一个protostuff pipe。
	 * @param in 输入流
	 */
	public static Pipe newPipe(final InputStream in) {
		final CodedInput codedInput = new CodedInput(in, true);
		return new Pipe() {
			protected Input begin(Pipe.Schema<?> pipeSchema) throws IOException {
				return codedInput;
			}

			protected void end(Pipe.Schema<?> pipeSchema, Input input,
					boolean cleanupOnly) throws IOException {
				if (cleanupOnly)
					return;

				assert input == codedInput;
			}
		};
	}

	/**
	 * 反序列化，根据给定的{@code schema}，使用字节数组{@code data}填充对象{@code message}。
	 * @param data 字节数组
	 * @param message 要被填充的对象
	 * @param schema 对象message的schema
	 */
	public static <T> void mergeFrom(byte[] data, T message, Schema<T> schema) {
		ProtostuffIOUtil.mergeFrom(data, message, schema);
	}

	/**
	 * 反序列化，使用字节数组{@code data}填充对象{@code message}。
	 * @param data 字节数组
	 * @param message 要被填充的对象
	 */
	public static <T> void mergeFrom(byte[] data, T message) {
		Schema<T> schema = getSchema(message);
		ProtostuffIOUtil.mergeFrom(data, message, schema);
	}
	
	/**
	 * 反序列化，根据给定的{@code schema}，使用字节数组{@code data}填充对象{@code message}。
	 * @param data 字节数组
	 * @param offset 数组偏移量
	 * @param length 使用字节数组的长度，从偏移量开始算起
	 * @param message 被序列化的对象
	 * @param schema message的schema
	 */
	public static <T> void mergeFrom(byte[] data, int offset, int length,
			T message, Schema<T> schema) {
		ProtostuffIOUtil.mergeFrom(data, offset, length, message, schema);
	}

	/**
	 * 反序列化，使用字节数组{@code data}填充对象{@code message}。
	 * @param data 字节数组
	 * @param offset 数组偏移量
	 * @param length 使用字节数组的长度，从偏移量开始算起
	 * @param message 被序列化的对象
	 */
	public static <T> void mergeFrom(byte[] data, int offset, int length, T message) {
		Schema<T> schema = getSchema(message);
		ProtostuffIOUtil.mergeFrom(data, offset, length, message, schema);
	}
	
	/**
	 * 根据给定的{@code schema}，使用输入流 {@link InputStream} 填充对象{@code message}
	 * @param in 输入流（数据来源）
	 * @param message 被merge的对象
	 * @param schema schema
	 * @throws IOException
	 */
	public static <T> void mergeFrom(InputStream in, T message, Schema<T> schema)
			throws IOException {
		ProtostuffIOUtil.mergeFrom(in, message, schema);
	}

	/**
	 * 使用输入流 {@link InputStream} 填充对象{@code message}
	 * @param in 输入流（数据来源）
	 * @param message 被merge的对象
	 * @throws IOException
	 */
	public static <T> void mergeFrom(InputStream in, T message)
			throws IOException {
		Schema<T> schema = getSchema(message);
		ProtostuffIOUtil.mergeFrom(in, message, schema);
	}
	
	/**
	 * 根据给定的{@code schema}，使用输入流 {@link InputStream} 填充对象{@code message}。
	 * 
	 * 将使用 {@code buffer} 内部的 byte array来读取对象message。
	 * 
	 * @param in 输入流（数据来源）
	 * @param message 被merge的对象
	 * @param schema schema
	 * @param buffer LinkedBuffer 缓冲区
	 * @throws IOException
	 */
	public static <T> void mergeFrom(InputStream in, T message,
			Schema<T> schema, LinkedBuffer buffer) throws IOException {
		ProtostuffIOUtil.mergeFrom(in, message, schema, buffer);
	}

	/**
	 * 根据给定的{@code schema}，使用输入流 {@link InputStream} 填充对象{@code message}。
	 * 
	 * 将使用 {@code buffer} 内部的 byte array来读取对象message。
	 * 
	 * @param in 输入流（数据来源）
	 * @param message 被merge的对象
	 * @param buffer LinkedBuffer 缓冲区
	 * @throws IOException
	 */
	public static <T> void mergeFrom(InputStream in, T message, LinkedBuffer buffer) throws IOException {
		Schema<T> schema = getSchema(message);
		ProtostuffIOUtil.mergeFrom(in, message, schema, buffer);
	}
	
	/**
	 * 根据给定的{@code schema}，使用输入流 {@link InputStream} 填充带分隔符的对象{@code message} (delimited)
	 * @param in 输入流（数据来源）
	 * @param message 被merge的对象
	 * @param schema schema
	 * @throws IOException
	 * 
	 * @return the size of the message
	 */
	public static <T> int mergeDelimitedFrom(InputStream in, T message,
			Schema<T> schema) throws IOException {
		return ProtostuffIOUtil.mergeDelimitedFrom(in, message, schema);
	}

	/**
	 * 根据给定的{@code schema}，使用输入流 {@link InputStream} 填充带分隔符的对象{@code message} (delimited)。
	 * 
	 * <p>被限制的带分隔符的message对象的size不能大于buffer的size或者容量，要不然{@link ProtobufException} "size limit exceeded"
	 * 会被抛出。
	 * 
	 * <p>The delimited message size must not be larger than the {@code buffer}'s
	 * size/capacity. {@link ProtobufException} "size limit exceeded" is thrown
	 * otherwise.
	 * @param in 输入流（数据来源）
	 * @param message 被merge的对象
	 * @param schema schema
	 * @throws IOException
	 * 
	 * @return the size of the message
	 */
	public static <T> int mergeDelimitedFrom(InputStream in, T message,
			Schema<T> schema, LinkedBuffer buffer) throws IOException {
		return ProtostuffIOUtil.mergeDelimitedFrom(in, message, schema, buffer);
	}

	/**
	 * Used by the code generated messages that implement
	 * {@link java.io.Externalizable}. Merges from the {@link DataInput}.
	 * 
	 * @return the size of the message
	 */
	public static <T> int mergeDelimitedFrom(DataInput in, T message,
			Schema<T> schema) throws IOException {
		return ProtostuffIOUtil.mergeDelimitedFrom(in, message, schema);
	}

	/**
	 * 使用给定的schema将对象序列化成字节数组。
	 * 
	 * @param message
	 *            要被序列化的对象
	 * @param schema
	 *            被序列化对象的Schema
	 * @param buffer
	 *            LinkedBuffer
	 * @return 对象序列化后的字节数组
	 */
	public static <T> byte[] toByteArray(T message, Schema<T> schema,
			LinkedBuffer buffer) {
		return ProtostuffIOUtil.toByteArray(message, schema, buffer);
	}

	/**
	 * 将对象序列化成字节数组。
	 * 
	 * @param message
	 *            要被序列化的对象
	 * @param buffer
	 *            LinkedBuffer
	 * @return 对象序列化后的字节数组
	 */
	public static <T> byte[] toByteArray(T message, LinkedBuffer buffer) {
		Schema<T> schema = getSchema(message);
		return toByteArray(message, schema, buffer);
	}

	/**
	 * 将对象序列化成字节数组。LinkedBuffer是LinkedBuffer.allocate(2048);
	 * 
	 * @param message
	 *            要被序列化的对象
	 * @return 对象序列化后的字节数组
	 */
	public static <T> byte[] toByteArray(T message) {
		LinkedBuffer buffer = LinkedBuffer.allocate(1024);
		Schema<T> schema = getSchema(message);
		return toByteArray(message, schema, buffer);
	}

	/**
	 * 序列化，使用给定的schema将对象 {@code message} 写进buffer {@link LinkedBuffer}。
	 * 
	 * @return 对象大小（序列化后的字节数组的长度）
	 */
	public static <T> int writeTo(LinkedBuffer buffer, T message,
			Schema<T> schema) {
		return ProtostuffIOUtil.writeTo(buffer, message, schema);
	}

	/**
	 * 序列化，将对象 {@code message} 写进buffer {@link LinkedBuffer}。
	 * 
	 * @return 对象大小（序列化后的字节数组的长度）
	 */
	public static <T> int writeTo(LinkedBuffer buffer, T message) {
		Schema<T> schema = getSchema(message);
		return ProtostuffIOUtil.writeTo(buffer, message, schema);
	}
	
	/**
	 * 序列化，使用给定的schema将对象 {@code message} 写到输出流 {@link OutputStream}。
	 * 
	 * @return 对象大小（序列化后的字节数组的长度）
	 */
	public static <T> int writeTo(final OutputStream out, final T message,
			final Schema<T> schema, final LinkedBuffer buffer)
			throws IOException {

		return ProtostuffIOUtil.writeTo(out, message, schema, buffer);
	}

	/**
	 * 序列化，将对象 {@code message} 写到输出流 {@link OutputStream}。
	 * 
	 * @return 对象大小（序列化后的字节数组的长度）
	 */
	public static <T> int writeTo(final OutputStream out, final T message, final LinkedBuffer buffer)
			throws IOException {
		Schema<T> schema = getSchema(message);
		return ProtostuffIOUtil.writeTo(out, message, schema, buffer);
	}
	
	/**
	 * 序列化，将对象 {@code message} 写到输出流 {@link OutputStream}。
	 * 
	 * @return （序列化后的字节数组的长度）
	 */
	public static <T> int writeTo(final OutputStream out, final T message)
			throws IOException {
		Schema<T> schema = getSchema(message);
		LinkedBuffer buffer = LinkedBuffer.allocate(1024);
		return ProtostuffIOUtil.writeTo(out, message, schema, buffer);
	}
	
	/**
	 * 序列化以本身长度开始的{@code message}对象，到一个输出流{@link OutputStream}中。
	 * 
	 * <p>Serializes the {@code message}, prefixed with its length, into an
	 * {@link OutputStream}.
	 * 
	 * @return the size of the message
	 */
	public static <T> int writeDelimitedTo(final OutputStream out,
			final T message, final Schema<T> schema, final LinkedBuffer buffer)
			throws IOException {
		return ProtostuffIOUtil.writeDelimitedTo(out, message, schema, buffer);
	}

	/**
	 * Used by the code generated messages that implement
	 * {@link java.io.Externalizable}. Writes to the {@link DataOutput}.
	 * 
	 * @return the size of the message.
	 */
	public static <T> int writeDelimitedTo(DataOutput out, T message,
			Schema<T> schema) throws IOException {

		return ProtostuffIOUtil.writeDelimitedTo(out, message, schema);
	}

	/**
	 * 序列化，使用指定的schema，序列化带分隔符（被分割的）的对象 {@code messages} (delimited) 到一个输出流 {@link OutputStream}
	 * 
	 * @return the bytes written（被写入的字节数）
	 */
	public static <T> int writeListTo(final OutputStream out,
			final List<T> messages, final Schema<T> schema,
			final LinkedBuffer buffer) throws IOException {

		return ProtostuffIOUtil.writeListTo(out, messages, schema, buffer);
	}

	/**
	 * 序列化带分隔符（被分割的）的对象 {@code messages} (delimited) 到一个输出流 {@link OutputStream}
	 * 
	 * @return the bytes written（被写入的字节数）
	 */
	public static <T> int writeListTo(final OutputStream out,
			final List<T> messages, final LinkedBuffer buffer) throws IOException {
		Schema<T> schema = getSchema(messages.get(0));
		return ProtostuffIOUtil.writeListTo(out, messages, schema, buffer);
	}
	
	/**
	 * 序列化带分隔符（被分割的）的对象 {@code messages} (delimited) 到一个输出流 {@link OutputStream}
	 * 
	 * @return the bytes written（被写入的字节数）
	 */
	public static <T> int writeListTo(final OutputStream out, final List<T> messages) throws IOException {
		Schema<T> schema = getSchema(messages.get(0));
		LinkedBuffer buffer = LinkedBuffer.allocate(1024);
		return ProtostuffIOUtil.writeListTo(out, messages, schema, buffer);
	}
	
	/**
	 * 使用指定的schema，从 {@link InputStream} 中解析被分割的对象 {@code messages} (delimited) 。
	 * 
	 * @return the list containing the messages。（messages中包含的list）
	 */
	public static <T> List<T> parseListFrom(final InputStream in, final Schema<T> schema) throws IOException {
		return ProtostuffIOUtil.parseListFrom(in, schema);
	}
	
	/**
	 * Optimal/Optional mergeDelimitedFrom - If the message does not fit the
	 * buffer, no merge is done and this method will return false.
	 * 
	 * This is strictly for reading a single message from the stream because the
	 * buffer is aggressively filled when reading the delimited size (which
	 * could result into reading more bytes than it has to).
	 * 
	 * The remaining bytes will be drained (consumed and discared) when the
	 * message is too large.
	 */
	public static <T> boolean optMergeDelimitedFrom(InputStream in, T message,
			Schema<T> schema, LinkedBuffer buffer) throws IOException {
		return optMergeDelimitedFrom(in, message, schema, true, buffer);
	}

	/**
	 * Optimal/Optional mergeDelimitedFrom - If the message does not fit the
	 * buffer, no merge is done and this method will return false.
	 * 
	 * This is strictly for reading a single message from the stream because the
	 * buffer is aggressively filled when reading the delimited size (which
	 * could result into reading more bytes than it has to).
	 */
	public static <T> boolean optMergeDelimitedFrom(InputStream in, T message,
			Schema<T> schema, boolean drainRemainingBytesIfTooLarge,
			LinkedBuffer buffer) throws IOException {
		return ProtostuffIOUtil.optMergeDelimitedFrom(in, message, schema, drainRemainingBytesIfTooLarge, buffer);
	}

	/**
	 * Optimal writeDelimitedTo - The varint32 prefix is written to the buffer
	 * instead of directly writing to outputstream.
	 * 
	 * @return the size of the message
	 */
	public static <T> int optWriteDelimitedTo(final OutputStream out,
			final T message, final Schema<T> schema, final LinkedBuffer buffer)
			throws IOException {
		return ProtostuffIOUtil.optWriteDelimitedTo(out, message, schema, buffer);
	}
	
	/**
	 * 获取对象schema，首先从缓存中获取，如果没有取到，再生成一个，并且返回缓存中。
	 * @param object 要被序列化的对象
	 * @return 对象schema
	 */
	@SuppressWarnings("unchecked")
	private static <T> Schema<T> getSchema(T object) {
		Class<?> clazz = object.getClass();
		Schema<?> schema = schemaMap.get(clazz);
		if (schema == null) {
			schema = RuntimeSchema.getSchema(clazz);
			schemaMap.put(clazz, schema);
		}
		return (Schema<T>) schema;
	}
	
	/**
	 * Schema第一次获取时，会有一些时间消耗，为提高性能，可以实现将需要的
	 * schema全部加载好，放入缓存中。
	 * @param clazz 设置该类的schema
	 */
	public static void setSchema(Class<?> clazz) {
		Schema<?> schema = RuntimeSchema.getSchema(clazz);
		schemaMap.put(clazz, schema);
	}
}
