package cn.vitem.webmagic.common.db;


import cn.vitem.webmagic.trademark.entity.TradeMarkImage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBHelper {  
    public static final String url = "jdbc:mysql://localhost:3306/crawler??useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=true";
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "135790";
  
    public Connection conn = null;  
    public PreparedStatement pst = null;  

  
    public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }

    public static Connection getConnection() {
        Connection connection = null;  //创建用于连接数据库的Connection对象
        try {
            Class.forName(name);// 加载Mysql数据驱动
            connection = DriverManager.getConnection(url, user, password);// 创建数据连接

        } catch (Exception e) {
            System.out.println("数据库连接失败" + e.getMessage());
        }
        return connection; //返回所建立的数据库连接
    }

    public static void insert(List<TradeMarkImage> tradeMarks) {
        for (TradeMarkImage tradeMarkImage:tradeMarks ) {
            insert(tradeMarkImage);
        }
    }

    public static void insert(TradeMarkImage tradeMark) {

        if(tradeMark.getId()==null){
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            tradeMark.setId(uuid);
        }

        String sql = "INSERT INTO trade_mark_image( " +
                "id," +
                "url, " +
                "filePath," +
                "anNum, " +
                "anType, " +
                "requestBody," +
                "postUrl," +
                "Referer," +
                "Host," +
                "pageNum," +
                "fileName," +
                "createTime)" +
                " VALUES ('"+
                tradeMark.getId()+"','"+
                tradeMark.getUrl()+"','"+
                tradeMark.getFilePath()+"','"+
                tradeMark.getAnNum()+"','" +
                tradeMark.getAnType()+"','"+
                tradeMark.getRequestBody()+"','"+
                tradeMark.getPostUrl()+"','" +
                tradeMark.getReferer()+"','"+
                tradeMark.getHost()+"','" +
                tradeMark.getPageNum()+"','" +
                tradeMark.getFileName()+"'," +
                "now())";

        Statement st;


        Connection connection = DBHelper.getConnection(); // 首先要获取连接，即连接到数据库

        try {
            st = (Statement) connection.createStatement();    // 创建用于执行静态sql语句的Statement对象

            int count = st.executeUpdate(sql);  // 执行插入操作的sql语句，并返回插入数据的个数

            System.out.println("向 trade_mark_image 表中插入 " + count + " 条数据"); //输出插入操作的处理结果

            connection.close();   //关闭数据库连接

        } catch (SQLException e) {
            System.out.println("插入数据失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<TradeMarkImage> getAllTradeMarkList() {

        List<TradeMarkImage> tradeMarkImages = new ArrayList<TradeMarkImage>();

        String sql = "select id from trade_mark_image_1560 ";//SQL语句
        Connection connection = DBHelper.getConnection();
        try {
            PreparedStatement st =  connection.prepareStatement(sql);
            ResultSet ret = null;
            ret = st.executeQuery();//执行语句，得到结果集

            while (ret.next()) {
                //
                String id = ret.getString("id");
                TradeMarkImage tradeMarkImage = new TradeMarkImage();
                tradeMarkImage.setId(id);
                tradeMarkImages.add(tradeMarkImage);
            }//显示数据
            ret.close();
            connection.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tradeMarkImages;
    }

    public static void updateTradeMarkList(TradeMarkImage tradeMarkImage) {

        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String sql = " update trade_mark_image_1560 set id = '"+uuid+"' where id = '"+tradeMarkImage.getId()+"';";
        Connection connection = DBHelper.getConnection();
        try {
            Statement st = connection.createStatement();    // 创建用于执行静态sql语句的Statement对象
            int count = st.executeUpdate(sql);  // 执行插入操作的sql语句，并返回插入数据的个数
            System.out.println("向 trade_mark_image 表中update " + count + " 条数据"); //输出插入操作的处理结果
            connection.close();   //关闭数据库连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ;
    }

    public static void main(String[] args) {
//        TradeMarkImage tradeMarkImage = new TradeMarkImage();
//        tradeMarkImage.setUrl("Url");
//        tradeMarkImage.setAnNum("AnNum");
//        tradeMarkImage.setAnType("AnType");
//        tradeMarkImage.setFilePath("FilePath");
//        tradeMarkImage.setPageNum("PageNum");
//        tradeMarkImage.setPostUrl("PostUrl");
//        tradeMarkImage.setReferer("Referer");
//        tradeMarkImage.setRequestBody("RequestBody");
//        tradeMarkImage.setHost("host");
//        insert(tradeMarkImage);
//        String uuid = UUID.randomUUID().toString().replaceAll("-","");
//        System.out.println(uuid);
//        getAllTradeMarkList().forEach(DBHelper::updateTradeMarkList);


        for (int i = 0; i < 1000 ; i++) {

            String uuid = UUID.randomUUID().toString().replaceAll("-","");

            System.out.println(uuid);
        }


    }
}  