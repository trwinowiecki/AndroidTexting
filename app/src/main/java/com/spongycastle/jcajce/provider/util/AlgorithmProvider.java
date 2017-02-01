package com.spongycastle.jcajce.provider.util;

import com.spongycastle.jcajce.provider.config.ConfigurableProvider;

public abstract class AlgorithmProvider
{
    public abstract void configure(ConfigurableProvider provider);
}
