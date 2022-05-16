package org.example;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private Socket clientSocket;
    private static BufferedReader in = null;
    private static PrintWriter out = null;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handle() {
        in = allocateReader(clientSocket);
        out = allocateWriter(clientSocket);
        buildProductList();
        handleInput();
    }

    private BufferedReader allocateReader(Socket clientSocket) {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Reader failed" + e);
            return null;
        } return in;
    }

    private PrintWriter allocateWriter(Socket clientSocket) {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } return out;
    }

    public void handleInput() {
        String userInput;
        try {
            while ((userInput = in.readLine()) != null) {
                System.out.println("Client: " + (userInput));
                out.println(process(userInput));
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }

    static ArrayList<Product> products = new ArrayList<>();

    static void buildProductList() {
        products.add(new Product(36213,"Huawei Honor 8 BLACK",25.94, 6));
        products.add(new Product(36214,"Huawei Honor 8 RED",23.94, 1));
        products.add(new Product(36215,"Apple IPhone 13 RED",1226.94, 10));
    }

    private void cheapest() {
        products.sort((Product product1, Product product2) -> {
            if (product1.getPrice() > product2.getPrice())
                return 1;
            if (product1.getPrice() < product2.getPrice())
                return -1;
            return 0;
        });
    }

    public String process(String input) {

        Gson gson = new Gson();

        String frase;
        String risultato;

        if (input.equals("cheapest")) {
            frase = "Prodotto meno costoso: ";

            cheapest();
            Product cheapest = products.get(0);
            risultato = gson.toJson(cheapest);

            return frase + risultato;

        } else if (input.equals("all")) {
            frase = "Lista di tutti i prodotti: ";
            risultato = gson.toJson(products);

            return frase + risultato;

        } else if (input.equals("all_sorted")) {
            frase = "Lista ordinata di tutti i prodotti in base al prezzo: ";

            cheapest();
            risultato = gson.toJson(products);
            return frase + risultato;

        } else {
            risultato = "Comandi: 'cheapest' | 'all' | 'all_sorted' ";
            return risultato;
        }

    }



}