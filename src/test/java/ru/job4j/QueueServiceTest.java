package ru.job4j;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.queue.QueueService;

public class QueueServiceTest {
    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test(expected = NullPointerException.class)
    public void whenBeginGetThenNull() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
    }
}