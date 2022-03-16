package stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TqsStackTest {

    TqsStack<String> wordsStack;

    @BeforeEach
    void setUp() {
        wordsStack = new TqsStack<>();
    }

    @DisplayName("A stack is empty on construction")
    @Test
    void isEmpty() {
        assertTrue(wordsStack.isEmpty());
    }

    @DisplayName("A stack has size 0 on construction")
    @Test
    void hasSizeZero() {
        assertEquals(0, wordsStack.size());
    }

    @DisplayName("After n pushes to an empty stack, n > 0, the stack is not empty and its size is n")
    @Test
    void afterPushes() {
        wordsStack.push("A");
        wordsStack.push("B");
        wordsStack.push("C");

        assertAll( ()-> {
            assertEquals(3, wordsStack.size());
            assertFalse(wordsStack.isEmpty());
        });
    }

    @DisplayName("If one pushes x then pops, the value popped is x")
    @Test
    void pop() {
        wordsStack.push("A");
        assertEquals("A", wordsStack.pop());
    }

    @DisplayName("If one pushes x then peeks, the value returned is x, but the size stays the same")
    @Test
    void peek() {
        wordsStack.push("A");
        int size = wordsStack.size();

        assertAll( () -> {
            assertEquals("A", wordsStack.peek());
            assertEquals(size, wordsStack.size());

        });
    }

    @DisplayName("If the size is n, then after n pops, the stack is empty and has a size 0")
    @Test
    void popAllElements() {
        wordsStack.push("A");
        wordsStack.push("B");
        wordsStack.push("C");
        wordsStack.push("D");

        int n = wordsStack.size();
        for (int i = 0; i < n; i++)
            wordsStack.pop();

        assertAll( () -> {
            assertTrue(wordsStack.isEmpty());
            assertEquals(0, wordsStack.size());

        });
    }

    @DisplayName("Popping from an empty stack does throw a NoSuchElementException")
    @Test
    void popEmptyStack() {
        assertThrows(NoSuchElementException.class, () -> wordsStack.pop());
    }

    @DisplayName("Peeking into an empty stack does throw a NoSuchElementException")
    @Test
    void peekEmptyStack() {
        assertThrows(NoSuchElementException.class, () -> wordsStack.peek());
    }

    @DisplayName("For bounded stacks only: pushing onto a full stack does throw an IllegalStateException")
    @Test
    void bounded() {
        wordsStack.setMaxSize(3);
        for (int i = 0; i < wordsStack.getMaxSize(); i++)
            wordsStack.push("A");
        assertThrows(IllegalStateException.class, () -> wordsStack.push("B"));

    }
}