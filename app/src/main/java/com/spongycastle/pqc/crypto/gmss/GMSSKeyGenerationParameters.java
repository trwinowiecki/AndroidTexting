package com.spongycastle.pqc.crypto.gmss;

import java.security.SecureRandom;

import com.spongycastle.crypto.KeyGenerationParameters;

public class GMSSKeyGenerationParameters
    extends KeyGenerationParameters
{

    private GMSSParameters params;

    public GMSSKeyGenerationParameters(
        SecureRandom random,
        GMSSParameters params)
    {
        // XXX key size?
        super(random, 1);
        this.params = params;
    }

    public GMSSParameters getParameters()
    {
        return params;
    }
}
