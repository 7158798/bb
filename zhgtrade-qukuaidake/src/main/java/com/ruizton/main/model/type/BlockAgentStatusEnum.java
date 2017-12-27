package com.ruizton.main.model.type;

/**
 * 招股金服
 * CopyRight: www.zhgtrade.com
 * Author: xuelin
 * Date: 17-3-2 下午3:32
 */
public enum BlockAgentStatusEnum {
    UNAUDITED("未审核"), NOT_PASSED("未通过"), PASSED("已通过");

    private String name;

    BlockAgentStatusEnum() {}

    BlockAgentStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static BlockAgentStatusEnum get(int index){
        BlockAgentStatusEnum status = null;
        for(BlockAgentStatusEnum statusEnum : BlockAgentStatusEnum.values()){
            if(index == statusEnum.ordinal()) {
                status = statusEnum;
                break;
            }
        }
        return status;
    }
}
