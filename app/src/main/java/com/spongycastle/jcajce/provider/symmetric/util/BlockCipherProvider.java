package com.spongycastle.jcajce.provider.symmetric.util;

import com.spongycastle.crypto.BlockCipher;

public interface BlockCipherProvider
{
    BlockCipher get();
}
