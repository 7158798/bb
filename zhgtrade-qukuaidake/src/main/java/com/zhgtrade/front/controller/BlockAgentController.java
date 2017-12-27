package com.zhgtrade.front.controller;

import com.ruizton.main.model.BlockAgent;
import com.ruizton.main.model.type.BlockAgentStatusEnum;
import com.ruizton.main.service.block.BlockAgentService;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
public class BlockAgentController extends ApiBaseController {
    @Autowired
    private BlockAgentService blockAgentService;

    /**
     *
     * @param realName
     * @param sex
     * @param identifyNo
     * @param job
     * @param companyAddress
     * @param company
     * @param mobile
     * @param email
     * @param wechatNo
     * @return
     */
    @RequestMapping("/applyAgent")
    public Object applyAgent(@RequestParam(name = "name") String realName,
                                    @RequestParam(name = "sex") short sex,
                                    @RequestParam(name = "idCard") String identifyNo,
                                    @RequestParam(name = "job") String job,
                                    @RequestParam(name = "location") String companyAddress,
                                    @RequestParam(name = "company") String company,
                                    @RequestParam(name = "tel") String mobile,
                                    @RequestParam(name = "email") String email,
                                    @RequestParam(name = "weixin") String wechatNo){

        BlockAgent agent = new BlockAgent();
        agent.setRealName(realName);
        agent.setSex(sex);
        agent.setStatus(BlockAgentStatusEnum.UNAUDITED);
        agent.setIdentifyNo(identifyNo);
        agent.setJob(job);
        agent.setCompany(company);
        agent.setCompanyAddress(companyAddress);
        agent.setMobile(mobile);
        agent.setEmail(email);
        agent.setWechatNo(wechatNo);
        agent.setCreateTime(Utils.getTimestamp());

        blockAgentService.saveApplyAgent(agent);
        return forSuccessResult(agent.getId());
    }
}

