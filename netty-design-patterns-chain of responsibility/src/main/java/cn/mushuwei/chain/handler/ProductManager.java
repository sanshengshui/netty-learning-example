package cn.mushuwei.chain.handler;

import cn.mushuwei.chain.quest.Request;
import cn.mushuwei.chain.quest.RequestType;

/**
 * @author james mu
 * @date 2020/4/10 17:12
 */
public class ProductManager extends RequestHandler {

    public ProductManager(RequestHandler handler) {
        super(handler);
    }

    @Override
    public void handleRequest(Request req) {
        if (RequestType.PRODUCT_MANGER == req.getRequestType()) {
            printHandling(req);
            req.markHandled();
        } else {
            super.handleRequest(req);
        }
    }

    @Override
    public String toString() {
        return "产品经理";
    }

}
