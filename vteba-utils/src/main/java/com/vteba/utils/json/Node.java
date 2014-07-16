package com.vteba.utils.json;

import java.io.Serializable;
import java.util.List;

/**
 * 树形节点，用于Json显示。
 * 
 * @author yinlei
 * @date 2014-03-26 21:30
 */
public class Node implements Serializable {
	private static final long serialVersionUID = -10392254772186054L;
	
	private String id;//节点id
	private String name;//显示的名字
	private Boolean open;//是否打开
	private Boolean checked;//是否选中
	private Boolean nocheck;//不显示checked
	private Boolean chkDisabled;//禁用checked
	private Integer level;// 层级
	private List<Node> children;//子节点
	private String indexId;//指标Id

	public Node(Long id, String name) {
		super();
		this.id = id.toString();
		this.name = name;
	}
	
	public Node(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Node(String id, String name, Integer level) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
	}
	
	public Node(String id, String name, List<Node> children) {
		super();
		this.id = id;
		this.name = name;
		this.children = children;
	}

	public Node(Long id, String name, List<Node> children) {
		super();
		this.id = id.toString();
		this.name = name;
		this.children = children;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}

	public Boolean getChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(Boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }
	
}
