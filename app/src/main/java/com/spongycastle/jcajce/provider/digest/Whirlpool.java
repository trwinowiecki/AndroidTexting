package com.spongycastle.jcajce.provider.digest;

import com.spongycastle.crypto.CipherKeyGenerator;
import com.spongycastle.crypto.digests.WhirlpoolDigest;
import com.spongycastle.crypto.macs.HMac;
import com.spongycastle.jcajce.provider.config.ConfigurableProvider;
import com.spongycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import com.spongycastle.jcajce.provider.symmetric.util.BaseMac;

public class Whirlpool
{
    private Whirlpool()
    {

    }

    static public class Digest
        extends BCMessageDigest
        implements Cloneable
    {
        public Digest()
        {
            super(new WhirlpoolDigest());
        }

        public Object clone()
            throws CloneNotSupportedException
        {
            Digest d = (Digest)super.clone();
            d.digest = new WhirlpoolDigest((WhirlpoolDigest)digest);

            return d;
        }
    }

    /**
     * Tiger HMac
     */
    public static class HashMac
        extends BaseMac
    {
        public HashMac()
        {
            super(new HMac(new WhirlpoolDigest()));
        }
    }

    public static class KeyGenerator
        extends BaseKeyGenerator
    {
        public KeyGenerator()
        {
            super("HMACWHIRLPOOL", 512, new CipherKeyGenerator());
        }
    }

    public static class Mappings
        extends DigestAlgorithmProvider
    {
        private static final String PREFIX = Whirlpool.class.getName();

        public Mappings()
        {
        }

        public void configure(ConfigurableProvider provider)
        {
            provider.addAlgorithm("MessageDigest.WHIRLPOOL", PREFIX + "$Digest");

            addHMACAlgorithm(provider, "WHIRLPOOL", PREFIX + "$HashMac", PREFIX + "$KeyGenerator");
        }
    }
}
