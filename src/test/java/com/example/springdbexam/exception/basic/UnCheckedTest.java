package com.example.springdbexam.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedTest {

    @Test
    public void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    public void unchecked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyUnCheckedException.class);
    }

    /**
     * RuntimeException을 상속받으면 언체크드 예외가 된다.
     */
    static class MyUnCheckedException extends RuntimeException {
        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    /**
     * 언체크드 예외는 명시적으로 잡거나 던지지 않아도 컴파일시 오류 발생 안함.
     * 아무것도 하지 않으면, 자동으로 밖으로 던져지게 된다.
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 된다.
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUnCheckedException e) {
                log.info("예외처리, message: {}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지않아도, 자동으로 밖으로 던진다.
         */
        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        public void call() {
            throw new MyUnCheckedException("ex");
        }
    }
}
