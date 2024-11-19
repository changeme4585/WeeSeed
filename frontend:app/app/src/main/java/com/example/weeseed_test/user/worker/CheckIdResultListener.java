package com.example.weeseed_test.user.worker;

public interface CheckIdResultListener {
    void oncheckIdSuccess();
    void oncheckIdFailure(String msg);
    void oncheckIdError(String msg);
}
