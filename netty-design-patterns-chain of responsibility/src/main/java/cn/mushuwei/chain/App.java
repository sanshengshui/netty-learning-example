package cn.mushuwei.chain;

import cn.mushuwei.chain.quest.Request;
import cn.mushuwei.chain.quest.RequestType;

/**
 * @author james mu
 * @date 2020/4/10 17:15
 */
public class App {

    public static void main(String[] args) {

        ITCompany king = new ITCompany();
        king.makeRequest(new Request(RequestType.PRODUCT_MANGER, "业务决策、目标拆分、需求分析和协同落地"));
        king.makeRequest(new Request(RequestType.SOFTWARE_ENGINNER, "负责架构设计与开发，能够带领团队进行技术攻关"));
        king.makeRequest(new Request(RequestType.QA_ENGINNER, "缺陷跟踪、软件质量度量及风险预估"));

    }

}
