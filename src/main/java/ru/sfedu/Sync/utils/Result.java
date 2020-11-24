package ru.sfedu.Sync.utils;

public class Result<T> {
    public final T data;
    public final ResultType resultType;
    public final String message;

    public Result(T data, ResultType resultType, String message) {
        this.data = data;
        this.resultType = resultType;
        this.message = message;
    }
}
