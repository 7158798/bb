package com.zhgtrade.deal.util;

import java.sql.ResultSet;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-05-11 17:45
 */
public interface Parser {

    <T> T parse(ResultSet rs, Class<T> clazz) throws Exception;

}
