package com.spongycastle.openpgp.jcajce;

import java.io.IOException;
import java.io.InputStream;

import com.spongycastle.openpgp.PGPPublicKeyRing;
import com.spongycastle.openpgp.operator.KeyFingerPrintCalculator;
import com.spongycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;

public class JcaPGPPublicKeyRing
    extends PGPPublicKeyRing
{
    private static KeyFingerPrintCalculator fingerPrintCalculator = new JcaKeyFingerprintCalculator();

    public JcaPGPPublicKeyRing(byte[] encoding)
        throws IOException
    {
        super(encoding, fingerPrintCalculator);
    }

    public JcaPGPPublicKeyRing(InputStream in)
        throws IOException
    {
        super(in, fingerPrintCalculator);
    }
}
