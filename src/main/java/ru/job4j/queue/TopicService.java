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
        Resp resp = null;
        if ("GET".equals(rea.getHttpRequestType()) && ("topic".equals(rea.getPoohMode()))) {
            topics.get(rea.getSourceName()).putIfAbsent(rea.getSourceName(), new ConcurrentLinkedQueue<>());
            topics.get(rea.getSourceName()).get(rea.getSourceName()).add(rea.getParam());
            resp = new Resp("", "200");
        } else if ("POST".equals(rea.getHttpRequestType()) && "topic".equals(rea.getPoohMode())) {
            topics.get(rea.getSourceName()).get(rea.getSourceName()).poll();
            resp = new Resp("temperature=18", "200");
        }
        return resp;
    }
}
