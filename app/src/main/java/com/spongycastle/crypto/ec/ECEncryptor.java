package com.spongycastle.crypto.ec;

import com.spongycastle.crypto.CipherParameters;
import com.spongycastle.math.ec.ECPoint;

public interface ECEncryptor
{
    void init(CipherParameters params);

    ECPair encrypt(ECPoint point);
}
