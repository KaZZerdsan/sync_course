package ru.sfedu.Sync.utils;

import java.util.List;

public class Result<T> {
    private final List<T> data;
    private final ResultType resultType;
    private final String message;

    public List<T> getData() {
        return data;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public String getMessage() {
        return message;
    }

    public Result(List<T> data, ResultType resultType, String message) {
        this.data = data;
        this.resultType = resultType;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultType=" + resultType +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
