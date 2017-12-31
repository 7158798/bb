package com.zhgtrade.deal.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-16 11:35
 */
public interface RowMapper<T> {

    T mapRow(ResultSet rs) throws SQLException;

}
