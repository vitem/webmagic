package us.codecraft.webmagic.samples.vitem.db;

import java.sql.ResultSet;

public class Demo {  
  
    static String sql = null;  
    static DBHelper db1 = null;  
    static ResultSet ret = null;  
  




//    /* 更新符合要求的记录，并返回更新的记录数目*/
//    public static void update() {
//        conn = getConnection(); //同样先要获取连接，即连接到数据库
//        try {
//            String sql = "update staff set wage='2200' where name = 'lucy'";// 更新数据的sql语句
//
//            st = (Statement) conn.createStatement();    //创建用于执行静态sql语句的Statement对象，st属局部变量
//
//            int count = st.executeUpdate(sql);// 执行更新操作的sql语句，返回更新数据的个数
//
//            System.out.println("staff表中更新 " + count + " 条数据");      //输出更新操作的处理结果
//
//            conn.close();   //关闭数据库连接
//
//        } catch (SQLException e) {
//            System.out.println("更新数据失败");
//        }
//    }

}  