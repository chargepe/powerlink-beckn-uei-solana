package com.bekn.energyp2p.constants;

public class ParentPaymentWallet {
    private static final String parentWalletSecretKey = "2NPATk5SeyXqBt45jz9Cs4UYdz9X1VTdBuVBpnbuYRGqSWNh6ysEPtz4o5T1r2uQR9fnBe5tMfwbCaGHuxYfX7We";
    private static final String parentWalletPublicKey = "Eb99VQpsUiRummbZmcYRv7GZo9g7w2QqYMGE2VGDvYir";

    public static String getSecretKey() {
        return parentWalletSecretKey;
    }

    public static String getPublicKey() {
        return parentWalletPublicKey;
    }
}