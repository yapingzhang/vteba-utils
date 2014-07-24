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
	private Long nodeId;// 节点Id，如果节点id是Long类型时，使用此字段
	private String name;//显示的名字（节点名称）
	private Boolean open;//是否打开
	private Boolean checked;//是否选中
	private Boolean nocheck;//不显示checked
	private Boolean chkDisabled;//禁用checked
	private Integer level;// 层级
	private List<Node> children;//子节点
	private Long parentId;//父节点ID
	private Boolean parent;//是否父节点，非叶子节点

	public Node() {
        super();
    }
	
	public Node(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Node(Long nodeId, String name) {
        super();
        this.nodeId = nodeId;
        this.name = name;
    }
	
	public Node(String id, String name, Long parentId) {
        super();
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

	public Node(Long nodeId, String name, Long parentId) {
        super();
        this.nodeId = nodeId;
        this.name = name;
        this.parentId = parentId;
    }
	
	public Node(String id, String name, Long parentId, Boolean parent) {
        super();
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.parent = parent;
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

	public Node(Long nodeId, String name, List<Node> children) {
		super();
		this.nodeId = nodeId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getParent() {
        return parent;
    }

    public void setParent(Boolean parent) {
        this.parent = parent;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
    
}
