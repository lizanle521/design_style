package com.lzl.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Created by lzl on 2017/5/22.
 */
public class TestHelloWorld {
    @Test
    public void testLogin(){
        // 1. 获取securityManager工程,此处使用ini文件初始化SecurityManager
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // 得到SecurityManager实例，并绑定SecurityUtils
        SecurityManager instance = factory.getInstance();
        SecurityUtils.setSecurityManager(instance);

        //得到subject以及创建用户名密码Token
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123", true);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        if(subject.isAuthenticated()){
            System.out.print("登录成功");
        }
        subject.logout();

    }
}
