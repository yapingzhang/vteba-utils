package com.vteba.test;

import java.util.Date;

import com.vteba.utils.json.FastJsonUtils;
import com.vteba.utils.json.JacksonUtils;
import com.vteba.utils.reflection.AsmUtils;
import com.vteba.utils.serialize.Kryos;
import com.vteba.utils.serialize.MarshaUtils;
import com.vteba.utils.serialize.ProtoUtils;
import com.vteba.utils.serialize.Protos;

public class TestUser {

	private String userName;
	private int age;
	private Date date;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	//fastjson比jackson稍微快一点5%左右
	//一次测试，protos、未注册kryo、protoUtils、marshutils、注册kryo
	//多次循环，protos、protosutils、marshautils、注册kryo、未注册kryo
	public static void main(String[] aa) {
		TestUser user = new TestUser();
		user.setAge(34);
		user.setDate(new Date());
		user.setUserName("wojiao尹雷");
		
		String json = null;
		long dd = System.currentTimeMillis();
		json = FastJsonUtils.toJson(user);
		FastJsonUtils.fromJson(json, TestUser.class);
		System.out.println("fastjson:" + (System.currentTimeMillis() - dd));
		
		dd = System.currentTimeMillis();
		json = JacksonUtils.get().toJson(user);
		JacksonUtils.get().fromJson(json, TestUser.class);
		System.out.println("jackson:" + (System.currentTimeMillis() - dd));
		
		ProtoUtils.toBytes(user);
		AsmUtils.get().createConstructorAccess(TestUser.class);
		
		int loop = 1000;
		byte[] bytes = null;
		long d = System.currentTimeMillis();
		bytes = ProtoUtils.toBytes(user);
		for (int i = 0; i < loop; i++) {
			bytes = ProtoUtils.toBytes(user);
			ProtoUtils.fromBytes(bytes);
		}
		
		System.out.println("Protos的序列化时间是：" + (System.currentTimeMillis() - d));
		
		d = System.currentTimeMillis();
		bytes = Protos.toByteArray(user);
		for (int i = 0; i < loop; i++) {
			bytes = Protos.toByteArray(user);
			TestUser message = new TestUser();
			Protos.mergeFrom(bytes, message);
		}
		System.out.println("ProtoUtils的序列化时间是：" + (System.currentTimeMillis() - d));
		
		d = System.currentTimeMillis();
		bytes = MarshaUtils.toBytes(user);
		for (int i = 0; i < loop; i++) {
			bytes = MarshaUtils.toBytes(user);
			MarshaUtils.fromBytes(bytes);
		}
		
		System.out.println("MarshaUtils的序列化时间是：" + (System.currentTimeMillis() - d));
		
		d = System.currentTimeMillis();
		bytes = Kryos.toBytes(user);
		for (int i = 0; i < loop; i++) {
			bytes = Kryos.toBytes(user);
			Kryos.fromBytes(bytes, TestUser.class);
		}
		
		System.out.println("Kryos注册的序列化时间是：" + (System.currentTimeMillis() -d));
		
		d = System.currentTimeMillis();
		bytes = Kryos.serialize(user);
		for (int i = 0; i < loop; i++) {
			bytes = Kryos.serialize(user);
			Kryos.deserialize(bytes);
		}
		
		System.out.println("Kryos未注册的序列化时间是：" + (System.currentTimeMillis() -d));
	}
}

