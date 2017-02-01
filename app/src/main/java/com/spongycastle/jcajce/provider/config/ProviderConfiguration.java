package com.spongycastle.jcajce.provider.config;

import javax.crypto.spec.DHParameterSpec;

import com.spongycastle.jce.spec.ECParameterSpec;

public interface ProviderConfiguration
{
    ECParameterSpec getEcImplicitlyCa();

    DHParameterSpec getDHDefaultParameters(int keySize);
}
