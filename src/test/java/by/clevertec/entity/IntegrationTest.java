package by.clevertec.entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private static final int DATA_SIZE = 10;
    private static final int NUMBER_OF_THREADS = 4;
    private static Client client;
    private static Server server;

    @BeforeAll
    static void setup() {
        client = new Client(DATA_SIZE, NUMBER_OF_THREADS);
        client.sendRequest();
        server = client.getServer();
    }

    @Test
    void shouldReturnEmptyListAfterRunRequests() {
        // given, when
        List<Integer> actualList = client.getData();

        // then
        assertThat(actualList).isEmpty();
    }

    @Test
    void shouldReturnAccumulatorSumByFormula() {
        // given
        int expectedSum = (1 + DATA_SIZE) * (DATA_SIZE / 2);

        // when
        int actualSum = client.getAccumulator().get();

        // then
        assertThat(actualSum).isEqualTo(expectedSum);
    }

    @Test
    void shouldCheckResourceListSize() {
        // given, when
        int actualSize = server.getResourceList().size();

        //then
        assertThat(actualSize).isEqualTo(DATA_SIZE);
    }

    @Test
    void shouldCheckUniqueValuesInResourceList() {
        // given, when
        Set<Integer> resourceSet = new HashSet<>(server.getResourceList());

        // then
        assertThat(server.getResourceList()).containsAll(resourceSet);
    }
}
