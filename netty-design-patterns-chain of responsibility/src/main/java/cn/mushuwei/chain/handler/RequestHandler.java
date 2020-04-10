package cn.mushuwei.chain.handler;


import cn.mushuwei.chain.quest.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james mu
 * @date 2020/4/10 17:12
 */
public abstract class RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

    private RequestHandler next;

    public RequestHandler(RequestHandler next) {
        this.next = next;
    }

    public void handleRequest(Request req) {
        if (next != null) {
            next.handleRequest(req);
        }
    }

    protected void printHandling(Request req) {
        LOGGER.info("{} 处理请求： \"{}\"", this, req);
    }

    @Override
    public abstract String toString();

}
