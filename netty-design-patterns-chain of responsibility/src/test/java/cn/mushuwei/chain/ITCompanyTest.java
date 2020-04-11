package cn.mushuwei.chain;

import cn.mushuwei.chain.quest.Request;
import cn.mushuwei.chain.quest.RequestType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author james mu
 * @date 2020/4/10 18:04
 */
public class ITCompanyTest {

    private static final List<Request> REQUESTS =
            Arrays.asList(new Request(RequestType.PRODUCT_MANGER, "业务决策、目标拆分、需求分析和协同落地"),
                    new Request(RequestType.SOFTWARE_ENGINNER,"负责架构设计与开发，能够带领团队进行技术攻关"),
                    new Request(RequestType.QA_ENGINNER, "缺陷跟踪、软件质量度量及风险预估"));

    @Test
    public void testMakeQuest() {
        ITCompany itCompany = new ITCompany();

        REQUESTS.forEach(request -> {
            itCompany.makeRequest(request);
            assertTrue(
                    request.isHandled(),
                    "公司新项目所有需求希望被处理, 但是 [" + request + "] 没有!"
            );
        });
    }
}
