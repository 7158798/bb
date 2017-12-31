package com.zhgtrade.deal.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-05-16 11:35
 */
public interface RowMapper<T> {

    T mapRow(ResultSet rs) throws SQLException;

}
