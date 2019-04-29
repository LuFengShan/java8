package com.java8.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 开发者 : SGX <BR>
 * 时间：2018年7月17日 下午2:33:01 <BR>
 * 变更原因： <BR>
 * 首次开发时间：2018年7月17日 下午2:33:01 <BR>
 * 描述： 数据库连接工具<BR>
 * 版本：V1.0
 */
public class DBUtils {
  // 地址
  private static String url =
      "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=true";
  // 用户名
  private static String user = "root";
  // 密码
  private static String password = "root";

  public static void main(String[] args) {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      // 加载驱动
      Class.forName("com.mysql.jdbc.Driver");
      // 创建连接
      conn = DriverManager.getConnection(url, user, password);
      // 创建查询面板
      stmt = conn.createStatement();
      // 查询语句
      String sql = "SELECT t.num, t.name, t.age FROM tb_student t";
      // 执行查询
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String cno = rs.getString(1);
        String cname = rs.getString(2);
        String tno = rs.getString(3);
        System.out.println(cno + " : " + cname + " : " + tno);
      }
      // 执行增加，修改
      sql = "INSERT INTO tb_student (num, name, age) VALUES ('541113140132', 'xu', '19')";
      // sql = "UPDATE tb_student t SET t.age = '26' WHERE t.num = '541113140132'";
      int insertNum = stmt.executeUpdate(sql);
      System.out.println(insertNum);
      // 删除数据
      // boolean b = stmt.execute("DELETE FROM tb_student WHERE num = '541113140132'");
      // System.out.println(b);
    } catch (ClassNotFoundException | SQLException e) {
      // 可能会出现类加载异常，sql查询异常
      e.printStackTrace();
    } finally {
      // 用完后要关闭连接
      try {
        if (rs != null) {
          rs.close();
        }
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

}

