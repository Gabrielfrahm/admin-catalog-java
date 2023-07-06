package com.admin.catalog.infra;

import com.admin.catalog.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");
        System.out.println(new UseCase().execute());
    }
}