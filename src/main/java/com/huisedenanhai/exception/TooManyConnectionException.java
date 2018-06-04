/*
 * Created by huisedenanhai on 2018/06/04
 */

package com.huisedenanhai.exception;

public class TooManyConnectionException extends Exception {
    public TooManyConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
