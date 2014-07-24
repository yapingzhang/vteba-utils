package com.vteba.test;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.vteba.utils.json.Node;
import com.vteba.utils.serialize.Kryos;

public class TestKryo {

    public static void main(String[] aa) {
        Node node = new Node();
        node.setName("hasdf");
        List<Node> children = new ArrayList<Node>();
        Node node2 = new Node();
        node2.setName("aa");
        //children.add(node2);

        ArrayListMultimap<String, Node> multimap = ArrayListMultimap.create();
        multimap.put("111", node2);
        
        Node node3 = new Node();
        node3.setName("111aa");
        
        multimap.put("111", node3);
        
        children = new ArrayList<Node>(multimap.get("111"));
        
        node.setChildren(children);

        byte[] aaa = Kryos.get().serialize(node);
        Node de = Kryos.get().deserialize(aaa);
        System.out.println(de);
    }
}
