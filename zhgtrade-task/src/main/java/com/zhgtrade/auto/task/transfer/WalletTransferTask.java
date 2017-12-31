package com.zhgtrade.auto.task.transfer;

import com.alibaba.fastjson.JSONObject;
import com.ruizton.main.Enum.WalletTransferStatus;
import com.ruizton.main.comm.ConstantMap;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.WalletTransfer;
import com.ruizton.main.service.front.FrontUserService;
import com.ruizton.main.service.front.WalletTransferService;
import com.ruizton.util.CollectionUtils;
import com.ruizton.util.ConstantKeys;
import com.ruizton.util.HttpUtils;
import com.ruizton.util.SignatureUtil;
import com.ruizton.util.Utils;
import com.zhgtrade.auto.task.TaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 比特家
 * CopyRight : www.btc58.cc
 * Author : xxp
 * Date： 2016/7/27
 */
public class WalletTransferTask implements TaskExecutor {
    private Logger logger = LoggerFactory.getLogger(WalletTransferTask.class);

    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private WalletTransferService walletTransferService;

    @Override
    public void execute() {
        logger.debug("------------------------START转账记录转移到用户系统-----------------------");
        List<WalletTransfer> list = walletTransferService.findForTransfer();
        for (WalletTransfer transfer : list) {
            Fuser fuser = frontUserService.findById(transfer.getUserId());

            logger.info("转账订单号：" + transfer.getId());

            Map<String, Object> param = new TreeMap();
            param.put("orderId", transfer.getId());
            param.put("openId", fuser.getZhgOpenId());
            param.put("amount", transfer.getAmount());
            param.put("coinType", transfer.getMoneyType().getIndex());
            param.put("accSysNum", transfer.getToSystem());
            if (transfer.isVirtualCoin()) {
                param.put("virtualCoinId", transfer.getVirtualCoinId());
            }
            param.put("sysNum", constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_TAG));
            param.put("sysSecret", constantMap.getString(ConstantKeys.USER_TRANSFER_SYSTEM_SECRET));
            param.put("sign", SignatureUtil.getSign(param));

            param.remove("sysSecret");

            JSONObject resultJson = HttpUtils.sendPostRequestForJson(constantMap.getString(ConstantKeys.USER_TRANSFER_URL), param);
            String retCode = resultJson.getString("ret");

            if ("1".equals(retCode)) {
                WalletTransfer walletTransfer = walletTransferService.findById(transfer.getId());
                walletTransfer.setTradeNo(resultJson.getString("traId"));
                walletTransfer.setUpdateTime(Utils.getTimestamp());
                walletTransfer.setStatus(WalletTransferStatus.Going);
                walletTransferService.update(walletTransfer);
            } else if ("-8".equals(retCode)) {
                // 无法转账 退款
                logger.debug("不支持转账，订单号为：" + transfer.getId());
                walletTransferService.updateRefund(transfer);
            } else if ("0".equals(retCode)) {
                // 重复订单
                logger.debug("重复转账，订单号为：" + transfer.getId());
            } else {
                logger.error("转账失败，用户系统响应：" + resultJson);
            }
        }
        logger.debug("------------------------END转账记录转移到用户系统-----------------------");
    }
}
