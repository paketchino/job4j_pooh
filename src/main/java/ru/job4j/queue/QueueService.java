package ru.job4j.queue;

import ru.job4j.Req;
import ru.job4j.Resp;
import ru.job4j.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap();
    @Override
    public Resp process(Req rea) {
        Resp resp = null;
        if (queue.isEmpty()) {
            resp = new Resp("", "204");
        }
        if ("POST".equals(rea.getHttpRequestType())) {
            queue.putIfAbsent(rea.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(rea.getSourceName()).add(rea.getParam());
        } else if ("GET".equals(rea.getHttpRequestType())) {
            String text = queue.get(rea.getSourceName()).poll();
            return resp == null ? new Resp(text, "200") : new Resp("", "204");
        }
        return resp;
    }
}
