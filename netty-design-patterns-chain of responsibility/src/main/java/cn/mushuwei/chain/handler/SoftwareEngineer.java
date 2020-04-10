package cn.mushuwei.chain.handler;

import cn.mushuwei.chain.quest.Request;
import cn.mushuwei.chain.quest.RequestType;

/**
 * @author james mu
 * @date 2020/4/10 17:25
 */
public class SoftwareEngineer extends RequestHandler {

    public SoftwareEngineer(RequestHandler handler) {
        super(handler);
    }

    @Override
    public void handleRequest(Request req) {
        if (RequestType.SOFTWARE_ENGINNER == req.getRequestType()) {
            printHandling(req);
            req.markHandled();
        } else {
            super.handleRequest(req);
        }
    }

    @Override
    public String toString() {
        return "软件工程师";
    }

}
