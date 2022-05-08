package ru.job4j.queue;

import ru.job4j.Req;
import ru.job4j.Resp;
import ru.job4j.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req rea) {
        Resp resp = new Resp("", "204");
        if ("GET".equals(rea.getHttpRequestType()) && ("topic".equals(rea.getPoohMode()))) {
            topics.putIfAbsent(rea.getSourceName(), new ConcurrentHashMap<>());
            topics.get(rea.getSourceName()).putIfAbsent(rea.getParam(), new ConcurrentLinkedQueue<>());
            String text = topics.get(rea.getSourceName()).get(rea.getParam()).poll();
            resp = text == null ? new Resp("", "204") : new Resp(text, "200");
        } else if ("POST".equals(rea.getHttpRequestType()) && "topic".equals(rea.getPoohMode())) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = topics.get(rea.getSourceName());
            for (ConcurrentLinkedQueue linkedQueue : topic.values()) {
                 linkedQueue.add(rea.getParam());
            }
        }
        return resp;
    }
}
