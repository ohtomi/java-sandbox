package com.github.ohtomi.java.sandbox.consumer;

import com.github.ohtomi.java.sandbox.provider.FooProvider;
import com.github.ohtomi.java.sandbox.spi.FooService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class FooClientTest {

    @Test
    public void testStub() {
        FooService stub = mock(FooService.class);
        when(stub.execute(anyInt())).thenReturn("-");
        when(stub.execute(1)).thenReturn("xxx");

        FooClient client = new FooClient(stub);

        String actual = client.fizzBuzz(1);
        String expected = "## xxx ##";
        assertEquals(expected, actual);
    }

    @Test
    public void testSpy() {
        FooService spy = spy(FooProvider.class);
        FooClient client = new FooClient(spy);

        String actual = client.fizzBuzz(1);
        String expected = "## 1 ##";
        assertEquals(expected, actual);
        verify(spy).execute(1);
    }
}
