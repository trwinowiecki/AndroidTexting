package com.spongycastle.openpgp.jcajce;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPSecretKeyRingCollection;
import com.spongycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;

public class JcaPGPSecretKeyRingCollection
    extends PGPSecretKeyRingCollection
{
    public JcaPGPSecretKeyRingCollection(byte[] encoding)
        throws IOException, PGPException
    {
        this(new ByteArrayInputStream(encoding));
    }

    public JcaPGPSecretKeyRingCollection(InputStream in)
        throws IOException, PGPException
    {
        super(in, new JcaKeyFingerprintCalculator());
    }

    public JcaPGPSecretKeyRingCollection(Collection collection)
        throws IOException, PGPException
    {
        super(collection);
    }
}
