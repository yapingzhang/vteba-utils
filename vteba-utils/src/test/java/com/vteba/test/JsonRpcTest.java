package com.vteba.test;

import java.util.Date;

import org.junit.Test;

import com.alibaba.fastjson.TypeReference;
import com.vteba.utils.json.Bean;
import com.vteba.utils.json.FastJsonUtils;
import com.vteba.utils.json.JsonRpc;
import com.vteba.utils.json.JsonRpc.Error;

public class JsonRpcTest {
	@Test
	public void test() {
		Bean bean = new Bean();
		bean.setDate(new Date());
		bean.setUserName("尹雷yinlei");
		
		JsonRpc<Bean, Object> jsonRpc = new JsonRpc<Bean, Object>();
		jsonRpc.setId(2222);
		jsonRpc.setMethod("sub");
		//jsonRpc.setParams(new Date());
		jsonRpc.setParams(bean);
		Error error = new Error(2222, "haoa");
		jsonRpc.setError(error);
		
		String json = FastJsonUtils.toJson(jsonRpc);
		System.out.println(json);
		
		JsonRpc rpc = FastJsonUtils.fromJson(json, new TypeReference<JsonRpc<Bean, Object>>(){});//new TypeReference<JsonRpc<Bean, Object>>(){}
		//Date date = (Date)rpc.getParams();
		System.out.println(rpc);
	}
	
//	@Test
//	public void test2() {
//		JsonRpc2 jsonRpc2 = new JsonRpc2();
//		Bean bean = new Bean();
//		bean.setDate(new Date());
//		bean.setUserName("尹雷yinlei");
//		
//		jsonRpc2.setId(2222);
//		jsonRpc2.setMethod("sub");
//		jsonRpc2.setParams(bean);
//		Error error = new Error(2222, "haoa");
//		jsonRpc2.setError(error);
//		
//		String json = FastJsonUtils.toJson(jsonRpc2);
//		System.out.println(json);
//		
//		JsonRpc2 rpc2 = FastJsonUtils.fromJson(json, JsonRpc2.class);//new TypeReference<JsonRpc<Bean, Object>>(){}
//		System.out.println(rpc2);
//	}
}
