package com.github.ohtomi.java.sandbox.provider;

import com.github.ohtomi.java.sandbox.spi.FooService;

public class FooProvider implements FooService {

    @Override
    public String execute(int i) {
        if (i % 15 == 0) {
            return "FizzBuzz";
        } else if (i % 3 == 0) {
            return "Fizz";
        } else if (i % 5 == 0) {
            return "Buzz";
        } else {
            return i + "";
        }
    }
}
