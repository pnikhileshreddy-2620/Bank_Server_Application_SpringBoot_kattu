package com.cg.numbergenerator;

import java.util.Random;

public class NumberGenerator {
	private final int ACCOUNT_NUMBER_LENGTH = 8;

    private final Random random = new Random();

    public  int generateNumber() {
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return Integer.parseInt(accountNumber.toString());
    }

}
