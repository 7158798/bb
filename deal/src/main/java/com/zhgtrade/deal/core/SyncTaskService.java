package com.zhgtrade.deal.core;

/**
 * 异步任务
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016-06-06 12:54
 */
public interface SyncTaskService {

    /**
     * 执行异步任务
     * @param runnable
     */
    void execute(Runnable runnable);

    /**
     * 获取任务数量
     * @return
     */
    int count();
}
