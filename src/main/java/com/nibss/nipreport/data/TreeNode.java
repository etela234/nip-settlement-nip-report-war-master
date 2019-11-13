/*
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */
package com.nibss.nipreport.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oluwaseun Ogunjimi
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TreeNode implements Data {

    @XmlElement
    private String label;
    @XmlElement
    private String iconType;
    @XmlElement
    private String data;
    @XmlElement
    private String parentData;
    @XmlElement
    private List<TreeNode> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getParentData() {
        return parentData;
    }

    public void setParentData(String parentData) {
        this.parentData = parentData;
    }

//
    public void addChild(TreeNode child) {
        if (child == null) {
            return;
        }
        if (children == null) {
            children = new ArrayList<TreeNode>();
        }
        children.add(child);
    }

}
