package com.spongycastle.crypto.ec;

import com.spongycastle.crypto.CipherParameters;
import com.spongycastle.math.ec.ECPoint;

public interface ECDecryptor
{
    void init(CipherParameters params);

    ECPoint decrypt(ECPair cipherText);
}
