package com.qiuyongchen.windweibo.db;

/**
 * 数据库信息，包括数据库名字、数据库版本、数据库中的表的详细信息。
 * Created by qiuyongchen on 2016/2/22.
 */
public class DBinfo {

    public static class DB {

        // 数据库的名字
        public static String DB_NAME = "WINDWEIBO";

        // 数据库的版本
        public static int DB_VERSION = 1;

    }

    public static class TABLE {

        // 用户信息表的名字
        private static final String USER_INFO_TB_NAME = "user_info";

        // 创建用户信息表
        public static String CREATE_USER_INFO_TABLE = "CREATE TABLE IF NOT EXISTS  " +
                USER_INFO_TB_NAME +
                " ( _id INTEGER PRIMARY KEY,uid TEXT, user_name TEXT, access_token " +
                "TEXT, expires_in TEXT, refresh_token TEXT, isDefault TEXT, user_icon BLOB)";

        // 数据库升级时，直接丢弃旧数据库
        public static String USER_INFO_DROP = "DROP TABLE " + USER_INFO_TB_NAME;

    }

}
