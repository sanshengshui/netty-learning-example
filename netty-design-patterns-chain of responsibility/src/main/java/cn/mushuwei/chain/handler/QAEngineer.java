package cn.mushuwei.chain.handler;

import cn.mushuwei.chain.quest.Request;
import cn.mushuwei.chain.quest.RequestType;

/**
 * @author james mu
 * @date 2020/4/10 17:30
 */
public class QAEngineer extends RequestHandler {

    public QAEngineer(RequestHandler handler) {
        super(handler);
    }

    @Override
    public void handleRequest(Request req) {
        if (RequestType.QA_ENGINNER == req.getRequestType()) {
            printHandling(req);
            req.markHandled();
        } else {
            super.handleRequest(req);
        }
    }


    @Override
    public String toString() {
        return "测试工程师";
    }

}
