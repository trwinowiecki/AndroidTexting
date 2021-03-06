package com.spongycastle.jcajce.provider.symmetric;

import com.spongycastle.crypto.CipherKeyGenerator;
import com.spongycastle.crypto.engines.HC256Engine;
import com.spongycastle.jcajce.provider.config.ConfigurableProvider;
import com.spongycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import com.spongycastle.jcajce.provider.symmetric.util.BaseStreamCipher;
import com.spongycastle.jcajce.provider.util.AlgorithmProvider;

public final class HC256
{
    private HC256()
    {
    }
    
    public static class Base
        extends BaseStreamCipher
    {
        public Base()
        {
            super(new HC256Engine(), 32);
        }
    }

    public static class KeyGen
        extends BaseKeyGenerator
    {
        public KeyGen()
        {
            super("HC256", 256, new CipherKeyGenerator());
        }
    }

    public static class Mappings
        extends AlgorithmProvider
    {
        private static final String PREFIX = HC256.class.getName();

        public Mappings()
        {
        }

        public void configure(ConfigurableProvider provider)
        {
            provider.addAlgorithm("Cipher.HC256", PREFIX + "$Base");
            provider.addAlgorithm("KeyGenerator.HC256", PREFIX + "$KeyGen");
        }
    }
}
