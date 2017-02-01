package com.spongycastle.openpgp.jcajce;

import java.io.IOException;
import java.io.InputStream;

import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPSecretKeyRing;
import com.spongycastle.openpgp.operator.KeyFingerPrintCalculator;
import com.spongycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;

public class JcaPGPSecretKeyRing
    extends PGPSecretKeyRing
{
    private static KeyFingerPrintCalculator fingerPrintCalculator = new JcaKeyFingerprintCalculator();

    public JcaPGPSecretKeyRing(byte[] encoding)
        throws IOException, PGPException
    {
        super(encoding, fingerPrintCalculator);
    }

    public JcaPGPSecretKeyRing(InputStream in)
        throws IOException, PGPException
    {
        super(in, fingerPrintCalculator);
    }
}
