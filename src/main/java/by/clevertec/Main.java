package by.clevertec;

import by.clevertec.entity.Client;

public class Main {

    public static void main(String[] args) {
        Client client = new Client(100, 8);
        client.sendRequest();
    }

}