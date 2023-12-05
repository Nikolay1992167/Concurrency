package by.clevertec.entity;

import by.clevertec.exception.ClientException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ClientTest {

    private static final int DATA_SIZE = 10;
    private static final int NUMBER_OF_THREADS = 4;
    private Client client;

    @BeforeEach
    void setup() {
        client = new Client(DATA_SIZE, NUMBER_OF_THREADS);
    }

    @Test
    void shouldThreadReturnNotZeroValue() throws ExecutionException, InterruptedException {
        // given
        Future<Client.Request> future = client.getExecutor().submit(client.new Request(1));

        // when
        int actual = future.get().getDataRequest();

        // then
        assertThat(actual).isNotZero();
    }

    @Test
    void shouldThreadReturnValidValue() throws ExecutionException, InterruptedException {
        // given
        Future<Client.Request> future = client.getExecutor().submit(client.new Request(1));

        // when
        int validValue = future.get().getDataRequest();

        // then
        assertThat(validValue >= 1 && validValue <= DATA_SIZE).isTrue();
    }

    @Test
    void shouldThreadRemoveDifferentValuesFromDataList() throws ExecutionException, InterruptedException {
        // given
        List<Future<Client.Request>> futures = List.of(
                client.getExecutor().submit(client.new Request(1)),
                client.getExecutor().submit(client.new Request(2))
        );

        // when
        int result1 = futures.get(0).get().getDataRequest();
        int result2 = futures.get(1).get().getDataRequest();

        // then
        assertThat(result1).isNotEqualTo(result2);
    }

    @Test
    void shouldReducedDataListSizeByRequestThread() {
        // given
        List<Future<Client.Request>> futures = List.of(
                client.getExecutor().submit(client.new Request(1)),
                client.getExecutor().submit(client.new Request(2))
        );

        // when
        futures.forEach(future -> client.getServer().processRequest(future));

        // then
        assertThat(client.getData()).hasSize(DATA_SIZE - 2);
    }

    @Test
    void shouldCheckThatRemovedValuesFromDataByThread() throws ExecutionException, InterruptedException {
        // given
        List<Future<Client.Request>> futures = List.of(
                client.getExecutor().submit(client.new Request(1)),
                client.getExecutor().submit(client.new Request(2))
        );

        // when
        int valueFirst = futures.get(0).get().getDataRequest();
        int valueSecond = futures.get(1).get().getDataRequest();

        // then
        assertThat(client.getData()).doesNotContain(valueFirst, valueSecond);
    }

    @Test
    void shouldTreadNotThrowAnyExceptionAndCheckForChangesListOfDataByThreadSendRequest() {
        // when, then
        assertThatCode(() -> client.sendRequest())
                .doesNotThrowAnyException();
        assertThat(client.getServer().getResourceList())
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    void shouldCheckAccumulatorByThreadSendRequest() {
        // when, then
        assertThat(client.getAccumulator().get())
                .isNotNull()
                .isGreaterThanOrEqualTo(0);
    }

    @Test
    void shouldThrowsExceptionBySendRequest() {

    }
}