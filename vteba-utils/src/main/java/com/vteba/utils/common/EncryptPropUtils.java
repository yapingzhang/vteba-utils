package com.vteba.utils.common;

import java.util.List;

import com.vteba.utils.cryption.DESUtils;

/**
 * Properties属性密文配置。同时提供很多获取属性的便捷工具方法。
 * 
 * @author yinlei
 * @date 2012-7-16 下午4:28:33
 */
public class EncryptPropUtils extends PropUtils {
    private List<String> encryptPropNames;
    private boolean exposeEncrypt = false;
    
    public EncryptPropUtils() {
        super();
    }

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            String decryptValue = DESUtils.getDecrypt(propertyValue);
            if (exposeEncrypt) {
                propertiesMap.put(propertyName, decryptValue);
            }
            return decryptValue;
        } else {
            return super.convertProperty(propertyName, propertyValue);
        }

    }

    private boolean isEncryptProp(String propName) {
        return encryptPropNames.contains(propName) ? true : false;
    }

    /**
     * 设置要被加密的属性
     * @param encryptPropNames 被加密的属性list
     */
    public void setEncryptPropNames(List<String> encryptPropNames) {
        this.encryptPropNames = encryptPropNames;
    }

    
    /**
     * 设置是否暴漏加密属性到系统中。（可通过静态方法获取）
     * @param exposeEncrypt true暴漏，false反之
     */
    public void setExposeEncrypt(boolean exposeEncrypt) {
        this.exposeEncrypt = exposeEncrypt;
    }

}
