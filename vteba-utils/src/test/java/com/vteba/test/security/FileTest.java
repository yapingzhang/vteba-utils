package com.vteba.test.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.vteba.utils.json.Node;
import com.vteba.utils.serialize.ProtoUtils;

public class FileTest {

	public static void main(String[] args) throws IOException {
		Node node = new Node();
		node.setChecked(false);
		node.setChkDisabled(false);
		node.setId("678687967");
		node.setLevel(2);
		node.setName("yinlei尹雷");
		node.setNodeId(6767676L);
		
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < 10; i++) {
			Node node2 = new Node();
			node2.setChecked(false);
			node2.setChkDisabled(false);
			node2.setId("678687967");
			node2.setLevel(2);
			node2.setName("yinlei尹雷");
			node2.setNodeId(6767676L);
			nodes.add(node2);
		}
		
		node.setChildren(nodes);
		ProtoUtils.toBytes(node);
		long d = System.currentTimeMillis();
		File file = new File("c:\\aa.txt");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		
		byte[] nodeBytes = ProtoUtils.toBytes(node);
		fileOutputStream.write(nodeBytes);
		fileOutputStream.flush();
		fileOutputStream.close();
		System.out.println(System.currentTimeMillis() - d);
		
		d = System.currentTimeMillis();
		FileInputStream fileInputStream = new FileInputStream(file);
		node = ProtoUtils.fromBytes(IOUtils.toByteArray(fileInputStream));
		System.out.println(System.currentTimeMillis() - d);
	}

}
