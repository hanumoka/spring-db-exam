package com.example.springdbexam.conection;

import com.example.springdbexam.connection.DBConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DBConnectionUtilTest {
    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull(); }
}
