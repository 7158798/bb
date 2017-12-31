package com.ruizton.main.admin.task;

import com.ruizton.main.service.admin.DateWalletStatisticService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 比特家
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2017/1/11
 */
public class DateWalletStatisticTask implements TaskExecutor {
    @Autowired
    private DateWalletStatisticService dateWalletStatisticService;

    @Override
    public void execute() {
        dateWalletStatisticService.nonBuildWalletStatisticLog();
    }
}
