package cn.mushuwei.chain;

import cn.mushuwei.chain.handler.ProductManager;
import cn.mushuwei.chain.handler.QAEngineer;
import cn.mushuwei.chain.handler.RequestHandler;
import cn.mushuwei.chain.handler.SoftwareEngineer;
import cn.mushuwei.chain.quest.Request;

/**
 * @author james mu
 * @date 2020/4/10 17:25
 */
public class ITCompany {

    private RequestHandler chain;

    public ITCompany() {
        buildChain();
    }

    private void buildChain() {
        chain = new ProductManager(new SoftwareEngineer(new QAEngineer(null)));
    }

    public void makeRequest(Request req) {
        chain.handleRequest(req);
    }
}
