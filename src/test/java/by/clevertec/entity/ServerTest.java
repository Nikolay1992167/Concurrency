package by.clevertec.entity;

import by.clevertec.exception.ServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ServerTest {

    private static final int DATA_SIZE = 10;
    private static final int NUMBER_OF_THREADS = 4;
    private Server server;
    private Client client;

    @BeforeEach
    void setup() {
        server = new Server();
        client = new Client(DATA_SIZE, NUMBER_OF_THREADS);
    }

    @Test
    void shouldReturnExpectedSizeOfResponseList() {
        // given
        Future<Client.Request> future = client.getExecutor().submit(client.new Request(1));
        Server.Response response = server.processRequest(future);

        //when
        int actual = response.message();

        // then
        assertThat(actual).isEqualTo(server.getResourceList().size());
    }

    @Test
    void shouldCheckAddedValuesToResponseList() throws ExecutionException, InterruptedException {
        // given
        List<Future<Client.Request>> futures = List.of(
                client.getExecutor().submit(client.new Request(1)),
                client.getExecutor().submit(client.new Request(2))
        );
        futures.forEach(future -> server.processRequest(future));

        // when
        int result1 = futures.get(0).get().getDataRequest();
        int result2 = futures.get(1).get().getDataRequest();

        // then
        assertThat(server.getResourceList()).contains(result1, result2);
    }

    @Test
    void shouldThrowsExceptionForProcessRequest() {
        // given
        Future<Client.Request> future = CompletableFuture.failedFuture(new RuntimeException("Exception execution!"));

        // when, then
        assertThatExceptionOfType(ServerException.class)
                .isThrownBy(() -> server.processRequest(future))
                .withCauseInstanceOf(ExecutionException.class)
                .withMessageContaining("Exception execution!");
    }
}