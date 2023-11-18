package top.woaibocai.common.utils;

import top.woaibocai.model.common.User;

/**
 * 完成该功能需要使用到ThreadLocal
 * ThreadLocal是jdk所提供的一个线程工具类
 * 叫做线程变量
 * 意思是ThreadLocal中填充的变量属于当前线程
 * 该变量对其他线程而言是隔离的
 * 也就是说该变量是当前线程独有的变量
 * 使用该工具类可以实现在同一个线程进行数据的共享
 */
public class AuthContextUtil {
    private static final ThreadLocal<User> userInfoThreadLocal = new ThreadLocal<>() ;
    // 定义存储数据的静态方法
    public static void setUserInfo(User user) {
        userInfoThreadLocal.set(user);
    }
    // 定义获取数据的方法
    public static User getUserInfo() {
        return userInfoThreadLocal.get() ;
    }
    // 删除数据的方法
    public static void removeUserInfo() {
        userInfoThreadLocal.remove();
    }
    //创建threadlocal对象
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();
    //添加数据
    public static void set(User user){
        threadLocal.set(user);
    }
    //获取数据
    public static User get(){
        return threadLocal.get();
    }
    //删除数据
    public static void remove(){
        threadLocal.remove();
    }
}
