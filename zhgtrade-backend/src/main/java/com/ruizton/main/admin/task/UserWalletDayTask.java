package com.ruizton.main.admin.task;

import com.ruizton.main.service.admin.UserWalletDayService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuyuanbo on 2017/4/17.
 */
/*定时任务*/
public class UserWalletDayTask implements TaskExecutor{
        @Autowired
        private UserWalletDayService userWalletDayService;
        @Override
        public void execute() {
           int count= userWalletDayService.updateWallet();
                System.out.println("更新用户每日金额");
                System.out.println("更新条数："+count);
        }

        public void deleteMonthAgo(){
                int count=userWalletDayService.deleteMonthAgo();
                System.out.println("删除一个月前记录");
                System.out.println("删除条数："+count);
        }


}
