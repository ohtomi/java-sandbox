package com.github.ohtomi.java.sandbox.consumer;

import com.github.ohtomi.java.sandbox.spi.FooService;

public class FooClient {

    private final FooService fooService;

    public FooClient(FooService fooService) {
        this.fooService = fooService;
    }

    String fizzBuzz(int i) {
        return "## " + this.fooService.execute(i) + " ##";
    }
}
