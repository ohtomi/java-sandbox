package com.github.ohtomi.java.sandbox.consumer;

import com.github.ohtomi.java.sandbox.spi.FooService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FooClientTest {

    @Test
    public void testStub() {
        FooService stub = mock(FooService.class);
        when(stub.execute(anyInt())).thenReturn("-");
        when(stub.execute(1)).thenReturn("1");
        when(stub.execute(2)).thenReturn("2");
        when(stub.execute(3)).thenReturn("Fizz");
        when(stub.execute(4)).thenReturn("4");
        when(stub.execute(5)).thenReturn("Buzz");

        FooClient client = new FooClient(stub);

        String actual = client.fizzBuzz(1);
        String expected = "## 1 ##";
        assertEquals(expected, actual);
    }
}
