package com.vteba.test;

import java.io.Serializable;


/**
 * @author haitao.yao Dec 14, 2010
 */
public class TestBean implements Serializable {
    /**
* 
*/
    private static final long serialVersionUID = -5994966479456252766L;
    protected String name;
    protected int age;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }
}
