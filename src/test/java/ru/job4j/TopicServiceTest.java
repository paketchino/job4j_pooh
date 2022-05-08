package ru.job4j;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.queue.TopicService;

public class TopicServiceTest {

    @Test
    public void whenTopicAddValueThen2QueueReturnThisValue() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForPublisher1 = "temperature=13";
        String client407 = "client407";
        String client6565 = "client6565";
        String t1000 = "T1000";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", client407)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", client407)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        topicService.process(
                new Req("GET", "topic", "weather", client6565));
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", client6565)
        );
        Resp resp = topicService.process(
                new Req("GET", "topic", "weather", t1000)
        );
        Resp result3 = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result4 = topicService.process(
                new Req("GET", "topic", "weather", t1000)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is("temperature=13"));
        assertThat(result3.text(), is(""));
        assertThat(resp.text(), is(""));
        assertThat(result4.text(), is("temperature=18"));
    }

    @Test(expected = NullPointerException.class)
    public void whenTopicNull() {
        TopicService topicService = new TopicService();
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", null)
        );
    }

    @Test
    public void whenTopicThenPost() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        assertThat(result1.text(), is("temperature=18"));
    }

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

}